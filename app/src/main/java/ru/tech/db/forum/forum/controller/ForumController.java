package ru.tech.db.forum.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.forum.service.ForumService;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
  private ForumService service;

  public ForumController(ForumService service) {
    this.service = service;
  }

  @PostMapping("/create")
  public ResponseEntity createForum(@RequestBody Forum forum) {
      Forum existingForum = service.createForum(forum);

      if (existingForum == null) {
          return ResponseEntity.status(HttpStatus.CREATED).body(forum);
      }

      return ResponseEntity.status(HttpStatus.CONFLICT).body(existingForum);
  }

  @GetMapping("/{slug}/details")
  public ResponseEntity getForum(@PathVariable(name = "slug") String slug) {
      Forum forum = service.getForum(slug);
      return ResponseEntity.status(HttpStatus.OK).body(forum);
  }
}
