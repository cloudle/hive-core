(ns hive.graphql.primitives)

(defn resolve-datomic-id
  [context args instance]
  (:db/id instance))

(defn resolve-datomic-field
  [context args instance]
  (let [selection (:com.walmartlabs.lacinia/selection context)
        field-definition (:field-definition selection)
        field-name (:field-name field-definition)
        type-name (:type-name field-definition)
        key (keyword (name type-name) (name field-name))]
    (key instance)))

(def datomic-id {:type 'String :resolve resolve-datomic-id})
(def datomic-string {:type 'String :resolve resolve-datomic-field})
(def datomic-boolean {:type 'Boolean :resolve resolve-datomic-field})