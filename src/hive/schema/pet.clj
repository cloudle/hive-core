(ns hive.schema.pet
  (require [datomic.api :as d]))

(def owner-name
  {:db/id (d/tempid :db.part/db)
   :db/ident :owner/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "A owner's name"
   :db.install/_attribute :db.part/db})

(def pet-name
  {:db/id (d/tempid :db.part/db)
   :db/ident :pet/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "A pet's name"
   :db.install/_attribute :db.part/db})

(def owner-pets
  {:db/id (d/tempid :db.part/db)
   :db/ident :owner/pets
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db/doc "All the pet for one onwer"
   :db.install/_attribute :db.part/db})