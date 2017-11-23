(ns follower.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))

(reg-sub
  :me
  (fn [db _]
    (:me db)))

(reg-sub
  :opponent
  (fn [db _]
    (:opponent db)))

(reg-sub
  :selected
  (fn [db _]
    (:selected db)))

(reg-sub
  :turn
  (fn [db _]
    (:turn db)))