(ns james.routes.home
  (:require [james.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]
            [james.db.core :as db]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [ring.util.response :refer [redirect]]))

(defn validate-message [params]
  (first
    (b/validate
      params
      :name v/required
      :message [v/required [v/min-count 10]])))

(defn save-recipe! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (-> (redirect "/")
        (assoc :flash (assoc params :errors errors)))
    (do
     (db/save-recipe!
       (assoc params :timestamp (java.util.Date.)))
      (redirect "/"))))

(defn cookbook-page [{:keys [flash]}]
  (layout/render
   "home.html"
   (merge {:messages (db/get-recipes)}
          (select-keys flash [:name :message :errors]))))

(defn home-page []
  (layout/render "index.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/cookbook" request (cookbook-page request))
  (POST "/cookbook" request (save-recipe! request))
  (GET "/docs" [] (ok (-> "docs/docs.md" io/resource slurp))))

