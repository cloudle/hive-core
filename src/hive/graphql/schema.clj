(ns hive.graphql.schema
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [hive.graphql.primitives :refer :all]
            [hive.store.iam :as iam]
            [hive.graphql.types :as types]
            [hive.graphql.enums :as enums]
            [hive.graphql.queries.user :as user-queries]
            [hive.graphql.queries.project :as project-query]
            [hive.graphql.queries.schema-type :as schema-type-query]
            [hive.graphql.mutations.user :as user-mutations]
            [hive.graphql.subscriptions.user :as user-subscriptions]))

(def hive-schema
  (schema/compile
    {:objects
     {:user types/user
      :project types/project
      :schemaType types/schema-type}
     :enums
     {:userType enums/user-type}
     :queries
     {:account user-queries/account
      :accounts user-queries/all-accounts
      :greeting user-queries/greeting
      :counter user-queries/counter
      :project project-query/project
      :projects project-query/all-projects
      :schemaTypes schema-type-query/all-schema-types}
     :mutations
     {:increaseCounter user-mutations/increase-counter}
     :subscriptions
     {:logs user-subscriptions/logs}}))