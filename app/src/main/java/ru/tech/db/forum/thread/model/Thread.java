package ru.tech.db.forum.thread.model;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity(name = "thread")
@Table(name = "threads")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "author")
    private String author;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "forum")
    private String forum;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "votes")
    private int votes;

    public Thread() {
    }

    public Thread(String slug, String author, ZonedDateTime created, String forum, String title, String message, int votes) {
        this.slug = slug;
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.title = title;
        this.message = message;
        this.votes = votes;
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
