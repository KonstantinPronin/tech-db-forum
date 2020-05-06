package ru.tech.db.forum.forum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.forum.repository.custom.CustomForumRepository;


@Repository
public interface ForumRepository extends CrudRepository<Forum, String>, CustomForumRepository {
    @Query("select f from forum f where lower(f.slug) = lower(?1)")
    Forum findForumBySlug(String slug);
}
