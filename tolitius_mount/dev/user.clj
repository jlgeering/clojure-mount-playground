(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."

  (:require [clojure.test :refer [run-tests]]
            [clojure.tools.namespace.repl :as tn]
            [mount.core :as mount :refer [defstate]]
            [mount.tools.graph]))

(defn create [name from]
  (let [id (gensym)]
    (println "creating" name id "from" from)
    {:name name
     :id   id}))

(defn destroy [state from]
  (println "destroying" (:name state) (:id state) "from" from))

(defstate a :start (create "a" "start a")
            :stop  (destroy a "stop a"))

(defstate b :start (create "b" "start b")
            :stop  (destroy b "stop b"))

(defn info []
  (println "current states:")
  (println " " (mount.tools.graph/states-with-deps))
  (println "  a:" (:name a) (:id a))
  (println "  b:" (:name b) (:id b)))

(defn this-works-as-expected []
  (mount/start)
  (info)
  (mount/stop))

(defn this-is-not-stopping-b []
  (mount/start-with-states {#'user/a #'user/b})
  (info)
  (mount/stop))
