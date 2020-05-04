package ru.tech.db.forum.vote.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.tech.db.forum.vote.model.Vote;

@Repository
public interface VoteRepository extends CrudRepository<Vote, String> {
}
