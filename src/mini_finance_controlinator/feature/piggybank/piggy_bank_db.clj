(ns mini-finance-controlinator.feature.piggybank.piggy-bank-db
  (:require [datomic.api :as d])
  (:use clojure.pprint))

(defn uuid [] (java.util.UUID/randomUUID))

(def default-db-uri "datomic:dev://localhost:4334/finance-controlinator")

(defn- delete-database []
  (d/delete-database default-db-uri))

(defn- open-connection [db-uri]
  (d/create-database db-uri)
  (d/connect db-uri))

(defn connection
  ([] (open-connection default-db-uri))
  ([db-uri] (open-connection db-uri)))

(defn db-datomic
  ([] (d/db (connection default-db-uri)))
  ([conn] (d/db conn))
  ([conn time] (d/as-of (d/db conn) time)))

(defn existing?
  ([db {name :piggy-bank/name}]

   (let [piggy-banks-with-same-name (d/q '[:find ?e
                                           :in $ ?name
                                           :where [?e :piggy-bank/name ?name]]
                                         db name)]
     (some number? (reduce concat piggy-banks-with-same-name)))))

(defn create! [conn entity]
  (pprint @(d/transact conn [(assoc entity :piggy-bank/id (uuid))])))

(defn update!
  [id update-piggy-bank-fn]
  (swap! db update-in [:piggy-banks id] update-piggy-bank-fn))

(defn get-all
  ([]
   (get-all (db-datomic)))
  ([db]
   (let [result (d/q '[:find (pull ?e [*])
                       :where [?e :piggy-bank/id]] db)]
     (first result))))

(def schema [
             {
              :db/ident       :piggy-bank/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              }
             {
              :db/ident       :piggy-bank/description
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              }
             {
              :db/ident       :piggy-bank/saved-value
              :db/valueType   :db.type/double
              :db/cardinality :db.cardinality/one
              }
             {
              :db/ident       :piggy-bank/goal-value
              :db/valueType   :db.type/double
              :db/cardinality :db.cardinality/one
              }
             {
              :db/ident       :piggy-bank/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              }])

(defn create-schema [conn]
  (d/transact conn schema))