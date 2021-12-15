(ns mini-finance-controlinator.feature.piggybank.piggy-bank-db)

(def db (atom {:piggy-banks {}}))

(defn existing? [{name :name}]
  (->> @db
       (:piggy-banks)
       vals
       (map :name)
       (some #(= % name))))

(defn create!
  [{
    _1    :name
    _2    :description
    _3    :saved-value
    :as   record}]
  (let [id (->  @db :piggy-banks  count inc)
        new-piggy-bank (assoc record :id id)]
  (swap! db update-in [:piggy-banks] assoc id new-piggy-bank)))

(defn update!
  [id update-piggy-bank-fn]
  (swap! db update-in [:piggy-banks id] update-piggy-bank-fn))

(defn get-all
  []
  (-> @db
      :piggy-banks
      vals))