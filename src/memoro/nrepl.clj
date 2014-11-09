(ns memoro.nrepl
  (:require [clojure.tools.nrepl.server :refer [start-server default-handler]]
            [lighttable.nrepl.handler :refer [lighttable-ops]]))

(defn start []
  (defonce nrepl-server (start-server :port 8081 :handler (default-handler (var lighttable-ops)))))
