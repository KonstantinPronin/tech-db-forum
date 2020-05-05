package ru.tech.db.forum.post.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.tech.db.forum.forum.model.Forum;
import ru.tech.db.forum.thread.model.Thread;
import ru.tech.db.forum.user.model.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullPost {
  private User author;
  private Forum forum;
  private Post post;
  private Thread thread;

  public FullPost() {}

  public FullPost(User author, Forum forum, Post post, Thread thread) {
    this.author = author;
    this.forum = forum;
    this.post = post;
    this.thread = thread;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Forum getForum() {
    return forum;
  }

  public void setForum(Forum forum) {
    this.forum = forum;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public Thread getThread() {
    return thread;
  }

  public void setThread(Thread thread) {
    this.thread = thread;
  }
}
