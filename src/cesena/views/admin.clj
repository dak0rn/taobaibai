;; admin.clj - Admin views
(ns cesena.views.admin
  (:require [ cesena.views.partials.document :refer [ document ] ]
            [ ring.util.anti-forgery :refer [ anti-forgery-field ] ]
            [ hiccup.form :refer [ form-to ] ]
            [ cesena.views.partials.navigation :refer [ navigation ] ]
            ))

;; Helper that creates a form performing a POST
;; to the given url with the given user
(defn- button-form
  [ url label { uid :user_id } ]
  (form-to [ :post url ]
    (anti-forgery-field)
    [ :input { :type "hidden" :name "uid" :value uid } ]
    [ :button label ]))

(def lock-user-form (partial button-form "/admin/lock" "Lock"))
(def unlock-user-form (partial button-form "/admin/unlock" "Unlock"))
(def delete-user-form (partial button-form "/admin/delete" "Delete"))

;; User list
(defn- user-list
  [ all-users ]
  [ :div.user-list
   [ :h2 "Users" ]
   (for [ user all-users ]
     (let [ is-locked (nil? (:passwd user)) ]
       [ :div.user-row
         [ :div.user-name (:name user) ]
         [ :div.user-locked (when is-locked "Locked") ]
         [ :div.user-admin (when (= 1 (:admin user)) "Admin") ]
         [ :div.user-button
          (if is-locked (unlock-user-form user) (lock-user-form user) )
          (delete-user-form user)
          [ :button "Change password" ] ]
     ]))])

;; Form used to create a new user
(defn- user-create-form
  [ ]
  (form-to [ :post "/admin/create" ]
    [ :h2 "Create user" ]
    (anti-forgery-field)
    [ :label "Username" ]
    [ :input { :name "name" :placeholder "Username" :required true :type "text" } ]
    [ :label "Password" ]
    [ :input { :name "passwd" :placeholder "Password" :type "password" :required true } ]
    [ :label
      "Is Admin?"
      [ :input { :type "checkbox" :name "admin" } ]
     ]
    [ :div [ :button "Submit" ] ]
))

(defn
  ^{
     :doc "Renders the admin overview page"
     :added "0.1.0"
  }
  render-admin
  [ session all-users ]
  (document "Cesena Admin"
    (navigation session)
    [ :main
      [ :h1 "Cesena Administration" ]
      [ :div.admin-grid
        [ :div.admin-left (user-list all-users) ]
        [ :div.admin-right (user-create-form) ]
      ]
    ]))