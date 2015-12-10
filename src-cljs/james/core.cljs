(ns james.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  [:li {:class (when (= page (session/get :page)) "active")}
   [:a {:href uri
        :on-click #(reset! collapsed? true)}
    title]])

(defn navbar []
  (let [collapsed? (atom true)]
    (fn []
      [:nav.navbar.navbar-inverse.navbar-fixed-top
       [:div.container
        [:div.navbar-header
         [:button.navbar-toggle
          {:class         (when-not @collapsed? "collapsed")
           :data-toggle   "collapse"
           :aria-expanded @collapsed?
           :aria-controls "navbar"
           :on-click      #(swap! collapsed? not)}
          [:span.sr-only "Toggle Navigation"]
          [:span.icon-bar]
          [:span.icon-bar]
          [:span.icon-bar]]
         [:a.navbar-brand {:href "#/"} "james"]]
        [:div.navbar-collapse.collapse
         (when-not @collapsed? {:class "in"})
         [:ul.nav.navbar-nav
          [nav-link "#/" "Home" :home collapsed?]
          [nav-link "#/about" "About" :about collapsed?]
          [nav-link "#/cookbook" "Cookbook" :cookbook collapsed?]
          [nav-link "#/services" "Services" :services collapsed?]]]]])))

(defn services-page []
  [:div.container-fluid
  [:div.row.hidden-xs
  [:div.col-md
    [:img.img-rounded.center-block {:src "/img/stonehenge.jpg"}]]]])

(defn cookbook-page []
  [:div.container
  [:div.row.hidden-xs
   [:img.img-rounded.center-block {:src "/img/4js.jpg"}]]])

(defn about-page []
  [:div.container
  [:div.row.hidden-xs
   [:img.img-rounded.center-block {:src "/img/selfie.jpg"}]
   [:div.row.well
    [:div.col-sm-6
     "James Farrington was born in a Florida hospital just in time for the presidential debate. He was raised in the English countryside, on German trains, in the back of c-130's. He planted trees in western deserts, and volunteered in an emergency shelter in the north country during the ice storm of '98. James taught wilderness survival for the boy scouts, and worked as an overnight technician at a UPN station."]
    [:div
     ]]]])

(defn home-page []
  [:div.container
   [:div.jumbotron.intro-header
      [:h2.intro "James Farrington"]
      [:p.intro "Steady hands for the future..."]
    [:p [:a.btn.btn-primary.btn-lg {:href "http://www.github.com/JamesTFarrington"} "Github"]
        [:a.btn.btn-primary.btn-lg {:href "http://www.twitter.com/philosophyjames"} "Twitter"]]]
   [:div.row.well
    [:div.col-md-6
     [:h2 "Full stack Clojure"]
     [:p "This website is a full page Clojurescript app written using Luminus, Reactjs, and Reagent. Reagent leverages Clojure's immutable data structures and Facebook's ReactJS to great effect. Developer workflows are made simpler, and everything is reactive by default."]]
    [:div.col-md-6
     [:h2 "Custom Python solutions"]
     [:p "I've helped create several web apps using Django, Twisted, and Hendrix. I'm experienced scripting in Python and Bash. The clarity that Python brings to developing has had an incredible impact on how I write code. The more clever an object the more valuable the docstring."]]]
   [:div.row.well
    [:div.col-md-6
     [:h2 "Opinionated web development"]
     [:p "I believe in the open source movement. Transparency is incredibly important."]]
    [:div.col-md-6
     [:h2 "Outstanding debugging"]
     [:p "I hate bugs. I crush them with my fists. The best way to prevent bugs is with transparency. Anything that can be destroyed by the truth should be destroyed by the truth."]]]
   [:div.row.well
    [:div.col-md-6
     [:h2 "Test driven development"]
     [:p "I believe testing should drive development. Testing prevents unexpected behavior."]]
    [:div.col-md-6
     [:h2 "Graph and sql databases"]
     [:p "Database access is expensive."]]]
])

(def pages
  {:home #'home-page
   :about #'about-page
   :cookbook #'cookbook-page
   :services #'services-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

(secretary/defroute "/cookbook" []
  (session/put! :page :cookbook))

(secretary/defroute "/services" []
  (session/put! :page :services))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          EventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET (str js/context "/docs") {:handler #(session/put! :docs %)}))

(defn mount-components []
  (reagent/render [#'navbar] (.getElementById js/document "navbar"))
  (reagent/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
