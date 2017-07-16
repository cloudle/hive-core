(ns hive.store.type
  (:require [datomic.api :as d]
            [hive.core :refer [conn]]))

(defn add-type [project-id type-name]
  (let [type-id (d/tempid :db.part/user)]
    @(d/transact
       conn [{:db/id type-id
              :type/name name
              :type/disabled false}
             {:db/id project-id
              :project/types type-id}])))

(defn find-type [project-id type-name]
  (d/q '[:find [(pull ?t [*])]
         :in $ ?project-id ?type-name
         :where
         [?t :type/name ?type-name]
         [?project-id :project/types ?t]]
       (d/db conn)
       project-id type-name))

(defn find-type-id [type-name]
  (ffirst (d/q '[:find ?t
                 :in $ ?type-name
                 :where
                 [?t :type/name ?type-name]]
               (d/db conn)
               type-name)))

(defn find-types-for-project [project-name]
  (d/q '[:find ?type-name
         :in $ ?project-name
         :where
         [?project-id :project/name ?project-name]
         [?project-id :project/types ?type-id]
         [?type-id :type/name ?type-name]]
       (d/db conn)
       project-name))

(defn delete-type [type-id]
  @(d/transact conn [{:db/id type-id :type/disabled true}]))