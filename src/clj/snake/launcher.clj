(ns snake.launcher  (:gen-class)
  (:require [snake.core :as sc]))

(defn -main [& _] (sc/launch-sketch { :width 800 :height 600 }))