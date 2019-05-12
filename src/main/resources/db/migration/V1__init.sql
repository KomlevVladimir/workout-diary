CREATE TABLE users (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT 1) PRIMARY KEY,
  first_name    VARCHAR(50) NOT NULL,
  last_name     VARCHAR(50) NOT NULL,
  age           INTEGER NOT NULL,
  email         VARCHAR(50) NOT NULL UNIQUE,
  password      VARCHAR(255) NOT NULL,
  is_enabled    BOOLEAN NOT NULL
);

CREATE TABLE confirmation_secrets (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT 1) PRIMARY KEY,
  secret        VARCHAR(50) NOT NULL,
  user_id       BIGINT REFERENCES users (id) NOT NULL
);

CREATE TABLE workouts (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT 1) PRIMARY KEY,
  user_id       BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
  workout_date  DATE     NOT NULL,
  description   VARCHAR(4000) NOT NULL
);