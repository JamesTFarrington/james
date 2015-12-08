(ns james.config
  (:require [selmer.parser :as parser]
            [taoensso.timbre :as timbre]
            [james.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (timbre/info "\n-=[james started successfully using the development profile]=-"))
   :middleware wrap-dev})
