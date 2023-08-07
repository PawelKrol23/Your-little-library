-- Books
-- Genres: 0 - ROMANCE,
INSERT INTO Book(title, genre, publication_year) VALUES ('The Nanny Diaries', 0, 2002);

-- Authors
-- Nationalities: 0 - UNITED_KINGDOM, 1 - GERMAN, 2 - AMERICAN
INSERT INTO Author(first_name, last_name, date_of_birth, nationality) VALUES ('Emma', 'McLaughlin', '1974-02-07', 2);
INSERT INTO Author(first_name, last_name, date_of_birth, nationality) VALUES ('Nicola', 'Kraus', '1974-08-17', 2);

-- Relationship
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Emma' AND last_name='McLaughlin',
                                                     SELECT id FROM Book WHERE title='The Nanny Diaries');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Nicola' AND last_name='Kraus',
                                                     SELECT id FROM Book WHERE title='The Nanny Diaries');