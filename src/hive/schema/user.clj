(ns hive.schema.user
  (require [datomic.api :as d]))

(def user-account
  {:db/id (d/tempid :db.part/db)
   :db/ident :user/account
   :db/valueType :db.type/string
   :db/unique :db.unique/identity
   :db/cardinality :db.cardinality/one
   :db/doc "User's account for authentication"
   :db.install/_attribute :db.part/db})

(def user-password
  {:db/id (d/tempid :db.part/db)
   :db/ident :user/password
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "User's password for authentication"
   :db.install/_attribute :db.part/db})

(def user-disabled
  {:db/id (d/tempid :db.part/db)
   :db/ident :user/disabled
   :db/valueType :db.type/boolean
   :db/cardinality :db.cardinality/one
   :db/doc "User's disabled attribute - account is locked if this is true"
   :db.install/_attribute :db.part/db})

(def user-name
  {:db/id (d/tempid :db.part/db)
   :db/ident :user/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "User's name"
   :db.install/_attribute :db.part/db})

(def user-email
  {:db/id (d/tempid :db.part/db)
   :db/ident :user/email
   :db/valueType :db.type/string
   :db/unique :db.unique/identity
   :db/cardinality :db.cardinality/one
   :db/doc "User's email"
   :db.install/_attribute :db.part/db})

(def user-projects
  {:db/id (d/tempid :db.part/db)
   :db/ident :user/projects
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db/doc "All projects of a User"
   :db.install/_attribute :db.part/db})