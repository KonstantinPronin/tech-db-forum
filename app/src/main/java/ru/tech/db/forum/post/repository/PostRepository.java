package ru.tech.db.forum.post.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.post.model.Post;
import ru.tech.db.forum.post.repository.custom.CustomPostRepository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long>, CustomPostRepository {
    Post findPostById(Long id);
}
