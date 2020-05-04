package ru.tech.db.forum.thread.repository.custom;

import ru.tech.db.forum.thread.model.Thread;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.List;

public class CustomThreadRepositoryImpl implements CustomThreadRepository {
  @PersistenceContext private EntityManager em;

  @Override
  public Thread create(Thread thread) {
    em.persist(thread);
    return thread;
  }

  @Override
  public List<Thread> findThreadsBySlugSinceCreatedWithLimitOrderByCreatedAsc(
          String slug, ZonedDateTime since, int limit) {
    return em.createQuery(
        "select t from thread t where t.forum = ?1 and t.created >= ?2 order by t.created asc",
        Thread.class).setParameter(1, slug).setParameter(2, since).setMaxResults(limit).getResultList();
  }
}
