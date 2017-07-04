(ns hive.graphql-test
  (:require [io.pedestal.test :refer :all]
            [expectations :refer :all]
            [flatland.ordered.map :refer [ordered-map]]
            [com.walmartlabs.lacinia :refer [execute]]
            [hive.service :as service]))

(expect {:data (ordered-map :greeting "world!")}
        (execute service/hive-schema "query { greeting }" nil nil))