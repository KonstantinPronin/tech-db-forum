package ru.tech.db.forum.thread.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.thread.repository.custom.CustomThreadRepository;

@Repository
public interface ThreadRepository extends CrudRepository<Thread, Long>, CustomThreadRepository {
    @Query("select t from thread t where lower(t.slug) = lower(?1)")
    Thread findThreadBySlug(String slug);
    Thread findThreadById(long id);
}
