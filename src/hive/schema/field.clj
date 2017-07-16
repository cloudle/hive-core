(ns hive.schema.field
  (require [datomic.api :as d]))

(def field-name
  {:db/id (d/tempid :db.part/db)
   :db/ident :field/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "GraphQL Field's name"
   :db.install/_attribute :db.part/db})

(def field-disabled
  {:db/id (d/tempid :db.part/db)
   :db/ident :field/disabled
   :db/valueType :db.type/boolean
   :db/cardinality :db.cardinality/one
   :db/doc "Field's disabled attribute - field is locked if this is true"
   :db.install/_attribute :db.part/db})

(def field-type
  {:db/id (d/tempid :db.part/db)
   :db/ident :field/type
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/one
   :db/doc "Enum of a Field's Type"
   :db.install/_attribute :db.part/db})