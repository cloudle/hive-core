(ns hive.store.type
  (:require [datomic.api :as d]
            [hive.core :refer [conn]]
            [hive.store.project :refer :all]))

(defn add-type [account project-name type-name]
  (let [type-id (d/tempid :db.part/user)]
    @(d/transact
       conn [{:db/id type-id
              :schemaType/name type-name
              :schemaType/disabled false}
             {:db/id (find-project-id project-name)
              :project/types type-id}])))

(defn find-type [project-id type-name]
  (d/q '[:find [(pull ?t [*])]
         :in $ ?project-id ?type-name
         :where
         [?t :schemaType/name ?type-name]
         [?project-id :project/types ?t]]
       (d/db conn)
       project-id type-name))

(defn find-type-id [type-name]
  (ffirst (d/q '[:find ?t
                 :in $ ?type-name
                 :where
                 [?t :schemaType/name ?type-name]]
               (d/db conn)
               type-name)))

(defn find-types-using-project [account project-name]
  (d/q '[:find [(pull ?type-id [*]) ...]
         :in $ ?account ?project-name
         :where
         [?u :user/account ?account]
         ;;[?u :user/projects ?project-id] ;;TODO: Check for owner relationship..
         [?project-id :project/name ?project-name]
         [?project-id :project/disabled false]
         [?project-id :project/types ?type-id]
         [?type-id :schemaType/disabled false]]
       (d/db conn)
       account project-name))

(defn delete-type [type-id]
  @(d/transact conn [{:db/id type-id :schemaType/disabled true}]))