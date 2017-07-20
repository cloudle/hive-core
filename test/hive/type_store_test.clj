(ns hive.type-store-test
  (:require [expectations :refer :all]
            [datomic.api :as d]
            [hive.core :refer :all]
            [hive.store.iam :refer :all]
            [hive.store.project :refer :all]
            [hive.store.type :refer :all]))

;; Adding single pet-owner and be able to find them
;(expect #{["Article"]}
;        (with-redefs [conn (create-empty-in-memory-db)]
;          (do (add-account "cloudle" "12345" "Cloud Le" "lehaoson@gmail.com")
;              (add-project-using-account "My project" "cloudle")
;               )))