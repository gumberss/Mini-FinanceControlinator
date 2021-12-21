(ns mini-finance-controlinator.feature.piggybank.piggy-bank-db
  (:require [datomic.api :as d]
            [clj-time.coerce :as tc])
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
  ([conn date] (d/as-of (d/db conn) date)))

(defn find-by-name [db name]
  (d/q '[:find ?e
         :in $ ?name
         :where [?e :piggy-bank/name ?name]]
       db name))

(defn existing?
  ([db {name :piggy-bank/name}]
   (let [piggy-banks-with-same-name (find-by-name db name)]
     (some number? (reduce concat piggy-banks-with-same-name)))))

(defn create! [conn entity]
  (:tempids @(d/transact conn [(assoc entity :piggy-bank/id (uuid))])))

; its inserting data without changes?
(defn update!
  [piggy-bank]
  (let [
        db-piggy-bank (find-by-name (db-datomic) (:piggy-bank/name piggy-bank))
        piggy-bank-id (ffirst db-piggy-bank)
        result @(d/transact (connection) (map #(into [:db/add piggy-bank-id] %) piggy-bank))]
    (:tempids result)))

(defn get-all
  ([]
   (get-all (db-datomic)))
  ([db]
   (let [result (d/q '[:find (pull ?e [*])
                       :where [?e :piggy-bank/id]]
                     db)]
     (map first result))))

(defn get-all-by-date
  [string-date]
  (let [
        date (clojure.instant/read-instant-date string-date)
        db (d/since (db-datomic (connection)) date)
        result (get-all db)]
    result))

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