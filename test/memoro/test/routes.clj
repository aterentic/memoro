(ns memoro.test.routes
  (:use clojure.test
        ring.mock.request
        memoro.routes))

(deftest test-app
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
