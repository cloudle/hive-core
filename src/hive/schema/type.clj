(ns hive.schema.type
  (require [datomic.api :as d]))

(def type-name
  {:db/id (d/tempid :db.part/db)
   :db/ident :schemaType/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "GraphQL Type's name"
   :db.install/_attribute :db.part/db})

(def type-disabled
  {:db/id (d/tempid :db.part/db)
   :db/ident :schemaType/disabled
   :db/valueType :db.type/boolean
   :db/cardinality :db.cardinality/one
   :db/doc "Type's disabled attribute - type is locked if this is true"
   :db.install/_attribute :db.part/db})

(def type-fields
  {:db/id (d/tempid :db.part/db)
   :db/ident :schemaType/fields
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db/doc "All fields of a GraphQL Type"
   :db.install/_attribute :db.part/db})
