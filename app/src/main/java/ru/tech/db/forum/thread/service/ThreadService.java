package ru.tech.db.forum.thread.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.tech.db.forum.exception.model.NotFoundException;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.thread.repository.ThreadRepository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@Service
public class ThreadService {
  private ThreadRepository repository;

  public ThreadService(ThreadRepository repository) {
    this.repository = repository;
  }

  public List<Thread> createThread(Thread thread) {
    List<Thread> threadList = new ArrayList<>();
    threadList.add(thread);

    try {
      repository.create(thread);
    } catch (DataIntegrityViolationException ex) {
      PSQLException sqlEx = ((PSQLException) ex.getCause().getCause());
      String state = sqlEx.getSQLState();

      if (FOREIGN_KEY_VIOLATION.getState().equals(state)) {
        throw new NotFoundException(
            String.format("Can't find forum %s or user %s", thread.getForum(), thread.getAuthor()));
      }
      if (UNIQUE_VIOLATION.getState().equals(state)) {
        threadList.add(repository.findThreadBySlug(thread.getSlug()));
      }

      throw ex;
    }

    return threadList;
  }

  public List<Thread> getThreads(
          String slug, ZonedDateTime since, int limit, boolean desc) {
    return repository.findThreadsBySlugSinceCreatedWithLimitOrderByCreatedAsc(slug, since, limit);
  }
}
