(ns hive.store.field
  (:require [datomic.api :as d]
            [hive.core :refer [conn]]))

(defn add-field [name]
  @(d/transact
     conn [{:db/id (d/tempid :db.part/user)
            :field/name name
            :field/disabled false}]))

(defn find-field-id [field-name]
  (ffirst (d/q '[:find ?t
                 :in $ ?field-name
                 :where
                 [?t :field/name ?field-name]]
               (d/db conn)
               field-name)))

(defn find-fields-for-type [type-name]
  (d/q '[:find ?field-name
         :in $ ?type-name
         :where
         [?type-id :type/name ?type-name]
         [?type-id :type/fields ?field-id]
         [?field-id :field/name ?field-name]]
       (d/db conn)
       type-name))

(defn delete-field [field-id]
  @(d/transact conn [{:db/id field-id :field/disabled true}]))