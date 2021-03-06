;; admin.clj - Admin routes
(ns cesena.routes.admin
  (:require [ compojure.core :refer [ GET POST ] ]
            [ cesena.routes :refer [ url ] ]
            [ cesena.views.admin :refer [ render-admin ] ]
            [ ring.util.response :refer [ redirect ] ]
            [ cesena.services.user :refer [ find-all-users create-user lock-user change-password delete-user ] ]
            [ cesena.services.scanner :refer [ rescan-library get-rescanning-error ] ]
            [ cesena.middlewares.local :refer [ require-admin ] ]))

;; Route handler functions
(defn-
  ^{
     :doc "Handler for /admin"
     :added "0.1.0"
  }
  handle-admin
  [ request ]
  (let [ user (:cesena-session request)
         query (:query-params request)
         re (get-rescanning-error)
         all-users (find-all-users) ]
    (render-admin user all-users query re)))

(defn-
  ^{
     :doc "Handler for creating a new user"
     :added "0.1.0"
  }
  handle-create-user
  [ request ]
  (let [ user-def (:form-params request)
         name (get user-def "name")
         passwd (get user-def "passwd")
         is-admin (= "on" (get user-def "admin")) ]
    (if (or (empty? name) (empty? passwd))
      (redirect (url "/admin?error=values-missing"))
      (do
        (create-user name passwd { :admin is-admin })
        (redirect (url "/admin?success=created"))))))

(defn-
  ^{
     :doc "Handler for /admin/lock that locks a given user"
     :added "0.1.0"
  }
  handle-lock
  [ request ]
  (if-let [ who (get-in request [ :form-params "uid" ]) ]
    (do
      (lock-user { :user_id who }) ;; Emulating a user here
      (redirect (url "/admin?success=locked")))
    (redirect (url "/admin?error=lock-missing"))))


(defn-
  ^{
     :doc "Handler for /admin/change that changes the user's password"
     :added "0.1.0"
  }
  handle-change
  [ request ]
  (let [ form-params (:form-params request)
         who (get form-params "uid")
         password (get form-params "password") ]
    (if (or (empty? who) (empty? password))
       (redirect (url "/admin?error=change-missing"))
       (do
         (change-password { :user_id who } password)
         (redirect (url "/admin?success=change"))))))

(defn-
  ^{
     :doc "Handler for /admin/delete that deletes a user"
     :added "0.1.0"
  }
  handle-delete
  [ request ]
  (let [ who (get-in request [ :form-params "uid" ]) ]
    (delete-user { :user_id who })
    (redirect (url "/admin?success=delete"))))

(defn-
  ^{
     :doc "Handler for /admin/rescan"
     :added "0.1.0"
  }
  handle-rescan
  [ request ]
  (rescan-library)
  (redirect (url "/admin?success=rescan")))

;; Exported routes

(def secured-admin (require-admin handle-admin))
(def secured-create (require-admin handle-create-user))
(def secured-lock (require-admin handle-lock))
(def secured-change (require-admin handle-change))
(def secured-delete (require-admin handle-delete))
(def secured-rescan (require-admin handle-rescan))

(def routes [
  (GET "/admin" request (secured-admin request))
  (POST "/admin/create" request (secured-create request))
  (POST "/admin/lock" request (secured-lock request))
  (POST "/admin/change" request (secured-change request))
  (POST "/admin/delete" request (secured-delete request))
  (POST "/admin/rescan" request (secured-rescan request))
])
