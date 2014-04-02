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

(defn create-user []
  (let [user (memoro.users/create-user)]
    (db/add-a-user user)
    user))

(defn user-location [user]
  (str "/user.html?code=" (:code user)))

(defroutes json-routes
  (GET "/users" []
       (-> (db/get-users)
           (json/write-str)
           (json-response)))
  (POST "/users" []
        (-> (create-user)
            (user-location)
            (see-other))))

(defroutes app-routes
  (context "/api" [] json-routes)
  (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
