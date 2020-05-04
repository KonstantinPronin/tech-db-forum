package ru.tech.db.forum.forum.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.forum.model.Forum;

public interface CustomForumRepository {
    @Transactional
    Forum create(Forum forum);
}
