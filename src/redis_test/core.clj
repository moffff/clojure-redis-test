(ns redis-test.core
  (:gen-class)
  (:use [compojure.core :only [defroutes GET]]
        [clojure.tools.cli :only [cli]]
        org.httpkit.server
        )
  (:require [taoensso.carmine :as car :refer (wcar)]
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


(defn -main [& args]
  (let  [[opts args banner] (cli *command-line-args*
     ["-p" "--port" "Listen on this port" :default 8080 :parse-fn #(Integer. %) ]
     ["-h" "--help" "Show this help" :default false :flag true ])]
    (when (:help opts)
      (println banner)
      (System/exit 0))
    (let [port (get opts :port)]
      (run-server app {:port port})
      (println (str "Server started on http://0.0.0.0:" port)))))
