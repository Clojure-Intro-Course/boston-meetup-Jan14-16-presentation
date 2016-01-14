(ns intro.workspace
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [firstclassshapes.core :refer :all]))




(defn explosion-helper [complex-shape state]
  (map #(assoc %
          :dy (+ (* (:rate state)  (:dy %)) (:dy %))
          :dx (+ (* (:rate state) (:dx %)) (:dx %))) complex-shape))


(defn explosion [complex-shape state]
  (vec (explosion-helper complex-shape state)))


(defn setup []
  (q/frame-rate 30)
  (def blue-rect (create-rect 25 25 :blue))
  (def orange-rect (create-rect 25 25 :orange))
  (def blue-stack (above blue-rect blue-rect))
  (def orange-stack (above orange-rect orange-rect))
  (def blue-square (beside blue-stack blue-stack))
  (def orange-square (beside orange-stack orange-stack))
  (def blue-orange (beside orange-square
                           blue-square))
  (def blue-orange-line (reduce beside (repeat 4 blue-orange)))
  (def big-bang (reduce above (repeat 8 blue-orange-line)))

  {:rate 0
   :explode false})


(defn mouse-clicked [state event]
  (if (= (:button event) :left)
    (assoc state :explode true)
    (assoc state :explode false)))


(defn update-state [state]
  (if (:explode state)
    {:rate (+ (:rate state) 0.05)
     :explode true}
    {:rate 0
     :explode false}))



(defn draw-state [state]
  (q/background 255)
  (if (:explode state)
    (ds (explosion big-bang state) 400 400)
    (ds big-bang 400 400)))

(q/defsketch start
  :title "Workspace"
  :size [800 800]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-clicked mouse-clicked
  :middleware [m/fun-mode])
