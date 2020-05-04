package ru.tech.db.forum.forum.repository.custom;

import ru.tech.db.forum.forum.model.Forum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomForumRepositoryImpl implements CustomForumRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Forum create(Forum forum) {
        em.persist(forum);
        return forum;
    }
}
