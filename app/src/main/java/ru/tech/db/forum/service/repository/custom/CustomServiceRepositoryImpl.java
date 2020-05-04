package ru.tech.db.forum.service.repository.custom;

import org.springframework.stereotype.Repository;
import ru.tech.db.forum.service.model.Status;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class CustomServiceRepositoryImpl implements CustomServiceRepository {
  private EntityManager em;

  public CustomServiceRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  public void clear() {
    em.createNativeQuery(
            "TRUNCATE TABLE forum.users, forum.forums, forum.threads, "
                + "forum.posts, forum.votes, forum.status CASCADE")
        .executeUpdate();
    em.createNativeQuery("insert into forum.status values (0, 0, 0, 0)").executeUpdate();
  }

  @Override
  public Status getStatus() {
    Object[] result = (Object[]) em.createNativeQuery("select * from forum.status").getSingleResult();
    return new Status(result);
  }
}
