package ru.tech.db.forum.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tech.db.forum.service.model.Status;
import ru.tech.db.forum.service.service.Service;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
  private Service service;

  public ServiceController(Service service) {
    this.service = service;
  }

  @PostMapping("/clear")
  public ResponseEntity clear() {
    service.clear();
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @GetMapping("/status")
  public ResponseEntity getStatus() {
    Status status = service.getStatus();
    return ResponseEntity.status(HttpStatus.OK).body(status);
  }
}
