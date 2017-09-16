(ns hive.graphql.mutations.project
  (:require [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]))

(def add-project
  {:type :project
   :resolve (fn [context args v] "2")})