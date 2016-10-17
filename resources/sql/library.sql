-- :name query-all-books :? :*
-- :doc "Selects all books"
SELECT * FROM cesena_book;

-- :name query-with-term :? :*
-- :doc "Selects all books that match a given criteria"
SELECT * FROM cesena_book WHERE title LIKE :term;

-- :name query-specific-book :? :1
-- :doc "Select a specific book identified by the given id"
SELECT * FROM cesena_book WHERE book_id = :bid;
