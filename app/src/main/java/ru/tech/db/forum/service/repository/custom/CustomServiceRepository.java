package ru.tech.db.forum.service.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.service.model.Status;

import java.util.Map;

public interface CustomServiceRepository {
  @Transactional
  void clear();
  Status getStatus();
}
