(ns james.config
  (:require [taoensso.timbre :as timbre]))

(def defaults
  {:init
   (fn []
     (timbre/info "\n-=[james started successfully]=-"))
   :middleware identity})
