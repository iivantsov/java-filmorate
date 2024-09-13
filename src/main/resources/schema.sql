DROP TABLE IF EXISTS mpa_ratings,
                     films,
                     genres,
                     films_genres,
                     users,
                     films_users_who_liked,
                     friendship_status,
                     friendship;

CREATE TABLE IF NOT EXISTS mpa_ratings (
  mpa_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  mpa_name varchar(10) UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
  film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  title varchar(50) NOT NULL CHECK (length(title) > 0),
  description varchar(200),
  release_date date NOT NULL CHECK (release_date > '1985-12-28'),
  duration int NOT NULL CHECK (duration > 0),
  mpa_id int REFERENCES mpa_ratings(mpa_id)
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  genre_name varchar(20)
);

CREATE TABLE IF NOT EXISTS films_genres (
  film_id int REFERENCES films(film_id),
  genre_id int REFERENCES genres(genre_id),
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  user_name varchar(50),
  login varchar(50) NOT NULL CHECK (locate(login,' ') = 0),
  email varchar(50) NOT NULL CHECK (length(email) > 0 AND locate(email,'@') != 0),
  birthday date NOT NULL CHECK (birthday <= now())
);

CREATE TABLE IF NOT EXISTS films_users_who_liked (
  film_id int REFERENCES films(film_id),
  user_id int REFERENCES users(user_id),
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friendship_status (
  status_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  status_name varchar(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS friendship (
  user1_id int REFERENCES users(user_id),
  user2_id int REFERENCES users(user_id),
  status_id int REFERENCES friendship_status(status_id),
  PRIMARY KEY (user1_id, user2_id)
);