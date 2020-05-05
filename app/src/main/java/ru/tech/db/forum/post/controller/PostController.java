package ru.tech.db.forum.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tech.db.forum.post.model.FullPost;
import ru.tech.db.forum.post.model.Post;
import ru.tech.db.forum.post.service.PostService;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.thread.service.ThreadService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
  private PostService service;
  private ThreadService threadService;

  public PostController(PostService service, ThreadService threadService) {
    this.service = service;
    this.threadService = threadService;
  }

  @PostMapping("/thread/{slug_or_id}/create")
  public ResponseEntity createPost(
      @RequestBody List<Post> post, @PathVariable(name = "slug_or_id") String slugOrId) {
    Thread thread;

    try {
      long id = Long.parseLong(slugOrId);
      thread = threadService.getThread(id);
    } catch (NumberFormatException ex) {
      thread = threadService.getThread(slugOrId);
    }

    post = service.createPosts(thread, post);

    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }

  @GetMapping("/post/{id}/details")
  public ResponseEntity getPost(
      @PathVariable(name = "id") Long id, @RequestParam(required = false) List<String> related) {
      FullPost post = service.getFullPost(id, related);

      return ResponseEntity.status(HttpStatus.OK).body(post);
  }

  @PostMapping("/post/{id}/details")
  public ResponseEntity updatePost(@RequestBody Post post, @PathVariable(name = "id") Long id) {
    post.setId(id);

    post = service.updatePost(post);

    return ResponseEntity.status(HttpStatus.OK).body(post);
  }

  @GetMapping("/thread/{slug_or_id}/posts")
  public ResponseEntity getThreadPosts(
      @PathVariable(name = "slug_or_id") String slugOrId,
      @RequestParam(required = false) Integer limit,
      @RequestParam(required = false) Long since,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) Boolean desc) {
    Thread thread;

    try {
      long id = Long.parseLong(slugOrId);
      thread = threadService.getThread(id);
    } catch (NumberFormatException ex) {
      thread = threadService.getThread(slugOrId);
    }

    List<Post> postList;
    if (sort == null) {
      sort = "";
    }
    switch (sort) {
      case "tree":
        postList = service.getThreadPostTree(thread.getId(), limit, since, desc);
        break;
      case "parent_tree":
        postList = service.getThreadPostParentTree(thread.getId(), limit, since, desc);
        break;
      default:
        postList = service.getThreadPostFlat(thread.getId(), limit, since, desc);
    }

    return ResponseEntity.status(HttpStatus.OK).body(postList);
  }
}
