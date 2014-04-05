(ns memoro.database
  (:use [datomic.api :only (q db) :as d]))

(def uri "datomic:mem://memoro")

(defn- connect []
  (d/connect uri))

(defn- read-db []
  (d/db (connect)))

(defn- create-schema []
  (d/transact (connect) [{:db/id (d/tempid :db.part/db)
                          :db/ident :user/code
                          :db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/doc "A user's code"
                          :db.install/_attribute :db.part/db}
                         {:db/id (d/tempid :db.part/db)
                          :db/ident :user/boards
                          :db/valueType :db.type/ref
                          :db/cardinality :db.cardinality/many
                          :db/doc "A users's boards"
                          :db.install/_attribute :db.part/db}
                         {:db/id (d/tempid :db.part/db)
                          :db/ident :board/name
                          :db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/doc "Board name"
                          :db.install/_attribute :db.part/db}]))

(defn- user-id [user]
  (first (first (d/q '[:find ?user_id :in $ ?code :where [?user_id :user/code ?code]] (read-db) (:code user)))))

(defn- parse-boards [user]
  (let [boards (:user/boards user)]
    (map (fn [board] {:name (:board/name board)}) boards)))

(defn make-db []
  (d/create-database uri)
  (create-schema))

(defn delete-db []
  (d/delete-database uri))

(defn add-user [user]
  (d/transact (connect) [{:db/id (d/tempid :db.part/user) :user/code (:code user)}]))

(defn get-users []
  (map
   (fn [code] {:code (first code)})
   (q '[:find ?n :where [?c user/code ?n ]] (read-db))))

(defn get-user [user]
  (let [e (d/entity (read-db) (user-id user))]
    { :code (:user/code e) :boards (parse-boards e)}))

(defn add-board [board]
  (let [tx @(d/transact (connect) [{:db/id (d/tempid :db.part/user) :board/name (:name board)}])
        id (d/resolve-tempid (read-db) (:tempids tx) (ffirst (:tempids tx)))]
    (d/transact (connect) [{:db/id (user-id {:code (:user board)}) :user/boards id}])))
