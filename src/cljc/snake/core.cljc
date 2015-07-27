(ns snake.core
  (:require [quil.core :as q #?@(:cljs [:include-macros true])]
            [quil.middleware :as m]))

(def world { :width 100 :height 100 :food-amount 1000 })

(defn gen-food [] [(rand-int (world :width)) (rand-int (world :width))])

(defn replenish-food [initial amount]
  (loop [food initial] (if (>= (count food) amount) food (recur (conj food (gen-food))))))

(defn wrap [i m]
  (loop [x i] (cond (< x 0) (recur (+ x m)) (>= x m) (recur (- x m)) :else x)))

(defn grow-snake [{:keys [snake velocity] :as state}]
  (let [[px py] (map + (peek snake) velocity)]
    (assoc state :snake (conj snake [(wrap px (world :width)) (wrap py (world :height))]))))

(defn eat [{:keys [snake food] :as state}]
  (if-let [pellet (food (peek snake))]
    (-> state (update :food disj pellet))
    (-> state (update :snake subvec 1))))

(defn reset? [{:keys [snake] :as state}]
  (if (apply distinct? snake)
    state
    (assoc state :snake [(peek snake)])))

(defn setup []
  (do (q/smooth)
      (q/frame-rate 30)
      {:snake [[50 50]] :velocity [1 0] :food (replenish-food #{} (world :food-amount))}))

(defn draw [{ :keys [snake food] }]
  (let [w (/ (q/width) (world :width))
        h (/ (q/height) (world :height))]
    (do
      (q/smooth)
      (q/stroke-cap :round)
      (q/stroke-join :round)
      (q/background 0 0 0)

      (q/fill 0 255 0)
      (q/stroke 0 255 0)
      (doseq [[x y] food](q/rect (* w x) (* h y) w h))

      (q/fill 255 0 0)
      (q/stroke 255 0 0)
      (doseq [[x y] snake](q/rect (* w x) (* h y) w h))

      (q/fill 0 255 255)
      (q/text (str "Score: " (count snake)) 10 15))))

(defn launch-sketch [{:keys[width height host]}]
  (q/sketch
    :title "Snake"
    :setup setup
    :update #(-> % grow-snake eat (update :food replenish-food (world :food-amount)) reset?)
    :draw draw
    :key-pressed
    (fn [{ :keys [velocity] :as state} { :keys [key-code] }]
      (case key-code
        37 (if (not= [1 0] velocity) (assoc state :velocity [-1 0]) state)
        39 (if (not= [-1 0] velocity) (assoc state :velocity [1 0]) state)
        38 (if (not= [0 1] velocity) (assoc state :velocity [0 -1]) state)
        40 (if (not= [0 -1] velocity) (assoc state :velocity [0 1]) state)
        state))
    :middleware [m/fun-mode]
    :size [width height]
    #?@(:cljs [:host host])))

;#?(:clj (launch-sketch { :width 400 :height 400 }))

#?(:cljs (defn ^:export launch-app[host width height]
           (launch-sketch { :width width :height height :host host})))
