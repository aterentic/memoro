(ns memoro.instarepl
  (:use
   [memoro.webserver :as ws]
   [memoro.database :as db]
   [memoro.users :as users]))

;;(ws/start)
;;(ws/stop)

(db/delete-db)
(db/make-db)
(db/get-users)
(let [user (users/create-user)]
  (db/add-user user)
  (db/add-board {:user (:code user) :name "Default"}))
