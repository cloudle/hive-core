 (ns hive.core
   (:require [datomic.api :as d]))

 ;(def conn (d/connect "datomic:mem://hive-core"))
 (def conn nil)

 (defn add-pet-owner [owner-name]
   @(d/transact conn [{:db/id (d/tempid :db.part/user)
                       :owner/name owner-name}]))

 (defn find-all-pet-owners []
   (d/q '[:find ?owner-name
          :where [_ :owner/name ?owner-name]]
        (d/db conn)))