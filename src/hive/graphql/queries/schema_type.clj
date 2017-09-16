(ns hive.graphql.queries.schema-type
  (:require [hive.graphql.primitives :refer :all]
            [hive.store.type :as type]))

(defn resolve-all-schema-types [context args v]
  (let [user (:user context)]
    (type/find-types-using-project (:email user) (:projectName args))))

(def all-schema-types
  {:type '(list :schemaType)
   :args {:projectName {:type '(non-null String)}}
   :resolve resolve-all-schema-types})