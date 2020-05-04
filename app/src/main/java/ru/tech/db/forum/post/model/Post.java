package ru.tech.db.forum.post.model;

import javax.persistence.*;

@Entity(name = "post")
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "author")
    private String author;

    @Column(name = "message")
    private String message;

    @Column(name = "forum")
    private String forum;

    @Column(name = "thread")
    private long thread;

    @Column(name = "parent")
    private int parent;

    @Column(name = "isEdited")
    private boolean isEdited;

    public Post() {
    }

    public Post(String slug, String author, String message, String forum, long thread, int parent, boolean isEdited) {
        this.slug = slug;
        this.author = author;
        this.message = message;
        this.forum = forum;
        this.thread = thread;
        this.parent = parent;
        this.isEdited = isEdited;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
