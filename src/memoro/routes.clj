(ns memoro.routes
  (:use [compojure.core])
  (:require [memoro.users :as users]
            [memoro.database :as db]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn json-response [body]
  {:status 200
   :headers {"Content-Type" "application/json; charset=UTF-8"}
   :body body})

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
       (-> (db/get-users)
           (json/write-str)
           (json-response)))
  (GET "/user/:code" [code]
       (-> (db/get-user {:code code})
           (json/write-str)
           (json-response)))
  (GET "/user/:user/board/:name" [user name]
       (-> (db/get-board {:user user :name name})
           (json/write-str)
           (json-response)))
  (GET "/user/:user/board/:name/note/:id" [user name id]
       (-> (db/get-note (bigint id))
           (json/write-str)
           (json-response)))
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
   (handler/api (context "/api" [] json-routes))
   (handler/site app-routes)))
