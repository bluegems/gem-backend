-- delete all tables, domains and functions
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- create tables and assign modified triggers
CREATE TABLE account (
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id UUID NOT NULL DEFAULT uuid_generate_v4 (),
    email VARCHAR(255) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users (
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id SERIAL NOT NULL,
    tag VARCHAR(5) NOT NULL,
    username VARCHAR(20) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20),
    bio TEXT,
    birthdate DATE,
    profile_picture TEXT,
    account_id UUID NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    CONSTRAINT username_tag_unique UNIQUE (username, tag)
);

CREATE TABLE post (
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    post_id SERIAL NOT NULL,
    user_id INTEGER NOT NULL,
    description TEXT,
    image TEXT,
    PRIMARY KEY (post_id),
    CONSTRAINT check_null CHECK (description IS NOT NULL OR image IS NOT NULL),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comment (
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment_id SERIAL NOT NULL,
    user_id INTEGER NOT NULL,
    post_id INTEGER NOT NULL,
    text TEXT,
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE post_like (
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    post_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (post_id, user_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE comment_like (
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (comment_id, user_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_id FOREIGN KEY (comment_id) REFERENCES comment(comment_id) ON DELETE CASCADE
);
