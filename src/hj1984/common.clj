(ns hj1984.common
    (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.util :refer [url-encode]]
            [clojure.string :as string]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [clj-time.format :as tf]))

(defn google-analytic [] 
  [:script "
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-94017226-1', 'auto');
  ga('send', 'pageview');
"])

(defn head [global-meta]
  [:head
   [:title (:site-title global-meta)]
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
   [:link {:rel "shortcut icon" :href "/favicon.ico"}]
   [:link {:rel "alternate" :type "application/atom+xml" :title "Atom feed" :href "/atom.xml"}]
   (include-css "/css/app.css")
   (include-css (str "https://fonts.googleapis.com/css?family=" (url-encode "Source+Code+Pro|Arvo:400,700|Droid+Serif:400,400italic,700")))
   (include-js "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/highlight.min.js")
   (include-js "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.6/languages/clojure.min.js")
   [:script "hljs.initHighlightingOnLoad();"]
   (google-analytic)
   ])

(defn head-with-keywords [global-meta title]
  (assoc-in (head global-meta) [1 1] title)
)

(defn disquss [id]
  [:div [:div#disqus_thread]
   [:script {:type "text/javascript"}
    (format
       "var disqus_config = function () {
            //this.page.url = 'http://unclebean.github.io/hugh/';
            this.page.identifier = '%s';
        };
        (function() { 
        var d = document, s = d.createElement('script');
        s.src = 'https://hj1984.disqus.com/embed.js';
        s.setAttribute('data-timestamp', +new Date());
        (d.head || d.body).appendChild(s);
        })();" id)]
    [:noscript "Please enable JavaScript to view the <a href=\"https://disqus.com/?ref_noscript\" rel=\"nofollow\">comments powered by Disqus.</a></noscript>"
   ]])


(defn datestr
  ([date] (datestr date "dd MMM YYYY"))
  ([date fmt] (tf/unparse (tf/formatter fmt) (tc/from-date date))))

(defn navigation-bar [{:keys [site-items]}]
    [:div.header
      [:nav.container {:role "navigation"}   
        [:div.site [:a {:href "/"} [:img {:src "/images/ub_blog.svg"}]]]
        [:ul (for [item site-items]
          [:li [:a {:href (:url item)} (:name item)]])
    
    ]]])

(defn footer []
  [:footer.footer
   [:div.container
    [:ul
     [:li [:a {:href "https://github.com/unclebean"} "github.com/unclebean"]]]]])

(defn render-posts [posts]
    [:ul.posts.columns.small-12
          (for [{:keys [permalink title date-published description]} (sort-by :date-created #(compare %2 %1) posts)]
            [:article {:itemprop "blogPost" :itemscope "" :itemtype "http://schema.org/BlogPosting"}
              [:h3
                [:a.title {:href permalink :itemprop "name"} title]
                [:div.secondary
                  [:span.desc description]
                  [:span.date (datestr date-published)]]               
              ]])  
    ]
)
