(ns memoro.instarepl
  (:use
   [memoro.webserver :as ws]
   [memoro.database :as db]
   [memoro.users :as users]))

(ws/start)
(ws/stop)
(db/make-db)
(db/delete-db)
(db/add-a-user (users/create-user))
(db/get-users)
