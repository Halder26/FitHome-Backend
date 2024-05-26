package org.halder.fithomebackend.Repositories;

import org.halder.fithomebackend.Entities.UserRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoutineRepository extends JpaRepository<UserRoutine, Long> {
    void deleteByUserIdAndRoutineId(Long userId, Long routineId);
    UserRoutine findByUserIdAndRoutineId(Long userId, Long routineId);
    void deleteAllByRoutineId(Long routineId);
}