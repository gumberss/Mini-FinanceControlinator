(ns mini-finance-controlinator.feature.piggybank.piggy-bank-flow
  (:require [mini-finance-controlinator.feature.piggybank.piggy-bank-db :as p.db])
  )

(defn create! [piggy-bank]
  (if
    (p.db/existing? piggy-bank)
    (throw (ex-info "Piggy banks already exist" {:piggy-bank piggy-bank}))
    (p.db/create! piggy-bank)))

(defn get-all
  []
  (p.db/get-all))