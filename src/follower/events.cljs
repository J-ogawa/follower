(ns follower.events
  (:require
    [re-frame.core :refer [reg-event-db after]]
    [clojure.spec.alpha :as s]
    [follower.db :as db :refer [app-db]]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
  :initialize-db
  validate-spec
  (fn [_ _]
    app-db))

(reg-event-db
  :select
  validate-spec
  (fn [db [_ value]]
    (assoc db :selected value)))

(reg-event-db
  :attacked
  validate-spec
  (fn [db [_ value]]
    (let [leader (keyword (:leader value))
          attacked-index (.indexOf (to-array (map :id (get-in db [leader :followers]))) (:id value))]
      (-> db
          (assoc :selected nil)
          (assoc :turn leader)
          (assoc-in [leader :followers attacked-index] (apply dissoc value [:leader :key]))
          (update-in [leader :followers] (fn [x] (vec (filter #(> (:hp %) 0) x))))))))
