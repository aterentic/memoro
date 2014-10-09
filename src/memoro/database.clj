(ns memoro.database
  (:require
   [clojure.string :as string :refer [split]]
   [clojure.walk :as walk]
   [datomic.api :as d :refer [q db]]))

(def uri "datomic:mem://memoro")

(defn- connect []
  (d/connect uri))

(defn- read-db []
  (d/db (connect)))

(defn- create-schema []
  (d/transact (connect) (load-file "resources/schema.clj")))

(defn- strip-namespace [kwd]
  (keyword (second (string/split (str kwd) #"/"))))

(defn- strip-namespaces [entity]
  (walk/postwalk
   (fn [value] (if (keyword? value) (strip-namespace value) value))
   entity))

(defn- read-entity [eid]
  (read-string (pr-str (d/touch (d/entity (read-db) eid)))))

(defn- user-id [{:keys [code]}]
  (first (first (d/q '[:find ?user_id :in $ ?code :where [?user_id :user/code ?code]] (read-db) code))))

(defn- board-id [{:keys [user name]}]
  (first (first (d/q '[:find ?board_id :in $ ?user ?name :where [?u :user/code ?user] [?u :user/boards ?board_id] [?board_id :board/name ?name]] (read-db) user name))))

(defn make-db []
  (d/create-database uri)
  (create-schema))

(defn delete-db []
  (d/delete-database uri))

(defn exists? [{:keys [code]}]
  (if (= code nil)
    nil
    (= 1 (count (d/q '[:find ?user-id :in $ ?code :where [?user-id :user/code ?code]] (read-db) code)))))

(defn add-user [{:keys [code]}]
  (d/transact (connect) [{:db/id (d/tempid :db.part/user) :user/code code}]))

(defn get-user [user]
  (strip-namespaces (read-entity (user-id user))))

(defn get-users []
  (map
   (fn [code] {:code (first code)})
   (q '[:find ?n :where [?c user/code ?n ]] (read-db))))

(defn get-board [board]
  (strip-namespaces (read-entity (board-id board))))

(defn get-note [id]
  (strip-namespaces (read-entity id)))

(defn add-board [{:keys [user name]}]
  (let [temp-id (d/tempid :db.part/user)
        user-id (user-id {:code user})]
    (d/transact (connect) [{:db/id temp-id :board/name name}
                           {:db/id user-id :user/boards temp-id}])))

(defn add-note [{:keys [user board text]}]
  (let [note-id (d/tempid :db.part/user)
        board-id (board-id {:user user :name board})]
    (d/resolve-tempid (read-db) (:tempids @(d/transact (connect) [{:db/id note-id :note/text text}
                                                                  {:db/id board-id :board/notes note-id}])) note-id)))
(defn add-note-item [note-id text]
  (let [note (d/entity (read-db) note-id)]
    (d/transact (connect) [{:db/id note-id :note/text text}])))
