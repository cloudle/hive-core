(ns hive.graphql.mutations.user
  (:require [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]))

(def increase-counter
  {:type 'String
   :resolve (fn [context args v] "2")})