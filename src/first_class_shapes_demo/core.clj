(ns intro.workspace
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [firstclassshapes.core :refer :all]))


(defn setup []
  (q/frame-rate 30)
  ;; create a simple shape:
  (def sun (create-ellipse 40 40 :yellow))
  ;; import an image and scale it:
  (def happy-sun (scale-shape (create-picture "images/sun.png") 0.3 0.3))
  ;; create an arc:
  (def moon (create-arc 40 40 (- (* Math/PI 4/10)) (* Math/PI 6/10) :white))

  ;; create a combined shape
  (def tree-height 100)
  (def tree-half-height (/ tree-height 2))
  (def tree (above (create-triangle (* tree-half-height 0.6) (* (- tree-half-height) 0.5) (* tree-height 0.6) 0 :green)
                   (create-triangle (* tree-half-height 0.8) (* (- tree-half-height) 0.7) (* tree-height 0.8) 0 :green)
                   (create-triangle tree-half-height (* (- tree-half-height) 0.9) tree-height 0 :green)
                   (create-rect 10 10 :brown)))

  ;; will be needed for drawing multiple trees:
  (defn random-position [] {:x (rand-int 400) :y (+ 150 (rand-int 200))})
  (def tree-positions (repeatedly 10 random-position))

  ;; the state is the position of the sun/moon:
  {:height 100
   :distance 0})


;; generate a new sun/moon position from the current one:
(defn update-state [state]
  (let [height (:height state)
        new-height (if (< (:distance state) 200) (- height 0.5) (+ height 0.5))]
    {:distance (+ (:distance state) 1)
     :height new-height}))


;; draw the picture according to the state:
(defn draw-state [state]
  ;; blue background:
  (q/background 0 200 220)
  ;; white snow:
  (ds (create-rect 400 200 :white) 200 300)
  ;; sun at its current position
  (ds sun (:distance state) (:height state))
  ;; forest:
  (ds tree 80 250))
  ;(doall (map #(ds tree (:x %) (:y %)) tree-positions)))

;; specifying what functions to use in the quil applet
(q/defsketch start
  :title "Winter scene"
  :size [400 400]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
