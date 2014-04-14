[{:db/id #db/id[:db.part/db]
  :db/ident :user/code
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/doc "A user's code"
  :db.install/_attribute :db.part/db}
 {:db/id #db/id[:db.part/db]
  :db/ident :user/boards
  :db/valueType :db.type/ref
  :db/cardinality :db.cardinality/many
  :db/isComponent true
  :db/doc "A users's boards"
  :db.install/_attribute :db.part/db}
 {:db/id #db/id[:db.part/db]
  :db/ident :board/name
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/doc "Board name"
  :db.install/_attribute :db.part/db}
 {:db/id #db/id[:db.part/db]
  :db/ident :board/notes
  :db/valueType :db.type/ref
  :db/cardinality :db.cardinality/many
  :db/isComponent true
  :db/doc "Board notes"
  :db.install/_attribute :db.part/db}
 {:db/id #db/id[:db.part/db]
  :db/ident :note/text
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/doc "Note text"
  :db.install/_attribute :db.part/db}]
