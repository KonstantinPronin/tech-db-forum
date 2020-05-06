package ru.tech.db.forum.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tech.db.forum.forum.service.ForumService;
import ru.tech.db.forum.user.model.User;
import ru.tech.db.forum.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {
  private UserService service;
  private ForumService forumService;

  public UserController(UserService service, ForumService forumService) {
    this.service = service;
    this.forumService = forumService;
  }

  @PostMapping("/user/{nickname}/create")
  public ResponseEntity createUser(
      @RequestBody User user, @PathVariable("nickname") String nickname) {
    user.setNickname(nickname);
    List<User> existingUsers = service.createUser(user);

    if (existingUsers.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(existingUsers);
  }

  @GetMapping("/user/{nickname}/profile")
  public ResponseEntity getUser(@PathVariable("nickname") String nickname) {
    User user = service.getUserByName(nickname);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @PostMapping("/user/{nickname}/profile")
  public ResponseEntity updateUser(
      @RequestBody User user, @PathVariable("nickname") String nickname) {
    user.setNickname(nickname);
    user = service.updateUser(user);

    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping("/forum/{slug}/users")
  public ResponseEntity getForumUsers(
          @PathVariable(name = "slug") String slug,
          @RequestParam(required = false) Integer limit,
          @RequestParam(required = false) String since,
          @RequestParam(required = false) Boolean desc) {
    List<User> userList = service.getForumUsers(slug, since, limit, desc);

    if (userList.isEmpty()) {
      forumService.getForum(slug);
    }

    return ResponseEntity.status(HttpStatus.OK).body(userList);
  }
}
