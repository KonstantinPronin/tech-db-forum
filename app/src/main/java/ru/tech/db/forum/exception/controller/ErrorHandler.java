package ru.tech.db.forum.exception.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tech.db.forum.exception.model.NotFoundException;


import java.util.HashMap;
import java.util.Map;

import static ru.tech.db.forum.constants.Constants.MESSAGE_KEY;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleConflict(DataIntegrityViolationException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put(MESSAGE_KEY, ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleConflict(NotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put(MESSAGE_KEY, ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }
}
