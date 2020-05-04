-- Database: tech_db

-- DROP DATABASE tech_db;

CREATE DATABASE tech_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- SCHEMA: forum

-- DROP SCHEMA forum ;

CREATE SCHEMA forum
    AUTHORIZATION postgres;

CREATE EXTENSION IF NOT EXISTS CITEXT;

create table forum.users
(
    nickname citext primary key,
    fullname text        not null,
    email    text unique not null,
    about    text
);

create table forum.forums
(
    slug    citext primary key,
    title   text   not null,
    "user"  citext not null references forum.users (nickname),
    threads integer default 0,
    posts   bigint  default 0
);

create table forum.threads
(
    id      bigserial primary key,
    slug    citext unique,
    author  citext not null references forum.users (nickname),
    created timestamptz default current_timestamp,
    forum   citext not null references forum.forums (slug),
    title   text   not null,
    message text   not null,
    votes   integer     default 0
);

create table forum.posts
(
    id       bigserial primary key,
    author   citext not null references forum.users (nickname),
    created  timestamptz default current_timestamp,
    message  text   not null,
    forum    citext not null references forum.forums (slug),
    thread   bigint not null references forum.threads (id),
    parent   integer     default 0,
    isEdited boolean     default false
);