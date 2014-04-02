(ns memoro.routes
  (:use [clojure.string :only (join)]
        [compojure.core]
        [ring.middleware.json]
        [ring.util.response :only (file-response)])
  (:require [memoro.users :as users]
            [clojure.data.json :as json]
            [memoro.database :as db]
            [compojure.response :as response]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn create-user []
  (let [user (memoro.users/create-user)]
    (db/add-a-user user)
    (:code user)))

(defn get-users []
  (json/write-str (db/get-users)))

(defroutes app-routes
  (GET "/users" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (get-users)})
  (POST "/users" []
        {:status 303
         :headers {"Location" (str "/user.html?code=" (create-user))}})
  (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
