(ns memoro.test.routes
  (:use clojure.test
        ring.mock.request
        memoro.routes))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "<html><body>Welcome to MEMORO!</body></html>"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
