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

(defn create-user []
  (let [user (memoro.users/create-user)]
    (db/add-user user)
    user))

(defroutes json-routes
  (GET "/users" []
       (-> (db/get-users)
           (json/write-str)
           (json-response)))
  (GET "/user/:code" [code]
       (-> (db/get-user {:code code})
           (json/write-str)
           (json-response)))
  (POST "/users" []
        (-> (create-user)
            (user-location)
            (see-other)))
  (POST "/boards" {params :body}
        (let [board (json/read-str (slurp params) :key-fn keyword)]
          (db/add-board board)
          (see-other (user-api-location {:code (:user board)})))))

(defroutes app-routes
  (context "/api" [] json-routes)
  (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
