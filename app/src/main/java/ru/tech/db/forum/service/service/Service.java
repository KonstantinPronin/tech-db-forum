package ru.tech.db.forum.service.service;

import ru.tech.db.forum.service.model.Status;
import ru.tech.db.forum.service.repository.custom.CustomServiceRepository;

@org.springframework.stereotype.Service
public class Service {
  private CustomServiceRepository repository;

  public Service(CustomServiceRepository repository) {
    this.repository = repository;
  }

  public void clear() {
    repository.clear();
  }

  public Status getStatus() {
    return repository.getStatus();
  }
}
