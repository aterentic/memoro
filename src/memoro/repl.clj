(ns memoro.instarepl
  (:require
   [memoro.database :as db]
   [memoro.users :as users]))

(memoro.database/delete-db)
(memoro.database/make-db)
(memoro.database/get-users)
(let [user (memoro.users/create-user)]
  (memoro.database/add-user user)
  (memoro.database/add-board {:user (:code user) :name "Default"}))
