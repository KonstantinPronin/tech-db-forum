-- Database: tech_db

-- DROP DATABASE tech_db;

-- CREATE DATABASE tech_db
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'C'
--     LC_CTYPE = 'C'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1;

-- SCHEMA: forum

-- DROP SCHEMA forum ;

CREATE SCHEMA forum
    AUTHORIZATION postgres;

CREATE EXTENSION IF NOT EXISTS CITEXT;
-----------------------------------------------------------------------------
create unlogged table forum.users
(
    nickname citext primary key,
    fullname text          not null,
    email    citext unique not null,
    about    text
);
-----------------------------------------------------------------------------
create unlogged table forum.forums
(
    slug    citext primary key,
    title   text   not null,
    "user"  citext not null references forum.users (nickname),
    threads integer default 0,
    posts   bigint  default 0
);
-----------------------------------------------------------------------------
create unlogged table forum.threads
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
-----------------------------------------------------------------------------
create unlogged table forum.posts
(
    id       bigserial primary key,
    author   citext not null references forum.users (nickname),
    created  timestamptz default current_timestamp,
    message  text   not null,
    forum    citext not null references forum.forums (slug),
    thread   bigint not null references forum.threads (id),
    isEdited boolean     default false,
    parent   bigint      default 0,
    path     integer[]   default array []::integer[],
    children integer     default 0
);
-----------------------------------------------------------------------------
create unlogged table forum.votes
(
    nickname citext not null references forum.users (nickname),
    thread   bigint not null references forum.threads (id),
    voice    smallint check ( voice in (-1, 1)),
    unique (nickname, thread)
);
-----------------------------------------------------------------------------
create unlogged table forum.status
(
    forum  bigint default 0,
    thread bigint default 0,
    post   bigint default 0,
    "user" bigint default 0
);

insert into forum.status
values (0, 0, 0, 0);
-----------------------------------------------------------------------------
create or replace function forum.update_forum_status() returns trigger
    LANGUAGE plpgsql
as
$body$
BEGIN
    UPDATE forum.status
    SET forum = forum + 1;
    RETURN NEW;
END;
$body$;

create trigger update_forum_status_trigger
    after insert
    on forum.forums
    for each row
execute procedure forum.update_forum_status();
-----------------------------------------------------------------------------
create or replace function forum.update_thread_status() returns trigger
    LANGUAGE plpgsql
as
$body$
BEGIN
    UPDATE forum.status
    SET thread = thread + 1;
    UPDATE forum.forums
    SET threads = threads + 1
    WHERE slug = NEW.forum;
    RETURN NEW;
END;
$body$;

create trigger update_thread_status_trigger
    after insert
    on forum.threads
    for each row
execute procedure forum.update_thread_status();
-----------------------------------------------------------------------------
create or replace function forum.update_post_status() returns trigger
    LANGUAGE plpgsql
as
$body$
BEGIN
    UPDATE forum.status
    SET post = post + 1;
    UPDATE forum.forums
    SET posts = posts + 1
    WHERE slug = NEW.forum;
    RETURN NEW;
END;
$body$;

create trigger update_post_status_trigger
    after insert
    on forum.posts
    for each row
execute procedure forum.update_post_status();
-----------------------------------------------------------------------------
create or replace function forum.update_user_status() returns trigger
    LANGUAGE plpgsql
as
$body$
BEGIN
    UPDATE forum.status
    SET "user" = "user" + 1;
    RETURN NEW;
END;
$body$;

create trigger update_user_status_trigger
    after insert
    on forum.users
    for each row
execute procedure forum.update_user_status();
-----------------------------------------------------------------------------
create or replace function forum.update_votes() returns trigger
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
EXECUTE PROCEDURE forum.update_votes();
-----------------------------------------------------------------------------
create or replace function forum.update_children() returns trigger
    LANGUAGE plpgsql
as
$body$
begin
    if new.parent != 0 then
        update forum.posts
        set children = children + 1
        where id = new.parent;
    else
        update forum.posts
        set path = array [new.id]
        where id = new.id;
    end if;
    return new;
end;
$body$;

create trigger update_children_trigger
    after insert
    on forum.posts
    for each row
