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

create table forum.users (
	nickname citext unique primary key,
	fullname text not null,
	email text unique not null,
	about text
);

