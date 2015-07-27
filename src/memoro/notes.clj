(ns memoro.notes
  (:require [memoro.database :as db]))

(def note-keys [:db/id :note/identificator :note/name])

(def item-keys [:db/id :item/priority :item/checked? :item/text])

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
  (let [note (db/read-entity (find-note identificator))]
    (merge
     (map-keys note note-keys)
     {:note/items (map #(map-keys % item-keys) (:note/items note))})))

(defn get-items [note]
  (map (fn [item] (map-keys item item-keys)) (:note/items (db/read-entity (find-note note)))))

(defn add-item [{:keys [note priority checked? text]}]
  (do (println {:note note :text text})
      (db/persist-child {:id note :collection :note/items}
                        {:item/priority priority
                         :item/checked? checked?
                         :item/text text})))

(defn get-item [id]
  (map-keys (db/read-entity id) item-keys))
