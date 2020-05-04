package ru.tech.db.forum.thread.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.thread.model.Thread;

import java.time.ZonedDateTime;
import java.util.List;

public interface CustomThreadRepository {
    @Transactional
    Thread create(Thread thread);
    List<Thread> findThreadsBySlugSinceCreatedWithLimitOrderByCreatedAsc(String slug, ZonedDateTime since, int limit);
}
