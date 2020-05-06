package ru.tech.db.forum.thread.repository.custom;

import ru.tech.db.forum.thread.model.Thread;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.List;

public class CustomThreadRepositoryImpl implements CustomThreadRepository {
  @PersistenceContext private EntityManager em;

  @Override
  public void clearCache() {
    em.clear();
  }

  @Override
  public Thread create(Thread thread) {
    em.persist(thread);
    return thread;
  }

  @Override
  public Thread update(Thread thread) {
    em.merge(thread);
    return thread;
  }

  @Override
  public List<Thread> findThreadsBySlugOrderByCreatedAsc(String slug) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) order by t.created asc",
            Thread.class)
            .setParameter(1, slug)
            .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugOrderByCreatedDesc(String slug) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = (?1) order by t.created desc",
            Thread.class)
            .setParameter(1, slug)
            .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugWithLimitOrderByCreatedAsc(String slug, int limit) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) order by t.created asc",
            Thread.class)
            .setParameter(1, slug)
            .setMaxResults(limit)
            .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugWithLimitOrderByCreatedDesc(String slug, int limit) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) order by t.created desc",
            Thread.class)
            .setParameter(1, slug)
            .setMaxResults(limit)
            .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugSinceCreatedOrderByCreatedAsc(
      String slug, ZonedDateTime since) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) and t.created >= ?2 order by t.created asc",
            Thread.class)
            .setParameter(1, slug)
            .setParameter(2, since)
            .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugSinceCreatedOrderByCreatedDesc(
      String slug, ZonedDateTime since) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) and t.created <= ?2 order by t.created desc",
            Thread.class)
            .setParameter(1, slug)
            .setParameter(2, since)
            .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugSinceCreatedWithLimitOrderByCreatedAsc(
      String slug, ZonedDateTime since, int limit) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) and t.created >= ?2 order by t.created asc",
            Thread.class)
        .setParameter(1, slug)
        .setParameter(2, since)
        .setMaxResults(limit)
        .getResultList();
  }

  @Override
  public List<Thread> findThreadsBySlugSinceCreatedWithLimitOrderByCreatedDesc(
      String slug, ZonedDateTime since, int limit) {
    return em.createQuery(
            "select t from thread t where lower(t.forum) = lower(?1) and t.created <= ?2 order by t.created desc",
            Thread.class)
            .setParameter(1, slug)
            .setParameter(2, since)
            .setMaxResults(limit)
            .getResultList();
  }
}
