DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id   INTEGER NOT NULL PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255)
);


