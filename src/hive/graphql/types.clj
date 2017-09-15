(ns hive.graphql.types
  (:require [hive.graphql.primitives :refer :all]))

(def user
  {:description "User account, could be internal user or an end-user with account"
   :fields {:id datomic-id
            :name datomic-string
            :email datomic-string
            :account datomic-string
            :error {:type 'String}}})

(def project
  {:description "Project, which containing it's own schema Types, data instances, configs, billings.."
   :fields {:id datomic-id
            :name datomic-string
            :disabled datomic-boolean}})