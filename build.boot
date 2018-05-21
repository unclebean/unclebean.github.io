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

(require '[clojure.string :as str]
         '[io.perun :as perun]
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
        (perun/global-metadata)
        (perun/markdown)
        (perun/draft)
        (perun/print-meta)
        (perun/slug)
        (perun/ttr)
        (perun/word-count)
        (perun/build-date)
        (perun/render :renderer 'hj1984.post/render)
        (perun/collection :renderer 'hj1984.index/render :page "index.html")
        (perun/collection :renderer 'hj1984.tags/render :page "tags/index.html")
        (perun/static :renderer 'hj1984.about/render :page "about.html")
        (perun/inject-scripts :scripts #{"public/app/main.js"})
        (perun/sitemap)
        (perun/print-meta)
        (target)
        ))

(deftask dev
  []
  (comp (watch)
        (build)
        (reload)
        (serve :resource-root "public")))