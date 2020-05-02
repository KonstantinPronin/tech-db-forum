package ru.tech.db.forum.user.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.user.model.User;

public interface CustomUserRepository {
    @Transactional
    User create(User user);
}
