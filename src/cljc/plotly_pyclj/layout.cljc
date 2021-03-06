(ns plotly-pyclj.layout
  (:require
   #?(:cljs [plotly-pyclj.utils :refer-macros [reg-paths]]
      :clj  [plotly-pyclj.utils :refer (reg-path reg-paths)])
   [plotly-pyclj.schema :as schema]))

(def subtree (schema/api-subtree (:layout schema/paths)))
(def paths (schema/api-paths (:layout schema/paths) 999 []))

(def help-path (schema/api-help subtree))
(defn help [f & ks] (apply help-path (drop 1 (f)) ks))

(def fn-args
  (schema/paths->fn-args [:layout] paths help-path))

(defn make-layout []
  (let [ks (->> (keys ((schema/api-subtree (:layout schema/paths)) []))
                (remove schema/api-excluded-keys))
        vs (mapv #(-> [%] help-path :dflt) ks)
        args (into (sorted-map) (map vector ks vs))]
    (eval `(reg-path "layout" [:layout] "Layout of plot" ~args))))

(do (eval `(reg-paths ~fn-args))
    (make-layout))

;; automatically generated by
;; (doseq [x (reg-paths (into (sorted-map) symbol+paths))]
;;   (println x))

(comment
  ;; move this into test
  (sort (plotly-pyclj.utils/show-fns *ns*))
  (def m {:layout {:width 100 :legend {:position :h :title "text"} :margin {:t 0 :r 50}}})
  (margin {:layout {:width 100 :legend {:position :h} :margin {:t 0 :r 50}}})
  (= (layout {:layout {:width 100}} {:height 100}) {:layout {:height 100}})
  (= (layout {:layout {:width 100}}) {:width 100})
  (= (layout {:layout {:width 100}} assoc :height 100) {:layout {:width 100, :height 100}})
  (= (layout {:layout {:width 100}} #(assoc % :height (* 2 (:width %))))
     {:layout {:width 100, :height 200}})

  )
