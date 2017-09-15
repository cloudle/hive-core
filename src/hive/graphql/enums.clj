(ns hive.graphql.enums
  (:require [hive.graphql.primitives :refer :all]))

(def user-type
  {:description "User Type on the system"
   :values [:ADMIN :USER]})