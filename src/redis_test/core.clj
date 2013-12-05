(ns redis-test.core
  (:use org.httpkit.server)
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn create-test-data
  "I don't do a whole lot."
  [redis-key redis-value]

  (wcar* (car/set redis-key redis-value)))

(defn request
  "I don't do a whole lot."
  [redis-key]

  (wcar* (car/get redis-key)))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (request "loop")})

(run-server app {:port 8080})
