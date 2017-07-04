(ns hive.project-store-test
  (:require [expectations :refer :all]
            [datomic.api :as d]
            [hive.core :refer [conn]]
            [hive.store.project :refer :all]))

;; Adding Project for an user-account and be able to find it
(expect #{["Project name"]}
        (add-project-using-account "Project name" "cloudle"))

;; Adding Project using account and find it using that account
(expect #{["Gundam"]}
        (do
          (add-project-using-account "Gundam" "cloudle")
          (find-projects-for-account "cloudle")))

;; Add multiple project and find them using account
(expect #{["Gundam"] ["Code Geass"] ["Hunter x Hunter"]}
        (do
          (add-project-using-account "Gundam" "cloudle")
          (add-project-using-account "Code Geass" "cloudle")
          (add-project-using-account "Hunter x Hunter" "cloudle")
          (find-projects-for-account "cloudle")))