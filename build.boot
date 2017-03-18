(set-env!
 :source-paths #{"src"}
 :resource-paths #{"content"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                 [perun "0.4.2-SNAPSHOT"]
                 [hiccup "1.0.5"]
                 [pandeiro/boot-http "0.7.3"]
                 [deraen/boot-less "0.5.0"]
                 [adzerk/boot-reload "0.5.1" :scope "test"]
                 [org.webjars.npm/normalize.css "3.0.3"]
                 [org.webjars.npm/highlight.js "8.7.0"]
                 [clj-time "0.12.0"]])

(require '[io.perun :refer :all]
         '[pandeiro.boot-http :refer [serve]]
         '[hj1984.index :as index-view]
         '[hj1984.post :as post-view]
         '[hj1984.tags :as tags-view]
         '[deraen.boot-less :refer [less]]
         '[adzerk.boot-reload :refer [reload]])

(deftask build
  "Build test blog. This task is just for testing different plugins together."
  []
  (comp (less)
        (global-metadata)
        (markdown)
        (draft)
        (print-meta)
        (slug)
        (ttr)
        (word-count)
        (build-date)
        (render :renderer 'hj1984.post/render)
        (collection :renderer 'hj1984.index/render :page "index.html")
        (collection :renderer 'hj1984.tags/render :page "tags/index.html")
        (static :renderer 'hj1984.about/render :page "about.html")
        (inject-scripts :scripts #{"public/app/main.js"})
        (sitemap)
        (print-meta)
        (target)
        ))

(deftask dev
  []
  (comp (watch)
        (build)
        (reload)
        (serve :resource-root "public")))