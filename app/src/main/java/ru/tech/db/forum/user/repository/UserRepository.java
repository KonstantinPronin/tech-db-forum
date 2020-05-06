package ru.tech.db.forum.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.user.model.User;
import ru.tech.db.forum.user.repository.custom.CustomUserRepository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String>, CustomUserRepository {
  //Java can not use citext
  @Query("select u from user u where lower(u.nickname) = lower(?1)")
  User findUserByNickname(String nickName);
  @Query("select u from user u where lower(u.nickname) = lower(?1) or lower(u.email) = lower(?2)")
  List<User> findUsersByNicknameOrEmail(String nickname, String email);
}
