(ns memoro.users
  (:require [clojure.math.numeric-tower :as math]))

(defn generate-code [] (math/round (* (rand) 100000)))

(defn create-user []
  { :code (str (generate-code))})
