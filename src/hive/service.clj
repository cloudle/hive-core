(ns hive.service
  (:require [io.pedestal.log :refer :all]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [io.pedestal.interceptor :refer [interceptor]]
            [cheshire.core :refer :all]
            [com.walmartlabs.lacinia :refer [execute execute-parsed-query]]
            [com.walmartlabs.lacinia.schema :as schema]
            [hive.utils.interceptor :as interceptors]))

(def hive-schema
  (schema/compile
   {:queries
     {:greeting {:type 'String
                 :resolve (constantly "world!")}}}))

(defn home-page [request]
  (let [query (get-in request [:params :query])]
       (ring-resp/response (str "Hello " query "!"))))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn ^:private apply-result-to-context
  [context result]
  ;; When :data is missing, then a failure occurred during parsing or preparing
  ;; the request, which indicates a bad request, rather than some failure
  ;; during execution.
  (let [status (if (contains? result :data) 200 400)
        response {:status status :headers {} :body result}]
    (assoc context :response response)))

;(defn api-page
;  [request]
;  (ring-resp/response (execute hive-schema "query { greeting }" nil nil)))

(defn api-page
  [request]
  (let [{query :parsed-query
         vars :graphql-vars
         context :graphql-context} request]
    (ring-resp/response (execute-parsed-query query vars context))))

(def hive-query-parser-interceptor (interceptors/graphql-query-parser hive-schema))
(def inject-hive-context-interceptor (interceptors/inject-graphql-context {:user-id "fake"}))

(def common-interceptors
  [(body-params/body-params)
   interceptors/coerce-response-body
   interceptors/content-negotiation])

(def graphql-get-interceptors
  [interceptors/coerce-response-body
   interceptors/content-negotiation
   interceptors/graphql-query-data
   interceptors/graphql-missing-query-guard
   hive-query-parser-interceptor
   interceptors/graphql-status-conversion
   inject-hive-context-interceptor])

(def graphql-post-interceptors
  [interceptors/coerce-response-body
   interceptors/content-negotiation
   interceptors/body-data
   interceptors/graphql-query-data
   interceptors/graphql-missing-query-guard
   hive-query-parser-interceptor
   interceptors/graphql-status-conversion
   inject-hive-context-interceptor])

;; Tabular routes
(def routes
  #{["/" :get (conj common-interceptors `home-page)]
    ["/about" :get (conj common-interceptors `about-page)]
    ["/api" :get (conj graphql-get-interceptors `api-page) :route-name :api-get]
    ["/api" :post (conj graphql-post-interceptors `api-page) :route-name :api-post]})

(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port 8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})
