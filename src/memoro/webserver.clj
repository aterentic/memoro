(ns memoro.webserver
  (:use [compojure.core]
        [ring.adapter.jetty]
        [memoro.routes :as routes]))

(defonce server
    (run-jetty (var routes/app) {:port 8080 :join? false}))

(defn start[]
  (.start server))

(defn stop []
  (.stop server))
