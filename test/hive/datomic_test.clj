(ns hive.datomic-test
  (:require [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [expectations :refer :all]
            [datomic.api :as d]
            [hive.service :as service]
            [hive.core :refer :all]))

(defn create-empty-in-memory-db []
  (let [uri "datomic:mem://hive-core-test"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/schema.edn")]
      (d/transact conn schema)
      conn)))

(expect #{["John"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-pet-owner "John")
            (find-all-pet-owners))))