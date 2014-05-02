(ns memoro.routes
  (:use [compojure.core]
        [ring.middleware.json :only [wrap-json-response]]
        [ring.util.response :only [response]])
  (:require [memoro.users :as users]
            [memoro.database :as db]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn see-other [location]
  {:status 303
   :headers {"Location" location}})

(defn user-location [user]
  (str "/user.html?code=" (:code user)))

(defn user-api-location [user]
  (str "/api/user/" (:code user)))

(defn board-api-location [board]
  (str "/api/user/" (:user board) "/board/" (:name board)))

(defn note-api-location [note]
  (str "/api/user/" (:user note) "/board/" (:board note) "/note/" (:id note)))

(defn create-user []
  (let [user (memoro.users/create-user)]
    (db/add-user user)
    user))

(defroutes json-routes
  (GET "/user" []
       (response (db/get-users)))
  (GET "/user/:code" [code]
       (response (db/get-user {:code code})))
  (GET "/user/:user/board/:name" [user name]
       (response (db/get-board {:user user :name name})))
  (GET "/user/:user/board/:name/note/:id" [user name id]
       (response (db/get-note (bigint id))))
  (POST "/user" []
        (-> (create-user)
            (user-location)
            (see-other)))
  (POST "/user/:user/board" {params :body}
        (let [board (json/read-str (slurp params) :key-fn keyword)]
          (db/add-board board)
          (see-other (board-api-location board))))
  (POST "/user/:user/board/:name/note" {params :body}
        (let [data (json/read-str (slurp params) :key-fn keyword)
              note (merge {:id (db/add-note data)} data)]
          (see-other (note-api-location note)))))

(defroutes app-routes
  (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (routes
   (-> (context "/api" [] json-routes)
       (handler/api)
       (wrap-json-response))
   (handler/site app-routes)))
