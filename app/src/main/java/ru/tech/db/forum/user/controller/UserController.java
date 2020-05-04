package ru.tech.db.forum.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tech.db.forum.user.model.User;
import ru.tech.db.forum.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {
  private UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping("/{nickname}/create")
  public ResponseEntity createUser(
      @RequestBody User user, @PathVariable("nickname") String nickname) {
    user.setNickname(nickname);
    List<User> existingUsers = service.createUser(user);

    if (existingUsers.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(existingUsers);
  }

  @GetMapping("/{nickname}/profile")
  public ResponseEntity getUser(@PathVariable("nickname") String nickname) {
    User user = service.getUserByName(nickname);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @PostMapping("/{nickname}/profile")
  public ResponseEntity updateUser(
      @RequestBody User user, @PathVariable("nickname") String nickname) {
    user.setNickname(nickname);
    user = service.updateUser(user);

    return ResponseEntity.status(HttpStatus.OK).body(user);
  }
}
