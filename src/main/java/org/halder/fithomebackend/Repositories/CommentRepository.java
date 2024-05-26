package org.halder.fithomebackend.Repositories;

import org.halder.fithomebackend.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRoutineId(Long routineId);
}
