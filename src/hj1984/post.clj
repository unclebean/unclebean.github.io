(ns hj1984.post
    (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [hj1984.common :as common]
        ))

(defn render [{global-meta :meta posts :entries post :entry}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    (common/head-with-keywords-and-share-image global-meta (:title post) (:description post) (or (:share-image post) "/images/ub.png"))
    [:body
      (common/navigation-bar global-meta)
      [:div.main 
        [:div.container.post
        [:h1 (:title post)]
        [:div (:content post)]

        [:aside.comments
          (common/disquss (:permalink post))]
      ]]
      
      (common/footer)
    ]))
