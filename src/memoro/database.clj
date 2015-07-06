(ns memoro.database
  (:require
   [clojure.string :as string :refer [split]]
   [clojure.walk :as walk]
   [datomic.api :as datomic :refer [q db]]))

(def uri "datomic:mem://memoro")

(def schema-data [{:db/ident :note/identificator
                   :db/valueType :db.type/uuid
                   :db/cardinality :db.cardinality/one
                   :db/doc "Note identificator." }
                  {:db/ident :note/name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "Note name."}
                  {:db/ident :note/items
                   :db/valueType :db.type/ref
                   :db/cardinality :db.cardinality/many
                   :db/isComponent true
                   :db/doc "Note items." }
                  {:db/ident :item/priority
                   :db/valueType :db.type/long
                   :db/cardinality :db.cardinality/one
                   :db/doc "Item priority."}
                  {:db/ident :item/checked?
                   :db/valueType :db.type/boolean
                   :db/cardinality :db.cardinality/one
                   :db/doc "Item checked?"}
                  {:db/ident :item/text
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "Item text."}])

(defn schema []
  (map (fn [attr] 
         (merge attr {:db/id (datomic/tempid :db.part/db) :db.install/_attribute :db.part/db})) 
       schema-data))

(defn- connect []
  (datomic/connect uri))

(defn- read-db []
  (datomic/db (connect)))

(defn init-db []
  (datomic/create-database uri)
  (datomic/transact (connect) (schema)))

(defn persist-entity [entity]
  (let [temp-id (datomic/tempid :db.part/user)
        tx (datomic/transact (connect) [(merge {:db/id temp-id} entity)])]
    (datomic/resolve-tempid (read-db) (:tempids @tx) temp-id)))

(defn find-entity [[key identificator]]
  (first (first (datomic/q [:find '?id :in '$ '?identificator :where ['?id key '?identificator]] (read-db) identificator))))

(defn read-entity [id]
  (datomic/entity (read-db) id))

(defn find-entities [id-key]
  (map first (datomic/q [:find '?id :in '$ :where ['?id id-key '_]] (read-db))))

