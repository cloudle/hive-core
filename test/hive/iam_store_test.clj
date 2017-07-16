(ns hive.iam-store-test
  (:require [expectations :refer :all]
            [datomic.api :as d]
            [hive.core :refer :all]
            [hive.store.project :refer :all]
            [hive.store.iam :refer :all]))

;; Add account and find it
(expect "Cloud Le"
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "Cloud Le" "lehaoson@gmail.com")
              (-> (find-account "cloudle") first :user/name))))

;; Hash password function consistently transform it's source to another one
;; Single hash source must derive variety hashes instead of 1
;; Only correct way to verify password is to use check
(expect true
        (let [hashed-password (hash-password "12345")]
          (and (not= (hash-password "12345") "12345")
               (not= (hash-password "12345") (hash-password "12345"))
               (= true (check-password "12345" hashed-password)))))

;; Login using account and password
(expect "Cloud Le"
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "Cloud Le" "lehaoson@gmail.com")
              (-> (iam-login "cloudle" "12345") first :user/name))))

; Delete account just toggle it's :user/disabled to true
(expect true
        (with-redefs [conn (create-empty-in-memory-db)]
          (do (add-account "cloudle" "12345" "CLoud Le" "lehaoson@gmail.com")
              (add-project-using-account "Code Geass" "cloudle")
              (let [account-id ( -> (find-account "cloudle") first :db/id)]
                (delete-account account-id))
              (-> (find-account "cloudle") first :user/disabled))))