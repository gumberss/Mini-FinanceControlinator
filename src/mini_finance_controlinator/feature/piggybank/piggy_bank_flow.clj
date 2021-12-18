(ns mini-finance-controlinator.feature.piggybank.piggy-bank-flow
  (:require
    [mini-finance-controlinator.feature.piggybank.piggy-bank-db :as p.db])
  (:use clojure.pprint)
  )

(defn key-string->keyword [[key value]]
  {(keyword key) value})

(defn create! [data]
  (let [piggy-bank (into {} (map key-string->keyword data))]
    (if (p.db/existing? piggy-bank)
      (throw (ex-info "Piggy bank already exist" {:piggy-bank piggy-bank}))
      (p.db/create! (p.db/connection) piggy-bank))))

(defn get-all
  []
  (p.db/get-all))