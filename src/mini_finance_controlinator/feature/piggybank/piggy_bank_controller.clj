(ns mini-finance-controlinator.feature.piggybank.piggy-bank-controller
  (:require [mini-finance-controlinator.feature.piggybank.piggy-bank-flow :as p.flow]
            [clojure.data.json :as json])
  )

(defn create-piggy-bank
  [req]
  (try
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (-> (p.flow/create! (req :params)))}
    (catch Exception e
      {:status  500                                         ; 500 or 400??
       :headers {"Content-Type" "application/json"}
       :body    (-> (ex-message e))})
    )
  )

(defn get-all-piggy-banks
  [req]
  (try
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str (p.flow/get-all))}
    (catch Exception e
      {:status  500                                         ; 500 or 400??
       :headers {"Content-Type" "application/json"}
       :body    (-> (ex-message e))})
    )
  )