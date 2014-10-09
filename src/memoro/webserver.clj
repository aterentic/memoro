(ns memoro.webserver
  (:require
   [ring.adapter.jetty :refer :all]
   [compojure.core :refer :all]
   [memoro.routes :as routes]))

(defonce server
    (run-jetty (var routes/app) {:port 8080 :join? false}))

(defn start[]
  (.start server))

(defn stop []
  (.stop server))
