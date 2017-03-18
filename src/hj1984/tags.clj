(ns hj1984.tags
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css include-js]]
            [clojure.string :as string]
            [hj1984.common :as common]))

(defn log-scale [min max n m]
  (+ min (* (- max min) (/ (Math/log n) (Math/log m)))))

(defn tag-comparator [[tag-a a] [tag-b b]]
  (or (> (:n a) (:n b))
      (and (= (:n a) (:n b))
           (compare tag-a tag-b))))

(defn render [{:keys [meta entries]}]
  (let [posts-by-tags
        (->> entries
             (reduce
              (fn [acc post]
                (update-in acc [(:keywords post)] (fnil conj []) post))
              {}))]
    (html5
     {:lang "en" :itemtype "http://schema.org/Blog"}
     (common/head meta)
     [:body
      (common/navigation-bar meta)
      [:div.main
       [:div.container.tags
        [:ul.tag-cloud
          (for [[tag _] posts-by-tags]
            (if tag
                [:li.tag [:a {:href (str "#" tag)} tag]]))]
          (for [[tag posts] posts-by-tags]
            (if tag
                [:div
                    [:h3 [:a {:name tag}] tag]
                    [:ul
                        (for [{:keys [permalink title]} (sort-by :title posts)]
                            [:li [:a {:href permalink} title]])]]
            ))
        ]]
      (common/footer)])))