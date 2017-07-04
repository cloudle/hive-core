 (ns hive.core
   (:require [datomic.api :as d]))

;; Database connection ----------------------------
; (def uri "datomic:ddb://ap-northeast-1/hive-core/hive-core")
(def uri "datomic:dev://localhost:4334/hive-core")
(def conn (d/connect uri))