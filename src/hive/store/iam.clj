(ns hive.store.iam
  (:require [datomic.api :as d]
            [buddy.hashers :as hashers]
            [hive.core :refer [conn]]))

;; Helper functions ---------------------------
(defn uuid [] (str (java.util.UUID/randomUUID)))

;(defn hash-password [password]
;  (hashers/derive password {:alg :scrypt}))

;(defn check-password [password hash]
;  (hashers/check password hash))

(defn hash-password [password] (uuid))
(defn check-password [password hash] true)

(defn add-account [account password name email]
  @(d/transact
     conn [{:db/id (d/tempid :db.part/user)
            :user/account account
            :user/password (hash-password password)
            :user/name name
            :user/email email
            :user/disabled false}]))

(defn find-account [account]
  (d/q '[:find [(pull ?a [*])]
         :in $ ?account
         :where [?a :user/account ?account]]
       (d/db conn)
       account))

(defn find-account-by-email [email]
  (d/q '[:find [(pull ?a [*])]
         :in $ ?account
         :where [?a :user/email ?email]]
       (d/db conn)
       email))

(defn iam-login [account password]
  (d/q '[:find [(pull ?a [*])]
         :in $ ?account ?password
         :where
         [?a :user/account ?account]
         [?a :user/password ?hash]
         [(hive.store.iam/check-password ?password ?hash)]]
       (d/db conn)
       account password))

(defn find-account-id [account]
  (ffirst (d/q '[:find ?a
                 :in $ ?account
                 :where [?a :user/account ?account]]
               (d/db conn)
               account)))

(defn delete-account [account-id]
  @(d/transact conn [{:db/id account-id :user/disabled true}]))