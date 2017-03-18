(ns hj1984.about
    (:require [hiccup.page :refer [html5]]
              [hj1984.common :as common]))

(defn render [{global-meta :meta posts :entries}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
    (common/head global-meta)
    [:body
      (common/navigation-bar global-meta)
      [:div.main 
        [:div.container
          [:p "I'm a developer, \"about me\" coming soon..."]                  
        ]]
      (common/footer)
    ]  
  ))