;; document.clj - partial views for the document
(ns cesena.views.partials.document
  (:require [ hiccup.core :refer [ h html ] ]
            [ hiccup.page :refer [ include-css ] ]))

(defn
  ^{
    :doc "Creates the default wrapping HTMl for the page"
    :added "0.1.0"
  }
  document
  [ title & children ]
  (html
    [ :html
      [ :head
        [ :title (h title) ]
        [ :meta { :name "viewport" :content "width=device-width, initial-scale=1.0" } ]
        (include-css "/cesena.css")
      ]
      [ :body children ]
    ]))
