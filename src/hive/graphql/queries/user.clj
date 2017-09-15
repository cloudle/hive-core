(ns hive.graphql.queries.user
  (:require [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]))

(defn resolve-account [context args v]
  (let [user (:user context)]
    (cond
      (contains? user :error) {:error (:error user)}
      (contains? user :email) (-> (iam/find-account-by-email (:email user)) first)
      :else {})))

(defn resolve-all-accounts [context args v]
  (let [user (:user context)]
    (iam/all-accounts)))

(def all-accounts
  {:type '(list :user)
   :resolve resolve-all-accounts})

(def account
  {:type :user
   :args {:account {:type 'String :default-value "cloudle"}}
   :resolve resolve-account})

(def greeting
  {:type 'String
   :args {:userType {:type :userType}}
   :resolve (constantly "world!")})

(def counter
  {:type 'String
   :resolve (constantly "1")})