(ns hive.store.project
  (:require [datomic.api :as d]
            [hive.core :refer [conn]]
            [hive.store.iam :refer :all]))

(defn all-projects []
  (d/q '[:find [(pull ?u [*]) ...]
         :where
         [?u :project/name]
         [?u :project/disabled false]]
       (d/db conn)))

(defn add-project-using-account [project-name account]
  (let [project-id (d/tempid :db.part/user)]
    @(d/transact
       conn [{:db/id project-id
              :project/disabled false
              :project/name project-name}
             {:db/id (find-account-id account)
              :user/projects project-id}])))

(defn find-projects-using-account [user-account]
  (d/q '[:find [(pull ?project-id [*]) ...]
         :in $ ?account
         :where
         [?user-id :user/account ?account]
         [?user-id :user/projects ?project-id]
         [?project-id :project/disabled false]]
       (d/db conn)
       user-account))

(defn find-project-id [project-name]
  (ffirst (d/q '[:find ?p
                 :in $ ?project-name
                 :where [?p :project/name ?project-name]]
               (d/db conn)
               project-name)))

(defn find-project [project-name]
  (d/q '[:find [(pull ?a [*])]
         :in $ ?project-name
         :where [?a :project/name ?project-name]]
       (d/db conn)
       project-name))

(defn delete-project [project-id]
  @(d/transact conn [{:db/id project-id :project/disabled true}]))