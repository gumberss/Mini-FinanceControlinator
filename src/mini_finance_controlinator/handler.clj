(ns mini-finance-controlinator.handler
  (:require
    [dotenv :refer [env app-env]]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [org.httpkit.server :as server]
    [ring.middleware.json :as js]
    [ring.middleware.defaults :refer :all]
    [ring.middleware.reload :refer [wrap-reload]]
    [clojure.pprint :as pp]
    [clojure.string :as str]
    [clojure.data.json :as json]
    [mini-finance-controlinator.feature.piggybank.piggy-bank-controller :as p.controller]
    [mini-finance-controlinator.feature.piggybank.piggy-bank-db :as p.db]
    )
  (:gen-class))

(defroutes app-routes
           (GET "/" [req] (str req))
           (GET "/piggy-bank" [req] p.controller/get-all-piggy-banks)
           (POST "/piggy-bank" [] p.controller/create-piggy-bank))

(defn -main
  "Production"
  [& args]
  (let [port 5009]
    (server/run-server (js/wrap-json-params (js/wrap-json-response (wrap-defaults #'app-routes api-defaults))) {:port port})
    (p.db/create-schema (p.db/connection))
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))

(defn -dev-main
  "Dev/Test (auto reload watch enabled)"
  [& args]
  (let [port 5008]
    (server/run-server (wrap-reload (js/wrap-json-params (js/wrap-json-response (wrap-defaults #'app-routes api-defaults)))) {:port port})

    (p.db/create-schema (p.db/connection))
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))