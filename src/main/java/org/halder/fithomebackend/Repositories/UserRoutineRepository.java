package org.halder.fithomebackend.Repositories;

import org.halder.fithomebackend.Entities.UserRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoutineRepository extends JpaRepository<UserRoutine, Long> {
    void deleteByUserIdAndRoutineId(Long userId, Long routineId);
    UserRoutine findByUserIdAndRoutineId(Long userId, Long routineId);
}