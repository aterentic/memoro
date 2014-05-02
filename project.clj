(defproject memoro "0.1.0-SNAPSHOT"
  :description "FIXME: write memoro description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/data.json "0.2.4"]
                 [compojure "1.1.6"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [com.datomic/datomic-free "0.9.4699"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler memoro.routes/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [midje "1.6.3"]]
         :plugins [[lein-midje "3.1.3"]]}})
