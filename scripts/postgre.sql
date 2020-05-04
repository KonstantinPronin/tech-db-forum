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

create table forum.votes
(
    nickname citext not null references forum.users (nickname),
    thread   bigint not null references forum.threads (id),
    voice    smallint check ( voice in (-1, 1)),
    unique (nickname, thread)
);

create or replace function update_votes() returns trigger
    LANGUAGE plpgsql
as
$body$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        UPDATE forum.threads
        SET votes = votes + NEW.voice
        WHERE id = NEW.thread;
    ELSE
        UPDATE forum.threads
        SET votes = votes - OLD.voice + NEW.voice
        WHERE id = NEW.thread;
    END IF;
    RETURN NEW;
END;
$body$;

CREATE TRIGGER update_vote_trigger
    AFTER UPDATE OR INSERT
    ON forum.votes
    FOR EACH ROW
EXECUTE PROCEDURE update_votes();

create or replace function forum.getForumUsers(forumId character varying, lim integer, since character varying,
                                               d boolean)
    returns table
            (
                like forum.users
            )
    LANGUAGE 'plpgsql'
as
$body$
begin
    if (lim IS NOT NULL and since IS NOT NULL and d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                       and u.nickname > since
                     order by u.nickname desc
                     limit lim;
    elsif (lim IS NOT NULL and d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                     order by u.nickname desc
                     limit lim;
    elsif (since IS NOT NULL and d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                       and u.nickname > since
                     order by u.nickname desc;
    elsif (d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                     order by u.nickname desc;
    elsif (lim IS NOT NULL and since IS NOT NULL) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                       and u.nickname > since
                     order by u.nickname
                     limit lim;
    elsif (lim IS NOT NULL) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                     order by u.nickname
                     limit lim;
    elsif (since IS NOT NULL) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                       and u.nickname > since
                     order by u.nickname;
    else
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author)
                     where (t.forum = forumId or p.forum = forumId)
                     order by u.nickname;
    end if;
end
$body$;




