package ru.tech.db.forum.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity(name = "post")
@Table(name = "posts")
@TypeDef(name = "int-array", typeClass = IntArrayType.class)
@NamedStoredProcedureQueries({
  @NamedStoredProcedureQuery(
      name = "getThreadPostFlat",
      procedureName = "forum.getThreadPostFlat",
      resultClasses = Post.class,
      parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "threadId", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "lim", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "since", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "d", type = Boolean.class),
      }),
  @NamedStoredProcedureQuery(
      name = "getThreadPostTree",
      procedureName = "forum.getThreadPostTree",
      resultClasses = Post.class,
      parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "threadId", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "lim", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "since", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "d", type = Boolean.class),
      }),
  @NamedStoredProcedureQuery(
      name = "getThreadPostParentTree",
      procedureName = "forum.getThreadPostParentTree",
      resultClasses = Post.class,
      parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "threadId", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "lim", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "since", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "d", type = Boolean.class),
      })
})
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "author")
  private String author;

  @Column(name = "created")
  private ZonedDateTime created;

  @Column(name = "message")
  private String message;

  @Column(name = "forum")
  private String forum;

  @Column(name = "thread")
  private long thread;

  @Column(name = "isedited")
  private boolean isEdited;

  @Column(name = "parent")
  private long parent;

  @JsonIgnore
  @Type(type = "int-array")
  @Column(name = "path")
  private int[] path;

  @JsonIgnore
  @Column(name = "children")
  private int children;

  public Post() {}

  public Post(
      String author,
      ZonedDateTime created,
      String message,
      String forum,
      long thread,
      boolean isEdited,
      long parent,
      int[] path,
      int children) {
    this.author = author;
    this.created = created;
    this.message = message;
    this.forum = forum;
    this.thread = thread;
    this.isEdited = isEdited;
    this.parent = parent;
    this.path = path;
    this.children = children;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public ZonedDateTime getCreated() {
    return created;
  }

  public void setCreated(ZonedDateTime created) {
    this.created = created;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getForum() {
    return forum;
  }

  public void setForum(String forum) {
    this.forum = forum;
  }

  public long getThread() {
    return thread;
  }

  public void setThread(long thread) {
    this.thread = thread;
  }

  @JsonProperty("isEdited")
  public boolean isEdited() {
    return isEdited;
  }

  public void setEdited(boolean edited) {
    isEdited = edited;
  }

  public long getParent() {
    return parent;
  }

  public void setParent(long parent) {
    this.parent = parent;
  }

  public int[] getPath() {
    return path;
  }

  public void setPath(int[] path) {
    this.path = path;
  }

  public int getChildren() {
    return children;
  }

  public void setChildren(int children) {
    this.children = children;
  }
}
