package ru.tech.db.forum.thread.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.thread.repository.custom.CustomThreadRepository;

@Repository
public interface ThreadRepository extends CrudRepository<Thread, Long>, CustomThreadRepository {
    Thread findThreadBySlug(String slug);
    Thread findThreadById(long id);
}
