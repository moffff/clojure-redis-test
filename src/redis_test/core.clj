(ns redis-test.core
  (:gen-class)
  (:use [compojure.core :only [defroutes GET]]
        org.httpkit.server
        )
  (:require [taoensso.carmine :as car :refer (wcar)]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.data.json :as json]))

(def server1-conn {:pool {} :spec {:uri (System/getenv "REDIS_URL") :timeout-ms 15}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))


(defn request
  "I don't do a whole lot."
  [redis-key]

  (json/write-str {:segements (wcar* (car/smembers redis-key))}))

(defn show-request
  [id]

  (let [day (.getDate (new java.util.Date))
        redis-key (str "users:" id )]

    (request redis-key)))

(defroutes app
  (GET "/users/:id" [id] (show-request id)))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 8080
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
  ["-h" "--help"]])

(defn -main [& args]
  (let  [{opts :options banner :summary errors :errors} (parse-opts args cli-options)]
    (when-not (empty? errors)
      (apply println errors)
      (System/exit 1))
    (when (:help opts)
      (println banner)
      (System/exit 0))
    (let [port (get opts :port)]
      (run-server app {:port port})
      (println (str "Server started on http://0.0.0.0:" port)))))
