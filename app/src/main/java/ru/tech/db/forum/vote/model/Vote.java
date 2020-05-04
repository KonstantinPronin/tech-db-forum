package ru.tech.db.forum.vote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "vote")
@Table(name = "votes")
public class Vote {
  @Id
  @Column(name = "nickname")
  private String nickname;

  @Column(name = "thread")
  private long thread;

  @Column(name = "voice")
  private int voice;

  public Vote() {}

  public Vote(String nickname, long thread, int voice) {
    this.nickname = nickname;
    this.thread = thread;
    this.voice = voice;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public long getThread() {
    return thread;
  }

  public void setThread(long thread) {
    this.thread = thread;
  }

  public int getVoice() {
    return voice;
  }

  public void setVoice(int voice) {
    this.voice = voice;
  }
}
