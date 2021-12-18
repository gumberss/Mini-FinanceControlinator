(ns mini-finance-controlinator.feature.piggybank.piggy-bank-db
  (:require [datomic.api :as d])
  (:use clojure.pprint))

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


(def db (atom {:piggy-banks {}}))

(defn existing? [{name :name}]
  (->> @db
       (:piggy-banks)
       vals
       (map :name)
       (some #(= % name))))

(defn create!
  [{
    _1  :name
    _2  :description
    _3  :saved-value
    :as record}]
  (let [id (-> @db :piggy-banks count inc)
        new-piggy-bank (assoc record :id id)]
    (swap! db update-in [:piggy-banks] assoc id new-piggy-bank)))

(defn create! [conn entity]
  (pprint @(d/transact conn [entity])))

(defn update!
  [id update-piggy-bank-fn]
  (swap! db update-in [:piggy-banks id] update-piggy-bank-fn))

(defn get-all
  []
  (-> @db
      :piggy-banks
      vals))


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