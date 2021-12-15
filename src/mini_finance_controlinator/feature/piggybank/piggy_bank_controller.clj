(ns mini-finance-controlinator.feature.piggybank.piggy-bank-controller
  (:require [mini-finance-controlinator.feature.piggybank.piggy-bank-flow :as p.flow]
            [clojure.data.json :as json])
  )

(defn create-piggy-bank
  [req]
  (try
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (-> (req :params) (p.flow/create!))}
    (catch Exception e
      {:status  500                                         ; 500 or 400??
       :headers {"Content-Type" "application/json"}
       :body    {:message (ex-message e)}})
    )
  )

(defn get-all-piggy-banks
  [req]
  (try
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (-> (p.flow/get-all) json/write-str)}
    (catch Exception e
      {:status  500                                         ; 500 or 400??
       :headers {"Content-Type" "application/json"}
       :body    {:message (ex-message e)}})
    )
  )