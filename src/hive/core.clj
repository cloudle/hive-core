 (ns hive.core
   (:require [datomic.api :as d]
             [hive.schema.pet :refer :all]
             [hive.schema.user :refer :all]
             [hive.schema.project :refer :all]
             [hive.schema.type :refer :all]
             [hive.schema.field :refer :all]
             [hive.schema.enum :as enum]
             [hive.utils.firebase.app :refer [init-once!]]))

;; Database connection ----------------------------
; (def uri "datomic:ddb://ap-northeast-1/hive-core/hive-core")
(def uri "datomic:dev://localhost:4334/hive-core")
(def conn (d/connect uri))

(def firebase-core (init-once! "resources/hive-core-secret.json" "https://dxg-crm.firebaseio.com"))

(defn make-idents [x] (mapv #(hash-map :db/ident %) x))

(def schema
  [owner-name
   owner-pets
   pet-name
   ;; User ------------
   user-account
   user-password
   user-disabled
   user-email
   user-name
   user-projects
   ;; Project ---------
   project-name
   project-disabled
   project-types
   ;; Type ------------
   type-name
   type-disabled
   type-fields
   ;; Field -----------
   field-name
   field-disabled
   field-type
   ])

@(d/transact conn schema)

(defn create-empty-in-memory-db []
  (let [uri "datomic:mem://hive-core-test"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)]
      (d/transact conn schema)
      conn)))