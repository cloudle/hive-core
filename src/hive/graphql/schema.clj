(ns hive.graphql.schema
  (:require [com.walmartlabs.lacinia.schema :as schema]))

(def hive-schema
  (schema/compile
    {:objects
     {:userAccount {:description "User account, could be internal user or an end-user with account"
                    :fields {:id {:type 'String}
                             :name {:type 'String}
                             :account {:type 'String}}}}
     :enums
     {:userType {:description "User Type on the system"
                 :values [:ADMIN :USER]}}
     :queries
     {:account {:type :userAccount
                :resolve (fn [context args v]
                           {:id "0001"
                            :name "Cloud Le"
                            :account "cloudle"})}
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