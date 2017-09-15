(ns hive.graphql.subscriptions.user
  (:require [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]))

(def logs
  {:type 'String
   :stream (constantly "world!")})