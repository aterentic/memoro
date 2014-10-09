(ns memoro.test.routes
  (:require [clojure.test :refer :all]
        [ring.mock.request :refer :all]
        [memoro.routes :refer :all]))

(deftest test-app
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
