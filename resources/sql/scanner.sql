-- :name query-clear-db :!
-- :doc "Empties the database"
DELETE FROM cesena_book;

-- :name query-insert-book :i!
-- :doc "Inserts the given book into the database"
INSERT INTO cesena_book (book_id, title, path, checksum, date)
        VALUES (:book_id, :title, :path, :checksum, :date);


-- :name query-set-lock :!
-- :doc "Sets the lock indicating that a rescan is in progress"
UPDATE cesena SET activity_lock = 1;

-- :name query-unset-lock :!
-- :doc "Removes the activity lock"
UPDATE cesena SET activity_lock = 0;

-- :name query-is-locked :? :1
-- :doc "Retrives the activity lock"
SELECT activity_lock FROM cesena LIMIT 1;
