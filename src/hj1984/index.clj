(ns hj1984.index
    (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [hj1984.common :as common]
        ))

(defn render [{global-meta :meta posts :entries}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    (common/head global-meta)
    [:body
      (common/navigation-bar global-meta)
      [:div.main 
        [:div.container
          (common/render-posts posts)
        ]]
      (common/footer)
    ]))