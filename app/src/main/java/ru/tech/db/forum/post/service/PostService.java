package ru.tech.db.forum.post.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.tech.db.forum.post.model.Post;
import ru.tech.db.forum.post.repository.PostRepository;
import ru.tech.db.forum.thread.model.Thread;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

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
        repository.create(post);
        continue;
      }

      Post parent = repository.findPostById(post.getParent());
      if (parent == null) {
        throw new DataIntegrityViolationException("Wrong parent post id");
      }

      int[] path = Arrays.copyOf(parent.getPath(), parent.getPath().length + 1);
      path[parent.getPath().length] = parent.getChildren() + 1;
      post.setPath(path);

      repository.create(post);
    }

    return postList;
  }

  public List<Post> getThreadPostFlat(Long threadId, Integer limit, Long since, Boolean desc) {
    return repository.getThreadPostFlat(threadId, limit, since, desc);
  }

  public List<Post> getThreadPostTree(Long threadId, Integer limit, Long since, Boolean desc) {
    return repository.getThreadPostTree(threadId, limit, since, desc);
  }

  public List<Post> getThreadPostParentTree(Long threadId, Integer limit, Long since, Boolean desc) {
    return repository.getThreadPostParentTree(threadId, limit, since, desc);
  }
}
