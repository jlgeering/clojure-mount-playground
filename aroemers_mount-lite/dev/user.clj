(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."

  (:require [clojure.test :refer [run-tests]]
            [clojure.tools.namespace.repl :as tn]
            [mount.lite :as mount :refer [defstate]]))

(defn create [name from]
  (let [id (gensym)]
    (println "creating" name id "from" from)
    {:name name
     :id   id}))

(defn destroy [state from]
  (println "destroying" (:name state) (:id state) "from" from))

(defstate a :start (create "a" "start a")
            :stop  (destroy a "stop a"))

(def a-substitution
  (mount/substitute #'a
                    (mount/state :start (create "a'" "start a'")
                                 :stop  (destroy a "stop a'"))))

(defn info []
  (println "current states:")
  (println "  a:" (:name a) (:id a)))

(defn without-substitution []
  (mount/start)
  (info)
  (mount/stop))

(defn with-substitution []
  (mount/start a-substitution)
  (info)
  (mount/stop))
