(ns memoro.instarepl
  (:use
   [memoro.webserver :as ws]
   [memoro.database :as db]
   [memoro.users :as users]))

(comment
  (ws/start)
  (ws/stop))
(comment
   (db/delete-db))

(db/make-db)
(db/add-a-user (users/create-user))
(db/add-a-user (users/create-user))
(db/add-a-user (users/create-user))
(db/get-users)
