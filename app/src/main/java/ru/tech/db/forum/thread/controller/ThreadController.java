package ru.tech.db.forum.thread.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tech.db.forum.exception.model.NotFoundException;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.forum.service.ForumService;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.thread.service.ThreadService;
import ru.tech.db.forum.vote.model.Vote;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ThreadController {
  private ThreadService service;
  private ForumService forumService;

  public ThreadController(ThreadService service, ForumService forumService) {
    this.service = service;
    this.forumService = forumService;
  }

  @PostMapping("/forum/{slug}/create")
  public ResponseEntity createThread(
      @RequestBody Thread thread, @PathVariable(name = "slug") String slug) {
    Forum forum = forumService.getForum(slug);
    if (forum == null) {
        throw new NotFoundException(
                String.format("Can't find forum %s", slug));
    }

    thread.setForum(forum.getSlug());
    List<Thread> threadList = service.createThread(thread);

    if (threadList.size() == 1) {
      return ResponseEntity.status(HttpStatus.CREATED).body(threadList.get(0));
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(threadList.get(1));
  }

  @GetMapping("/forum/{slug}/threads")
  public ResponseEntity getThreads(
      @PathVariable(name = "slug") String slug,
      @RequestParam(required = false) Integer limit,
      @RequestParam(required = false) String since,
      @RequestParam(required = false) Boolean desc) {
    ZonedDateTime zdt = null;
    if (since != null) {
      zdt = ZonedDateTime.parse(since);
    }

    forumService.getForum(slug);
    List<Thread> threadList = service.getThreads(slug, zdt, limit, desc);

    return ResponseEntity.status(HttpStatus.OK).body(threadList);
  }

  @GetMapping("/thread/{slug_or_id}/details")
  public ResponseEntity getThread(@PathVariable(name = "slug_or_id") String slugOrId) {
    Thread thread;

    try {
      long id = Long.parseLong(slugOrId);
      thread = service.getThread(id);
    } catch (NumberFormatException ex) {
      thread = service.getThread(slugOrId);
    }

    return ResponseEntity.status(HttpStatus.OK).body(thread);
  }

  @PostMapping("/thread/{slug_or_id}/details")
  public ResponseEntity updateThread(
      @RequestBody Thread thread, @PathVariable(name = "slug_or_id") String slugOrId) {
    try {
      long id = Long.parseLong(slugOrId);
      thread.setId(id);
      thread = service.updateThreadById(thread);
    } catch (NumberFormatException ex) {
      thread.setSlug(slugOrId);
      thread = service.updateThreadBySlug(thread);
    }

    return ResponseEntity.status(HttpStatus.OK).body(thread);
  }

  @PostMapping("/thread/{slug_or_id}/vote")
  public ResponseEntity vote(
      @RequestBody Vote vote, @PathVariable(name = "slug_or_id") String slugOrId) {
    Thread thread;

    //TODO too many requests to db
    try {
      long id = Long.parseLong(slugOrId);
      thread = service.getThread(id);
    } catch (NumberFormatException ex) {
      thread = service.getThread(slugOrId);
    }

    vote.setThread(thread.getId());
    service.vote(vote);

    thread = service.getThread(vote.getThread());
    return ResponseEntity.status(HttpStatus.OK).body(thread);
  }
}
