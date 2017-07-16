(ns hive.schema.project
  (require [datomic.api :as d]))

(def project-name
  {:db/id (d/tempid :db.part/db)
   :db/ident :project/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "A project's name"
   :db.install/_attribute :db.part/db})

(def project-disabled
  {:db/id (d/tempid :db.part/db)
   :db/ident :project/disabled
   :db/valueType :db.type/boolean
   :db/cardinality :db.cardinality/one
   :db/doc "Project's disabled attribute - project is locked if this is true"
   :db.install/_attribute :db.part/db})

(def project-types
  {:db/id (d/tempid :db.part/db)
   :db/ident :project/types
   :db/valueType :db.type/ref
   :db/cardinality :db.cardinality/many
   :db/doc "All types of a Project"
   :db.install/_attribute :db.part/db})