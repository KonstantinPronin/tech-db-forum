package ru.tech.db.forum.post.service;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.tech.db.forum.exception.model.NotFoundException;
import ru.tech.db.forum.post.model.FullPost;
import ru.tech.db.forum.post.model.Post;
import ru.tech.db.forum.post.repository.PostRepository;
import ru.tech.db.forum.thread.model.Thread;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;

@Service
public class PostService {
  private PostRepository repository;

  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public List<Post> createPosts(Thread thread, List<Post> postList) {
    ZonedDateTime zdt = ZonedDateTime.now();
    for (Post post : postList) {
      post.setThread(thread.getId());
      post.setForum(thread.getForum());
      post.setCreated(zdt);

      if (post.getParent() == 0) {
        createPost(post);
        continue;
      }

      repository.clearCache();
      Post parent = repository.findPostByIdAndThread(post.getParent(), post.getThread());
      if (parent == null) {
        throw new DataIntegrityViolationException("Wrong parent post id");
      }

      int[] path = Arrays.copyOf(parent.getPath(), parent.getPath().length + 1);
      path[parent.getPath().length] = parent.getChildren() + 1;
      post.setPath(path);

      createPost(post);
    }

    return postList;
  }

  private void createPost(Post post) {
    try {
      repository.create(post);
    } catch (DataIntegrityViolationException ex) {
      PSQLException sqlEx = ((PSQLException) ex.getCause().getCause());
      String state = sqlEx.getSQLState();
      if (FOREIGN_KEY_VIOLATION.getState().equals(state)) {
        throw new NotFoundException(String.format("Can't find user %s", post.getAuthor()));
      }

      throw ex;
    }
  }

  public Post getPost(Long id) {
    Post post = repository.findPostById(id);

    if (post == null) {
      throw new NotFoundException(String.format("Can't find post %s", id));
    }

    return post;
  }

  public FullPost getFullPost(Long id, List<String> related) {
    FullPost post = repository.getFullPost(id);
    if (post == null) {
      throw new NotFoundException(String.format("Can't find post %s", id));
    }

    FullPost filteredPost = new FullPost();
    filteredPost.setPost(post.getPost());

    if (related == null || related.isEmpty()) {
      return filteredPost;
    }

    for (String param : related) {
      switch (param) {
        case "user":
          filteredPost.setAuthor(post.getAuthor());
          break;
        case "forum":
          filteredPost.setForum(post.getForum());
          break;
        case "thread":
          filteredPost.setThread(post.getThread());
          break;
        default:
      }
    }

    return filteredPost;
  }

  public Post updatePost(Post post) {
    Post stored = repository.findPostById(post.getId());
    if (stored == null) {
      throw new NotFoundException(String.format("Can't find post %s", post.getId()));
    }

    if (post.getMessage() == null || post.getMessage().equals(stored.getMessage())) {
      return stored;
    }

    stored.setMessage(post.getMessage());
    stored.setEdited(true);
    return repository.update(stored);
  }

  public List<Post> getThreadPostFlat(Long threadId, Integer limit, Long since, Boolean desc) {
    return repository.getThreadPostFlat(threadId, limit, since, desc);
  }

  public List<Post> getThreadPostTree(Long threadId, Integer limit, Long since, Boolean desc) {
    return repository.getThreadPostTree(threadId, limit, since, desc);
  }

  public List<Post> getThreadPostParentTree(
      Long threadId, Integer limit, Long since, Boolean desc) {
    return repository.getThreadPostParentTree(threadId, limit, since, desc);
  }
}
