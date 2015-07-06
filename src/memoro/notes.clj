(ns memoro.notes
  (:require [memoro.database :as db]))

(def note-keys [:db/id :note/identificator :note/name :note/items])

(defn- uuid [] (java.util.UUID/randomUUID))

(defn- uuid-from-string [data]
  (java.util.UUID/fromString data))

(defn- map-keys [entity keys]
  (apply merge (map (fn [key] {key (str (key entity))}) keys)))

(defn get-notes []
  (map (fn [e] (map-keys e note-keys))
       (map db/read-entity (db/find-entities :note/identificator))))

(defn find-note [identificator]
  (db/find-entity [:note/identificator (uuid-from-string identificator)]))

(defn create-note []
  (let [identificator (uuid)]
    (db/persist-entity {:note/identificator identificator}) identificator))

(defn get-note [identificator]
  (map-keys (db/read-entity (find-note identificator)) note-keys))

(defn update-note [id {:keys [name items]}] 
;TODO let id = :id (map rest items ... ) #({:db/id (:id items) :item/
;TODO db/update-entity {:id id :items [{:item/id x :item/text x :item/order x :item/... x}]))
)
