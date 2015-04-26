(ns memoro.nrepl
  (:require [environ.core :refer [env]]
            [clojure.tools.nrepl.server :refer [start-server default-handler]]))

(defonce defaults
  (load-file "resources/defaults.clj"))

(defonce nrepl?
  (or (env :nrepl?) (defaults :nrepl?)))

(defn- resolve-handler [handler]
  (if handler
    (do
      (require [(read-string (namespace handler))])
      (resolve handler))
    (default-handler)))

(defn start []
  (if nrepl?
    (defonce nrepl-server
      (let [nrepl-port (or (env :nrepl-port) (defaults :nrepl-port))
            nrepl-handler (or (env :nrepl-handler) (defaults :nrepl-handler))]
        (start-server :port nrepl-port :handler (resolve-handler nrepl-handler))))))