execute procedure forum.update_children();
-----------------------------------------------------------------------------
create or replace function forum.getForumUsers(forumId character varying,
                                               lim integer,
                                               since character varying,
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
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                       and lower(u.nickname) < lower(since)
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname desc
                     limit lim;
    elsif (lim IS NOT NULL and d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname desc
                     limit lim;
    elsif (since IS NOT NULL and d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                       and lower(u.nickname) < lower(since)
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname desc;
    elsif (d = true) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                     order by u.nickname desc;
    elsif (lim IS NOT NULL and since IS NOT NULL) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                       and lower(u.nickname) > lower(since)
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname
                     limit lim;
    elsif (lim IS NOT NULL) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname
                     limit lim;
    elsif (since IS NOT NULL) then
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                       and lower(u.nickname) > lower(since)
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname;
    else
        return query select u.nickname, u.fullname, u.email, u.about
                     from forum.threads t
                              full join forum.posts p on (t.author = p.author)
                              join forum.users u on (u.nickname = t.author or u.nickname = p.author)
                     where (lower(t.forum) = lower(forumId) or lower(p.forum) = lower(forumId))
                     group by u.nickname, u.fullname, u.email, u.about
                     order by u.nickname;
    end if;
end
$body$;
-----------------------------------------------------------------------------
create or replace function forum.getThreadPostFlat(threadId bigint,
                                                   lim integer,
                                                   since bigint,
                                                   d boolean)
    returns table
            (
                like forum.posts
            )
    LANGUAGE 'plpgsql'
as
$body$
begin
    if (lim IS NOT NULL and since IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and p.id < since
                     order by created desc, id desc
                     limit lim;
    elsif (lim IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by created desc, id desc
                     limit lim;
    elsif (since IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and p.id < since
                     order by created desc, id desc;
    elsif (d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by created desc, id desc;
    elsif (lim IS NOT NULL and since IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and p.id > since
                     order by created, id
                     limit lim;
    elsif (lim IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by created, id
                     limit lim;
    elsif (since IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and p.id > since
                     order by created, id;
    else
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by created, id;
    end if;
end;
$body$;
-----------------------------------------------------------------------------
create or replace function forum.getThreadPostTree(threadId bigint,
                                                   lim integer,
                                                   since bigint,
                                                   d boolean)
    returns table
            (
                like forum.posts
            )
    LANGUAGE 'plpgsql'
as
$body$
declare
    sincePath forum.posts.path%TYPE;
begin
    if (since is not null) then
        select path into sincePath from forum.posts where id = since;
    end if;
    if (lim IS NOT NULL and sincePath IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and path < sincePath
                     order by path desc, id desc
                     limit lim;
    elsif (lim IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by path desc, id desc
                     limit lim;
    elsif (sincePath IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and path < sincePath
                     order by path desc, id desc;
    elsif (d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by path desc, id desc;
    elsif (lim IS NOT NULL and sincePath IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and path > sincePath
                     order by path, id
                     limit lim;
    elsif (lim IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by path, id
                     limit lim;
    elsif (sincePath IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                       and path > sincePath
                     order by path, id;
    else
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.thread = threadId
                     order by path, id;
    end if;
end;
$body$;
-----------------------------------------------------------------------------
create or replace function forum.getThreadPostParentTree(threadId bigint,
                                                         lim integer,
                                                         since bigint,
                                                         d boolean)
    returns table
            (
                like forum.posts
            )
    LANGUAGE 'plpgsql'
as
$body$
declare
    sincePath integer;
begin
    if (since is not null) then
        select path[1] into sincePath from forum.posts where id = since;
    end if;
    if (lim IS NOT NULL and sincePath IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                           and id < sincePath
                         order by id desc
                         limit lim
                     )
                     order by path[1] desc, path, id;
    elsif (lim IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                         order by id desc
                         limit lim
                     )
                     order by path[1] desc, path, id;
    elsif (sincePath IS NOT NULL and d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                           and id < sincePath
                     )
                     order by path[1] desc, path, id;
    elsif (d = true) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                     )
                     order by path[1] desc, path, id;
    elsif (lim IS NOT NULL and sincePath IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                           and id > sincePath
                         order by id
                         limit lim
                     )
                     order by path, id;
    elsif (lim IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                         order by id
                         limit lim
                     )
                     order by path, id;
    elsif (sincePath IS NOT NULL) then
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                           and id > sincePath
                     )
                     order by path, id;
    else
        return query select p.id,
                            p.author,
                            p.created,
                            p.message,
                            p.forum,
                            p.thread,
                            p.isEdited,
                            p.parent,
                            p.path,
                            p.children
                     from forum.posts p
                     where p.path[1] in (
                         select id
                         from forum.posts
                         where thread = threadId
                           and parent = 0
                     )
                     order by path, id;
    end if;
end;
$body$;
-----------------------------------------------------------------------------


