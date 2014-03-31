(ns memoro.database
  (:use [datomic.api :only (q db) :as d]))

(def uri "datomic:mem://memoro")

(defn connect [uri]
  (def conn (d/connect uri)))

(defn create-schema [conn]
  (d/transact conn [{:db/id (d/tempid :db.part/db)
                     :db/ident :user/code
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "A user's code"
                     :db.install/_attribute :db.part/db}]))

(defn make-db []
  (d/create-database uri)
  (connect uri)
  (create-schema conn))

(defn delete-db []
  (d/delete-database uri))

(defn add-a-user [user]
  (d/transact conn [{:db/id (d/tempid :db.part/user) :user/code (:code user)}]))

(defn get-users []
  (map
   (fn [code] {:code (first code)})
   (q '[:find ?n :where [?c user/code ?n ]] (db conn))))

(defn find-user [code]
  (first (first (q '[:find ?c :where [?c user/code ~code]] (db conn)))))
