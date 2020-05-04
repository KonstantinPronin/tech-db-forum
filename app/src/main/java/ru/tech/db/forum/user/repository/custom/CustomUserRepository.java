package ru.tech.db.forum.user.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.user.model.User;

import java.util.List;

public interface CustomUserRepository {
  @Transactional
  User create(User user);
  List<User> getForumUsers(String slug, String since, Integer lim, Boolean d);
}
