package ru.tech.db.forum.post.repository.custom;

import ru.tech.db.forum.post.model.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

public class CustomPostRepositoryImpl implements CustomPostRepository {
  @PersistenceContext private EntityManager em;

  @Override
  public Post create(Post post) {
    em.persist(post);
    return post;
  }

  @Override
  public Post update(Post post) {
    em.merge(post);
    return post;
  }

  @Override
  public List<Post> getThreadPostFlat(Long threadId, Integer limit, Long since, Boolean desc) {
    StoredProcedureQuery q = em.createNamedStoredProcedureQuery("getThreadPostFlat");
    q.setParameter("threadId", threadId);
    q.setParameter("lim", limit);
    q.setParameter("since", since);
    q.setParameter("d", desc);
    return q.getResultList();
  }

  @Override
  public List<Post> getThreadPostTree(Long threadId, Integer limit, Long since, Boolean desc) {
    StoredProcedureQuery q = em.createNamedStoredProcedureQuery("getThreadPostTree");
    q.setParameter("threadId", threadId);
    q.setParameter("lim", limit);
    q.setParameter("since", since);
    q.setParameter("d", desc);
    return q.getResultList();
  }

  @Override
  public List<Post> getThreadPostParentTree(Long threadId, Integer limit, Long since, Boolean desc) {
    StoredProcedureQuery q = em.createNamedStoredProcedureQuery("getThreadPostParentTree");
    q.setParameter("threadId", threadId);
    q.setParameter("lim", limit);
    q.setParameter("since", since);
    q.setParameter("d", desc);
    return q.getResultList();
  }
}
