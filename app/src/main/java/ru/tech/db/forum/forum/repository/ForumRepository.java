package ru.tech.db.forum.forum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.forum.repository.custom.CustomForumRepository;


@Repository
public interface ForumRepository extends CrudRepository<Forum, String>, CustomForumRepository {
    Forum findForumBySlug(String slug);
}
