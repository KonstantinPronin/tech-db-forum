package ru.tech.db.forum.user.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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
    return repository.findUserByNickname(nickname);
  }

  public User updateUser(User user) {
    int rowsEffected = repository.update(user);
    if (rowsEffected == 0) {
      return null;
    }

    return user;
  }
}
