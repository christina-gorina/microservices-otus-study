DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    id               IDENTITY NOT NULL PRIMARY KEY,
    name             VARCHAR                           NOT NULL UNIQUE,
    password         VARCHAR                           NOT NULL,
    email            VARCHAR,
    first_name        VARCHAR,
    last_name         VARCHAR,
    phone            VARCHAR
);
