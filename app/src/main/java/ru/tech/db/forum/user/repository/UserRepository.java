package ru.tech.db.forum.user.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.user.model.User;
import ru.tech.db.forum.user.repository.custom.CustomUserRepository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String>, CustomUserRepository {
  User findUserByNickname(String nickName);
  List<User> findUsersByNicknameOrEmail(String nickname, String email);
}
