(ns memoro.routes
  (:require [compojure.core :refer [ANY defroutes]]
            [ring.middleware
             [params :refer [wrap-params]]
             [nested-params :refer [wrap-nested-params]]
             [keyword-params :refer [wrap-keyword-params]]
             [json :refer [wrap-json-response]]]
            [clojure.data.json :as json]
            [liberator.core :refer [defresource]]
            [memoro.notes :as notes]))

(defn- read-json [ctx]
  (dosync (-> ctx
              (get-in [:request :body])
              slurp
              (json/read-str :key-fn keyword))))

(defresource notes
  :allowed-methods [:get :post]
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx] (json/write-str (notes/get-notes)))
  :post! (fn [ctx] {::identificator (notes/create-note)})
  :post-redirect? (fn [ctx] {:location (format "/notes/%s" (::identificator ctx))}))

(defresource note [identificator]
  :allowed-methods [:get]
  :exists? (fn [ctx] (if-let [id (notes/find-note identificator)] {::id id}))
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (json/write-str (notes/get-note identificator) :escape-slash false)))

(defresource items [note]
  :allowed-methods [:get :post]
  :exists? (fn [ctx] (if-let [id (notes/find-note note)] {::id id}))
  :post! (fn [ctx] (notes/add-item (read-json ctx)))
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (json/write-str (notes/get-items note) :escape-slash false)))

(defresource item [_ id]
  :allowed-methods [:get]
  :exists? (fn [_] (notes/get-item id))
  :available-media-types ["application/json"]
  :handle-ok (fn [_] (json/write-str (notes/get-item id))))

(defroutes resource-routes
  (ANY "/notes" [] notes)
  (ANY "/notes/:identificator" [identificator] (note identificator))
  (ANY "/notes/:note/items" [note] (items note))
  (ANY "/notes/:note/items/:id" [_ id]))

(def app
  (->  resource-routes
       (wrap-keyword-params)
       (wrap-nested-params)
       (wrap-params)
       (wrap-json-response {:escape-non-ascii true})))
