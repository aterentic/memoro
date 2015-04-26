(defproject memoro "0.1.0-SNAPSHOT"
  :description "Memoro, yet another TODO."
  :url "http://memoro.herokuapp.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/data.json "0.2.4"]
                 [environ "1.0.0"]
                 [compojure "1.1.6"]
                 [ring/ring-json "0.3.1"]
                 [liberator "0.12.2"]
                 [com.datomic/datomic-free "0.9.4699"]]
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "1.0.0"]]
  :ring { :handler memoro.routes/app }
  :profiles {:dev 
             { :env 
              { :nrepl? true :nrepl-port 8081 :nrepl-handler cider.nrepl/cider-nrepl-handler }
              :dependencies [[org.clojure/tools.nrepl "0.2.10"]
                                   [cider/cider-nrepl "0.8.2"]
                                   [javax.servlet/servlet-api "2.5"]
                                   [ring-mock "0.1.5"]
                                   [midje "1.6.3"]]
                    :plugins [[lein-midje "3.1.3"]]
                    :ring { :init memoro.nrepl/start
                            :port 8080 }}})
