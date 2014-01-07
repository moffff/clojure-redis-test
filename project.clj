(defproject redis-test "0.1.2-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.3.1"]
                 [com.taoensso/carmine "2.4.0"]
                 [http-kit "2.1.13"]
                 [compojure "1.1.6"]
                 [org.clojure/data.json "0.2.3"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-jetty-adapter "1.2.0"]]
  :main ^:skip-aot redis-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
