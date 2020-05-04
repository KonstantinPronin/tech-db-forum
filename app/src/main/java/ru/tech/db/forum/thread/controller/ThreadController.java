package ru.tech.db.forum.thread.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.thread.service.ThreadService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ThreadController {
  private ThreadService service;

  public ThreadController(ThreadService service) {
    this.service = service;
  }

  @PostMapping("/forum/{slug}/create")
  public ResponseEntity createThread(
      @RequestBody Thread thread, @PathVariable(name = "slug") String slug) {
    thread.setForum(slug);
    List<Thread> threadList = service.createThread(thread);

    if (threadList.size() == 1) {
      return ResponseEntity.status(HttpStatus.CREATED).body(threadList.get(0));
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(threadList.get(1));
  }

  @GetMapping("/forum/{slug}/threads")
  public ResponseEntity getThreads(
      @PathVariable(name = "slug") String slug,
      @RequestParam(required = false) int limit,
      @RequestParam(required = false) String since,
      @RequestParam(required = false) boolean desc) {
    ZonedDateTime zdt = ZonedDateTime.parse(since);
    List<Thread> threadList = service.getThreads(slug, zdt, limit, desc);
    return ResponseEntity.status(HttpStatus.OK).body(threadList);
  }
}
