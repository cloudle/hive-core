(ns hive.account-store-test
  (:require [expectations :refer :all]
            [datomic.api :as d]
            [hive.core :refer [conn]]
            [hive.store.project :refer :all]
            [hive.store.account :refer :all]))

;; Add account and find it
(expect #{["cloudle"]}
        (do
          (add-account "cloudle")
          (find-account "cloudle")))

;; Delete account also delete it child projects
(expect #{}
        (do
          (add-account "cloudle")
          (add-project-using-account "Code Geass" "cloudle")
          (delete-account "cloudle")
          (find-project "Code Geass")))