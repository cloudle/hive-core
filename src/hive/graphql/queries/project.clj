(ns hive.graphql.queries.project
  (:require [hive.graphql.primitives :refer :all]
            [hive.store.project :as project]))

(defn resolve-project [context args v]
  (let [user (:user context)]
    (project/find-project (:name args))))

(defn resolve-all-projects [context args v]
  (let [user (:user context)]
    (cond
      (contains? user :email) (project/find-projects-using-account (:email user))
      :default [])))

(def project
  {:type :project
   :args {:name {:type 'String}}
   :resolve resolve-project})

(def all-projects
  {:type '(list :project)
   :resolve resolve-all-projects})