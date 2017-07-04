(ns hive.graphql-test
  (:require [io.pedestal.test :refer :all]
            [expectations :refer :all]
            [flatland.ordered.map :refer [ordered-map]]
            [com.walmartlabs.lacinia :refer [execute]]
            [hive.graphql.schema :refer [hive-schema]]))

(expect {:data (ordered-map :greeting "world!")}
        (execute hive-schema "query { greeting }" nil nil))