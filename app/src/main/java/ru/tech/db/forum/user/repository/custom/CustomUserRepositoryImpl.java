package ru.tech.db.forum.user.repository.custom;

import ru.tech.db.forum.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

public class CustomUserRepositoryImpl implements CustomUserRepository {
  @PersistenceContext private EntityManager em;

  @Override
  public User create(User user) {
    em.persist(user);
    return user;
  }

  @Override
  public List<User> getForumUsers(String slug, String since, Integer limit, Boolean desc) {
    StoredProcedureQuery q = em.createNamedStoredProcedureQuery("getForumUsers");
    q.setParameter("forumId", slug);
    q.setParameter("since", since);
    q.setParameter("lim", limit);
    q.setParameter("d", desc);
    return q.getResultList();
  }
}
