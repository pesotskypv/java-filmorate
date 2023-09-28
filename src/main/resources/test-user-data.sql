INSERT INTO users ("login", "name", birthday, "email") VALUES ('dolore', 'Nick Name', '1946-08-20', 'mail@mail.ru');
INSERT INTO users ("login", "name", birthday, "email") VALUES ('common', 'common', '2000-08-20', 'friend@common.ru');
INSERT INTO users ("login", "name", birthday, "email") VALUES ('third', 'third', '1987-08-20', 'third@third.ru');
INSERT INTO friends_users (user_id, friend_id) VALUES (1, 2);
INSERT INTO friends_users (user_id, friend_id) VALUES (3, 2);
INSERT INTO friends_users (user_id, friend_id) VALUES (2, 3);
INSERT INTO friends_users (user_id, friend_id) VALUES (2, 1);
INSERT INTO films ("name", "description", release_date, duration, mpa_id)
    VALUES ('film1', 'description_film1', '1992-08-20', 120, '1');
INSERT INTO films ("name", "description", release_date, duration, mpa_id)
    VALUES ('film2', 'description_film2', '2011-08-20', 90, '2');
INSERT INTO likes_films (film_id, user_id) VALUES (1, 2);
INSERT INTO likes_films (film_id, user_id) VALUES (2, 1);
INSERT INTO likes_films (film_id, user_id) VALUES (2, 3);