package ru.tech.db.forum.forum.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.tech.db.forum.exception.model.NotFoundException;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.forum.repository.ForumRepository;
import ru.tech.db.forum.user.model.User;
import ru.tech.db.forum.user.repository.UserRepository;

import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@Service
public class ForumService {
  private ForumRepository repository;
  private UserRepository userRepository;

  public ForumService(ForumRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  public Forum createForum(Forum forum) {
    User user = userRepository.findUserByNickname(forum.getUser());

    if (user == null) {
      throw new NotFoundException(String.format("Can't find user %s", forum.getUser()));
    }
    forum.setUser(user.getNickname());

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

    return null;
  }

  public Forum getForum(String slug) {
    Forum forum = repository.findForumBySlug(slug);

    if (forum == null) {
        throw new NotFoundException(String.format("Can't find forum %s", slug));
    }

    return forum;
  }
}
