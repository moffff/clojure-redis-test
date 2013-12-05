(ns redis-test.core
  (:use [compojure.core :only [defroutes GET]]
        org.httpkit.server)
  (:require [taoensso.carmine :as car :refer (wcar)]
            [clojure.data.json :as json]))

(def server1-conn {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn create-test-data
  "I don't do a whole lot."
  [redis-key redis-value]

  (wcar* (car/set redis-key redis-value)))

(defn request
  "I don't do a whole lot."
  [redis-key]

  (json/write-str (or (wcar* (car/get redis-key)) [])))

(defn show-request
  "I don't do a whole lot."
  [id]

  (let [day (.getDate (new java.util.Date))
        redis-key (str "users:" id ":" day)]
    (request redis-key)))

(defroutes app
  (GET "/api/v1/users/:id" [] show-request))

(run-server app {:port 8080})
