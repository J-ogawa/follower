(ns follower.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [follower.events]
            [follower.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(enable-console-print!)

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn attack [from to]
  (let [attacked (update-in to [:hp] - (:ap from))]
    (dispatch [:attacked attacked])))

(defn on-press-follower [target]
  (let [selected @(subscribe [:selected])
        turn @(subscribe [:turn])]
    (if (= (keyword (:leader target)) turn)
      (dispatch [:select target])
      (when-not (nil? selected) (attack selected target)))
    ))

(defn row [item]
  (r/create-element
    (r/reactify-component
      (fn []
        (let [info (:item (js->clj item :keywordize-keys true))
              selected @(subscribe [:selected])]
          [touchable-highlight {:on-press #(on-press-follower info)}
           [view {:style {:flex 1 :flex-direction :column :padding 20 :background-color (if (= info selected) :gray :green) :width 60}}
            [view {:style {:flex 3}}
             [text {:style {:flex 1 :color :white :font-size 30 :text-align :center}} (:key info)]]
            [view {:style {:align-items :center}}
             [view {:style {:flex 1 :flex-direction :row :background-color :yellow}}
              [view {:style {:width 30 :background-color :cyan}} [text {:style {:text-align :center}} (:hp info)]]
              [view {:style {:width 30 :background-color :pink}} [text {:style {:text-align :center}} (:ap info)]]
              ]]
            ]]
          )))))

(defn player-container [player-role]
  (fn []
    (let [player @(subscribe [player-role])]
      [view {:style {:flex 1  :background-color "pink"}}
       [view {:style {:flex 3 :background-color :black}}
        [flat-list {:style {:background-color :blue}
                    :content-container-style {:justify-content :center
                                              :flex-direction :row
                                              :flex-wrap :wrap}
                    :data (clj->js (map #(merge % {:key (:id %) :leader player-role})(:followers player)))
                    :render-item row}]]
       [view {:style {:flex 1 :background-color :red}}
        [text {:style {:flex 1 :color :white :font-size 40 :text-align :center}} (:hp player)]
        ]
       ])))

(defn app-root []
  (fn []
    [view {:style {:flex 1 :flex-direction "column" :background-color "green"}}
     [view {:style {:flex 1 :transform [{ :rotate "180deg" }] :background-color :yellow}}
      [player-container :opponent]]
     [view {:style {:flex 1 :flex-direction "row" :background-color "skyblue"}}
      [player-container :me]]]))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "Follower" #(r/reactify-component app-root)))
