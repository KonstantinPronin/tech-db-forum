package ru.tech.db.forum.user.repository.custom;

import ru.tech.db.forum.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(User user) {
        em.persist(user);
        return user;
    }
}
