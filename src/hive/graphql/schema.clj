(ns hive.graphql.schema
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [hive.store.iam :as iam]))

(defn resolve-id
  [context args instance]
  (:db/id instance))

(defn resolve-field
  [context args instance]
  (let [selection (:com.walmartlabs.lacinia/selection context)
        field-definition (:field-definition selection)
        field-name (:field-name field-definition)
        type-name (:type-name field-definition)
        key (keyword (name type-name) (name field-name))]
    (key instance)))

(def datomic-id {:type 'String :resolve resolve-id})
(def datomic-string {:type 'String :resolve resolve-field})

(defn resolve-account [context args v]
  (let [account (:account args)]
    (iam/find-account account)))

(def hive-schema
  (schema/compile
    {:objects
     {:user {:description "User account, could be internal user or an end-user with account"
                    :fields {:id datomic-id
                             :name datomic-string
                             :email datomic-string
                             :account datomic-string}}}
     :enums
     {:userType {:description "User Type on the system"
                 :values [:ADMIN :USER]}}
     :queries
     {:account {:type :user
                :args {:account {:type 'String}}
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