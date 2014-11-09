(ns memoro.nrepl
  (:require [environ.core :refer [env]]
            [clojure.tools.nrepl.server :refer [start-server default-handler]]))

(defonce defaults
  (load-file "resources/defaults.clj"))

(defonce nrepl?
  (or (env :nrepl?) (defaults :nrepl?)))

(defn- handler [middleware]
  (if middleware
    (do
      (require [(read-string (namespace middleware))])
      (default-handler (resolve middleware)))
    default-handler))

(defn start []
  (if nrepl?
    (defonce nrepl-server
      (let [nrepl-port (or (env :nrepl-port) (defaults :nrepl-port))
            nrepl-middleware (or (env :nrepl-middleware) (defaults :nrepl-middleware))]
        (start-server :port nrepl-port :handler (handler nrepl-middleware))))))
