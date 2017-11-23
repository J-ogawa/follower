(ns follower.db
  (:require [clojure.spec.alpha :as s]))

;;; spec of app-db
;(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un []));[::greeting]))

(def app-db {:me {
                  :hp 10
                  :mp 20
                  :followers [
                              {:id "a" :hp 6 :ap 7}
                              {:id "b" :hp 8 :ap 4}
                              {:id "c" :hp 4 :ap 7}
                              {:id "d" :hp 5 :ap 3}
                              {:id "e" :hp 2 :ap 4}
                              {:id "f" :hp 6 :ap 3}
                              {:id "g" :hp 3 :ap 8}
                              {:id "h" :hp 1 :ap 7}
                              {:id "i" :hp 5 :ap 7}
                              ]
                  }
             :opponent {
                        :hp 13
                        :mp 20
                        :followers [
                                    {:id "j" :hp 6 :ap 7}
                                    {:id "k" :hp 4 :ap 7}
                                    {:id "l" :hp 5 :ap 3}
                                    {:id "m" :hp 6 :ap 3}
                                    {:id "n" :hp 1 :ap 7}
                                    {:id "o" :hp 5 :ap 7}
                                    ]
                        }
             :selected nil
             :turn :me})
