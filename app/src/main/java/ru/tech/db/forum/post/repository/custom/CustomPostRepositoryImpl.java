package ru.tech.db.forum.post.repository.custom;

import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.post.model.FullPost;
import ru.tech.db.forum.post.model.Post;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
  public FullPost getFullPost(Long id) {
    FullPost post = new FullPost();
    Object[] result;
    try {
      result =
          (Object[])
              em.createQuery(
                      "select u, f, p, t from post p inner join user u on p.author = u.nickname "
                          + "inner join forum f on p.forum = f.slug inner join thread t on p.thread = t.id "
                          + "where p.id = ?1")
                  .setParameter(1, id)
                  .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }

    if (result.length < 4) {
      return null;
    }

    post.setAuthor((User) result[0]);
    post.setForum((Forum) result[1]);
    post.setPost((Post) result[2]);
    post.setThread((Thread) result[3]);
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
  public List<Post> getThreadPostParentTree(
      Long threadId, Integer limit, Long since, Boolean desc) {
    StoredProcedureQuery q = em.createNamedStoredProcedureQuery("getThreadPostParentTree");
    q.setParameter("threadId", threadId);
    q.setParameter("lim", limit);
    q.setParameter("since", since);
    q.setParameter("d", desc);
    return q.getResultList();
  }
}
