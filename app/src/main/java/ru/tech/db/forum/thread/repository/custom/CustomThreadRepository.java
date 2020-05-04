package ru.tech.db.forum.thread.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.thread.model.Thread;

import java.time.ZonedDateTime;
import java.util.List;

public interface CustomThreadRepository {
    @Transactional
    Thread create(Thread thread);
    @Transactional
    Thread update(Thread thread);
    List<Thread> findThreadsBySlugOrderByCreatedAsc(String slug);
    List<Thread> findThreadsBySlugOrderByCreatedDesc(String slug);
    List<Thread> findThreadsBySlugWithLimitOrderByCreatedAsc(String slug, int limit);
    List<Thread> findThreadsBySlugWithLimitOrderByCreatedDesc(String slug, int limit);
    List<Thread> findThreadsBySlugSinceCreatedOrderByCreatedAsc(String slug, ZonedDateTime since);
    List<Thread> findThreadsBySlugSinceCreatedOrderByCreatedDesc(String slug, ZonedDateTime since);
    List<Thread> findThreadsBySlugSinceCreatedWithLimitOrderByCreatedAsc(String slug, ZonedDateTime since, int limit);
    List<Thread> findThreadsBySlugSinceCreatedWithLimitOrderByCreatedDesc(String slug, ZonedDateTime since, int limit);
}
