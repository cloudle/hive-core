(ns hive.pet-store-test
  (:require [expectations :refer :all]
            [datomic.api :as d]
            [hive.core :refer [conn]]
            [hive.store.pet :refer :all]))

(defn create-empty-in-memory-db []
  (let [uri "datomic:mem://hive-core-test"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/schema.edn")]
      (d/transact conn schema)
      conn)))

;; Adding single pet-owner and be able to find them
(expect #{["John"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-pet-owner "John")
            (find-all-pet-owners))))

;; Adding multiple pet-owner should work, we could find them all
(expect #{["John"] ["Paul"] ["George"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-pet-owner "John")
            (add-pet-owner "Paul")
            (add-pet-owner "George")
            (find-all-pet-owners))))

;; Adding one owner with one pet should allow us to find that pet for that owner
(expect #{["Salt"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-pet-owner "John")
            (add-pet "Salt" "John")
            (find-pets-for-owner "John"))))

;; Adding multiple owners and pets should allow us to find the pets for a particular owner
(expect #{["Martha"] ["Jet"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-pet-owner "John")
            (add-pet "Salt" "John")
            (add-pet "Pepper" "John")
            (add-pet-owner "Paul")
            (add-pet "Martha" "Paul")
            (add-pet "Jet" "Paul")
            (find-pets-for-owner "Paul"))))