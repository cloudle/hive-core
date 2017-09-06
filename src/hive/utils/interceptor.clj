(ns hive.utils.interceptor
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.content-negotiation :as conneg]
            [io.pedestal.interceptor :refer [interceptor]]
            [com.walmartlabs.lacinia.parser :refer [parse-query]]
            [cheshire.core :refer :all]))

(defn bad-request
  "Generates a bad request Ring response."
  ([body]
   (bad-request 400 body))
  ([status body]
   {:status status
    :headers {}
    :body body}))

(defn ^:private query-not-found-error
  [request]
  (let [request-method (get request :request-method)
        content-type (get-in request [:headers "content-type"])
        body (get request :body)]
    (cond
      (= request-method :get) "Query parameter 'query' is missing or blank."
      (nil? body) "Request body is empty."
      (and (= request-method :post) (= (:query body) nil)) "Your 'query' param under :post request's body is missing or blank"
      :else {:message "Request content type must be application/graphql or application/json."})))

(def graphql-missing-query-guard
  "Rejects the request when there's no GraphQL query in the request map.
   This must come after [[graphql-data-interceptor]], which is responsible for adding the query to the request map."
  {:name ::missing-query
   :enter (fn [context]
            (if (-> context :request :graphql-query clojure.string/blank?)
              (assoc context :response (bad-request (query-not-found-error (:request context))))
              context))})

(defmulti
  extract-query
  "Based on the content type of the query, adds up to three keys to the request:
  :graphql-query
  : The query itself, as a string (parsing the query happens later)
  :graphql-vars
  : A map of variables used when executing the query.
  :graphql-operation-name
  : The specific operation requested (for queries that define multiple named operations)."
  (fn [request]
    (get-in request [:headers "content-type"])))

(defmethod extract-query "application/json" [request]
  (let [body (parse-string (:body request) true)
        query (:query body)
        variables (:variables body)
        operation-name (:operationName body)]
    {:graphql-query query
     :graphql-vars variables
     :graphql-operation-name operation-name}))

(defmethod extract-query "application/graphql" [request]
  (let [query (:body request)
        variables (when-let [vars (get-in request [:query-params :variables])]
                    (parse-string vars true))]
    {:graphql-query query
     :graphql-vars variables}))

(defmethod extract-query :default [request]
  (let [query (get-in request [:query-params :query])]
    {:graphql-query query}))

;; Interceptors ----------------------------------------------
(def supported-types ["text/html" "application/edn" "application/json" "text/plain"])

(def content-negotiation (conneg/negotiate-content supported-types))

(def coerce-response-body
  "Coerce response type based on requested data type (by using Accept header)"
  {:name ::coerce-body
   :leave
         (fn [context]
           (let [accepted         (get-in context [:request :accept :field] "text/plain")
                 response         (get context :response)
                 body             (get response :body)
                 coerced-body     (case accepted
                                    "text/html"        body
                                    "text/plain"       body
                                    "application/edn"  (pr-str body)
                                    "application/json" (generate-string body))
                 updated-response (assoc response
                                    :headers {"Content-Type" accepted}
                                    :body    coerced-body)]
             (assoc context :response updated-response)))})

(def body-data
  "Converts the POSTed body from a input stream into a string."
  {:name ::body-data
   :enter (fn [context]
            (update-in context [:request :body] slurp))})

(def graphql-query-data
  "Extracts the raw data (query and variables) from the request using [[extract-query]]."
  {:name ::graphql-data
   :enter (fn [context]
            (let [request (:request context)
                  q (extract-query request)]
              (assoc context :request
                             (merge request q))))})

(defn graphql-query-parser
  "Given an schema, returns an interceptor that parses the query.
   Expected to come after [[missing-query-interceptor]] in the interceptor chain.
   Adds a new request key, :parsed-query."
  [schema]
  {:name ::query-parser
   :enter (fn [context]
            (try
              (let [q (get-in context [:request :graphql-query])
                    operation-name (get-in context [:request :graphql-operation-name])
                    parsed-query (parse-query schema q operation-name)]
                (assoc-in context [:request :parsed-query] parsed-query))
              (catch Exception e
                (assoc context :response
                               (bad-request
                                 {:errors [(assoc (ex-data e)
                                             :message (.getMessage e))]})))))})

(def graphql-status-conversion
  "Checks to see if any error map in the :errors key of the response
  contains a :status value.  If so, the maximum status value of such errors
  is found and used as the status of the overall response, and the
  :status key is dissoc'ed from all errors."
  {:name ::status-conversion
   :leave (fn [context]
            (let [response (:response context)
                  errors (get-in response [:body :errors])
                  statuses (keep :status errors)]
              (if (seq statuses)
                (let [max-status (reduce max (:status response) statuses)]
                  (-> context
                      (assoc-in [:response :status] max-status)
                      (assoc-in [:response :body :errors]
                                (map #(dissoc % :status) errors))))
                context)))})

(def inject-graphql-context
  "Adds a :graphql-context key to the request, used when executing the query.
  The provided graphql-context map is augmented with the request map, as key :request."
  {:name ::inject-app-context
   :enter (fn [context]
            (let [token (get-in context [:request :headers "token"])]
              (assoc-in context [:request :graphql-context]
                        (assoc {:token token} :request (:request context)))))})