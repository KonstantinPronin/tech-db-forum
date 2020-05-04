package ru.tech.db.forum.forum.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.tech.db.forum.exception.model.NotFoundException;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.forum.repository.ForumRepository;

import java.util.ArrayList;
import java.util.List;

import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@Service
public class ForumService {
  private ForumRepository repository;

  public ForumService(ForumRepository repository) {
    this.repository = repository;
  }

  public Forum createForum(Forum forum) {
    try {
      repository.create(forum);
    } catch (DataIntegrityViolationException ex) {
      PSQLException sqlEx = ((PSQLException) ex.getCause().getCause());
      String state = sqlEx.getSQLState();

      if (FOREIGN_KEY_VIOLATION.getState().equals(state)) {
          throw new NotFoundException(String.format("Can't find user %s", forum.getUser()));
      }
      if (UNIQUE_VIOLATION.getState().equals(state)) {
          return repository.findForumBySlug(forum.getSlug());
      }

      throw ex;
    }

    return forum;
  }

  public Forum getForum(String slug) {
    Forum forum = repository.findForumBySlug(slug);

    if (forum == null) {
        throw new NotFoundException(String.format("Can't find forum %s", slug));
    }

    return forum;
  }
}
