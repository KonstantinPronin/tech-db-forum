package ru.tech.db.forum.forum.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "forum")
@Table(name = "forums")
public class Forum {
  @Id
  @Column(name = "slug")
  private String slug;

  @Column(name = "title")
  private String title;

  @Column(name = "\"user\"")
  private String user;

  @Column(name = "threads")
  private int threads;

  @Column(name = "posts")
  private long posts;

  public Forum() {}

  public Forum(String slug, String title, String user, int threads, long posts) {
    this.slug = slug;
    this.title = title;
    this.user = user;
    this.threads = threads;
    this.posts = posts;
  }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }
}
