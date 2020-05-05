package ru.tech.db.forum.post.repository.custom;

import org.springframework.transaction.annotation.Transactional;
import ru.tech.db.forum.post.model.Post;

import java.util.List;

public interface CustomPostRepository {
  @Transactional
  Post create(Post post);

  @Transactional
  Post update(Post post);

  List<Post> getThreadPostFlat(Long threadId, Integer limit, Long since, Boolean desc);
  List<Post> getThreadPostTree(Long threadId, Integer limit, Long since, Boolean desc);
  List<Post> getThreadPostParentTree(Long threadId, Integer limit, Long since, Boolean desc);
}
