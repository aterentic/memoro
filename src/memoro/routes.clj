(ns memoro.routes
  (:use [compojure.core]
        [ring.middleware params nested-params keyword-params]
        [ring.middleware.json :only [wrap-json-response]]
        [ring.util.response :only [response]])
  (:require [memoro.users :as users]
            [memoro.database :as db]
            [clojure.data.json :as json]
            [compojure.handler :as handler]
            [compojure.route :as route]))

;; TODO unauthorized-user returns invalid WWW-Authenticate header value.
(defn unauthorized-user []
  {:status 401
   :headers {"Content-Type" "text/html; charset=UTF8", "WWW-Authenticate" "Query realm=\"MemoroUser\""}
   :body "User code is not provided in URL or user does not exist"})

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

(defn authenticated? [user]
  (db/exists? user))

(defn wrap-authenication [handler]
  (fn [request]
    (if (authenticated? (:params request))
      (handler request)
      (unauthorized-user))))

(defmacro defauthroutes [name {:keys [context auth-handler]} & handlers]
  `(def ~name (context ~context [] (~auth-handler (routes ~@handlers)))))

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
          (see-other (note-api-location note))))
  (POST "/user/:user/board/:name/note/:id" {params :body}
       (let [note (json/read-str (slurp params) :key-fn keyword)]
         (println note)
             (db/add-note-item (:id note) (:text note))
         (see-other (note-api-location note)))))

(defauthroutes user-routes
  {:context "/user" :auth-handler wrap-authenication}
  (GET "/" {user :params}
       (response (db/get-user user))))

(defroutes app-routes
  (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes
       user-routes
       (context "/api" [] json-routes)
       app-routes)
      (wrap-keyword-params)
      (wrap-nested-params)
      (wrap-params)
      (wrap-json-response {:escape-non-ascii true})))
