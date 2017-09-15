(ns hive.graphql.schema
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]
            [hive.graphql.types :as types]
            [hive.graphql.enums :as enums]
            [hive.graphql.queries.user :as user-queries]
            [hive.graphql.mutations.user :as user-mutations]
            [hive.graphql.subscriptions.user :as user-subscriptions]))

(def hive-schema
  (schema/compile
    {:objects
     {:user types/user}
     :enums
     {:userType enums/user-type}
     :queries
     {:account user-queries/account
      :greeting user-queries/greeting
      :counter user-queries/counter}
     :mutations
     {:increaseCounter user-mutations/increase-counter}
     :subscriptions
     {:logs user-subscriptions/logs}}))