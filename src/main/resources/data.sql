-- Books
-- Genres: 0 - ROMANCE, 1 - FANTASY, 2 - THRILLER, 3 - MYSTERY, 4 - HORROR, 5 - FICTION
INSERT INTO Book(title, genre, publication_year, created_at, updated_at)
VALUES ('The Nanny Diaries', 0, 2002, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Book(title, genre, publication_year, created_at, updated_at)
VALUES ('Beautiful Creatures', 1, 2009, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Book(title, genre, publication_year, created_at, updated_at)
VALUES ('Gideon''s Corpse', 2, 2012, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Book(title, genre, publication_year, created_at, updated_at)
VALUES ('Beach Road', 3, 2006, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Book(title, genre, publication_year, created_at, updated_at)
VALUES ('Good Omens', 4, 1990, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Book(title, genre, publication_year, created_at, updated_at)
VALUES ('The Little Paris Bookshop', 5, 2013, '2023-08-22 01.20.00', '2023-08-22 01.20.00');

-- Authors
-- Nationalities: 0 - UNITED_KINGDOM, 1 - GERMAN, 2 - AMERICAN
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Emma', 'McLaughlin', '1974-02-07', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Nicola', 'Kraus', '1974-08-17', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Kami', 'Garcia', '1972-03-25', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Margaret', 'Stohl', '1967-01-01', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Douglas', 'Preston', '1956-05-20', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Lincoln', 'Child', '1957-10-13', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Peter', 'Jonge', '1957-04-5', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('James', 'Patterson', '1947-03-22', 2, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Terry', 'Pratchett', '1948-04-28', 0, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Neil', 'Gaiman', '1960-11-10', 0, '2023-08-22 01.20.00', '2023-08-22 01.20.00');
INSERT INTO Author(first_name, last_name, date_of_birth, nationality, created_at, updated_at)
VALUES ('Nina', 'George', '1973-08-30', 1, '2023-08-22 01.20.00', '2023-08-22 01.20.00');

-- Relationship
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Emma' AND last_name='McLaughlin',
                                                     SELECT id FROM Book WHERE title='The Nanny Diaries');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Nicola' AND last_name='Kraus',
                                                     SELECT id FROM Book WHERE title='The Nanny Diaries');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Kami' AND last_name='Garcia',
                                                     SELECT id FROM Book WHERE title='Beautiful Creatures');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Margaret' AND last_name='Stohl',
                                                     SELECT id FROM Book WHERE title='Beautiful Creatures');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Douglas' AND last_name='Preston',
                                                     SELECT id FROM Book WHERE title='Gideon''s Corpse');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Lincoln' AND last_name='Child',
                                                     SELECT id FROM Book WHERE title='Gideon''s Corpse');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Peter' AND last_name='Jonge',
                                                     SELECT id FROM Book WHERE title='Beach Road');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='James' AND last_name='Patterson',
                                                     SELECT id FROM Book WHERE title='Beach Road');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Terry' AND last_name='Pratchett',
                                                     SELECT id FROM Book WHERE title='Good Omens');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Neil' AND last_name='Gaiman',
                                                     SELECT id FROM Book WHERE title='Good Omens');
INSERT INTO Author_Book(author_id, book_id) VALUES ( SELECT id FROM Author WHERE first_name='Nina' AND last_name='George',
                                                     SELECT id FROM Book WHERE title='The Little Paris Bookshop');