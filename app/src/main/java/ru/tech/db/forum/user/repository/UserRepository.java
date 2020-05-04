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
  List<User> findUsersByNicknameOrEmail(String nickname, String email);

  User findUserByNickname(String nickName);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query(
      "update user set fullname=:#{#user?.getFullname()}, about=:#{#user?.getAbout()}, email=:#{#user?.getEmail()} "
          + "where nickname=:#{#user?.getNickname()}")
  int update(@Param("user") User user);

//  @Procedure(name = "getForumUsers")
//  List<User> getForumUsers(
//      @Param("slug") String slug,
//      @Param("since") String since,
//      @Param("lim") Integer limit,
//      @Param("d") Boolean desc);
}
