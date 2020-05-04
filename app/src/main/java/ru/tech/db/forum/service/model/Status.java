package ru.tech.db.forum.service.model;

import java.math.BigInteger;

public class Status {
  private long forum;
  private long thread;
  private long post;
  private long user;

  public Status(Object[] value) {
    this.forum = ((BigInteger)value[0]).longValue();
    this.thread = ((BigInteger)value[1]).longValue();
    this.post = ((BigInteger)value[2]).longValue();
    this.user = ((BigInteger)value[3]).longValue();
  }

  public long getForum() {
    return forum;
  }

  public long getThread() {
    return thread;
  }

  public long getPost() {
    return post;
  }

  public long getUser() {
    return user;
  }
}
