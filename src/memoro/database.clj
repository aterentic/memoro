(ns memoro.database
  (:use
   [clojure.string :as string :only (split)]
   [clojure.walk :as walk]
   [datomic.api :only (q db) :as d]))

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

(defn- user-id [user]
  (first (first (d/q '[:find ?user_id :in $ ?code :where [?user_id :user/code ?code]] (read-db) (:code user)))))

(defn- board-id [board]
       (first (first (d/q '[:find ?board_id :in $ ?user ?name :where [?u :user/code ?user] [?u :user/boards ?board_id] [?board_id :board/name ?name]] (read-db) (:user board) (:name board)))))

(defn make-db []
  (d/create-database uri)
  (create-schema))

(defn delete-db []
  (d/delete-database uri))

(defn add-user [user]
  (d/transact (connect) [{:db/id (d/tempid :db.part/user) :user/code (:code user)}]))

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

(defn add-board [board]
  (let [tx @(d/transact (connect) [{:db/id (d/tempid :db.part/user) :board/name (:name board)}])
        id (d/resolve-tempid (read-db) (:tempids tx) (ffirst (:tempids tx)))]
    (d/transact (connect) [{:db/id (user-id {:code (:user board)}) :user/boards id}])))

(defn add-note [note]
  (let [tx @(d/transact (connect) [{:db/id (d/tempid :db.part/user) :note/text (:text note)}])
        id (d/resolve-tempid (read-db) (:tempids tx) (ffirst (:tempids tx)))]
    (d/transact (connect) [{:db/id (board-id {:user (:user note) :name (:board note)}) :board/notes id}]) id))
