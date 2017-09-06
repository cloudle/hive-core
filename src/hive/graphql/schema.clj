(ns hive.graphql.schema
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]))

(def user-type
  {:description "User account, could be internal user or an end-user with account"
   :fields {:id datomic-id
            :name datomic-string
            :email datomic-string
            :account datomic-string}})

(defn resolve-account [context args v]
  (let [user-email (get-in context [:user :email])]
    (-> (iam/find-account-by-email user-email) first)))

(def hive-schema
  (schema/compile
    {:objects
     {:user user-type}
     :enums
     {:userType {:description "User Type on the system"
                 :values [:ADMIN :USER]}}
     :queries
     {:account {:type :user
                :args {:account {:type 'String :default-value "cloudle"}}
                :resolve resolve-account}
      :greeting {:type 'String
                 :args {:userType {:type :userType}}
                 :resolve (constantly "world!")}
      :counter {:type 'String
                :resolve (constantly "1")}}
     :mutations
     {:increaseCounter {:type 'String
                        :resolve (fn [context args v] "2")}}
     :subscriptions
     {:logs {:type 'String
             :stream (constantly "world!")}}}))