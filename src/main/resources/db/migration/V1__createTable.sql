CREATE TABLE IF NOT EXISTS users(
    id serial NOT NULL,
    login text UNIQUE NOT NULL,
    email text UNIQUE NOT NULL,
    password text NOT NULL,
    rating bigint NOT NULL,
    CONSTRAINT Pk_id PRIMARY KEY (id)
);
