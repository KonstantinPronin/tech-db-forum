package ru.tech.db.forum.user.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.tech.db.forum.exception.model.NotFoundException;
import ru.tech.db.forum.user.model.User;
import ru.tech.db.forum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
  private UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public List<User> createUser(User user) {
    try {
      repository.create(user);
    } catch (DataIntegrityViolationException ex) {
      return repository.findUsersByNicknameOrEmail(user.getNickname(), user.getEmail());
    }
    return new ArrayList<>();
  }

  public User getUserByName(String nickname) {
    User user = repository.findUserByNickname(nickname);

    if (user == null) {
      throw new NotFoundException(String.format("Can't find user %s", nickname));
    }

    return user;
  }

  public User updateUser(User user) {
    User stored = repository.findUserByNickname(user.getNickname());
    if (stored == null) {
      throw new NotFoundException(String.format("Can't find user %s", user.getNickname()));
    }

    if (user.getFullname() != null) {
      stored.setFullname(user.getFullname());
    }
    if (user.getAbout() != null) {
      stored.setAbout(user.getAbout());
    }
    if (user.getEmail() != null) {
      stored.setEmail(user.getEmail());
    }

    return repository.update(stored);
  }

  public List<User> getForumUsers(String slug, String since, Integer limit, Boolean desc) {
     return repository.getForumUsers(slug, since, limit, desc);
  }
}
