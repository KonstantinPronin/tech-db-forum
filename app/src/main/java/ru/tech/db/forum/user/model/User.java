package ru.tech.db.forum.user.model;

import javax.persistence.*;

@Entity(name = "user")
@Table(name = "users")
public class User {
  @Id
  @Column(name = "nickname")
  private String nickname;

  @Column(name = "fullname")
  private String fullname;

  @Column(name = "about")
  private String about;

  @Column(name = "email")
  private String email;

  public User() {}

  public User(String nickname, String fullname, String about, String email) {
    this.nickname = nickname;
    this.fullname = fullname;
    this.about = about;
    this.email = email;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
