(ns hive.project-store-test
  (:require [expectations :refer :all]
            [datomic.api :as d]
            [hive.core :refer :all]
            [hive.store.iam :refer :all]
            [hive.store.project :refer :all]))

;; Adding Project for an user-account and be able to find it
(expect "Project name"
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "Cloud Le" "lehaoson@gmail.com")
              (add-project-using-account "Project name" "cloudle")
              (-> (find-project "Project name") first :project/name))))

;; Adding Project using account and find it using that account
(expect #{["Gundam"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "Cloud Le" "lehaoson@gmail.com")
              (add-project-using-account "Gundam" "cloudle")
              (find-projects-for-account "cloudle"))))

;; Add multiple project and find them using account
(expect #{["Gundam"] ["Code Geass"] ["Hunter x Hunter"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "Cloud Le" "lehaoson@gmail.com")
              (add-project-using-account "Gundam" "cloudle")
              (add-project-using-account "Code Geass" "cloudle")
              (add-project-using-account "Hunter x Hunter" "cloudle")
              (find-projects-for-account "cloudle"))))

; Delete project just toggle it's :project/disabled to true
(expect true
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "CLoud Le" "lehaoson@gmail.com")
              (add-project-using-account "Code Geass" "cloudle")
              (delete-project (find-project-id "Code Geass"))
              (-> (find-project "Code Geass") first :project/disabled))))