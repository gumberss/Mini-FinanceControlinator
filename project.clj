(defproject mini-finance-controlinator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.7.10"]
                 [org.postgresql/postgresql "42.1.4"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.5.0"]
                 [compojure "1.6.1"]
                 [http-kit "2.3.0"]
                 [lynxeyes/dotenv "1.0.2"]
                 [org.clojure/data.json "2.4.0"]
                 [com.datomic/datomic-pro "0.9.5951"]]
  :main ^:skip-aot mini-finance-controlinator.handler
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all} :dev {:main mini-finance-controlinator.handler/-dev-main}})
