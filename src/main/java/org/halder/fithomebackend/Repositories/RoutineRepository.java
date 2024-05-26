package org.halder.fithomebackend.Repositories;

import org.halder.fithomebackend.Entities.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByCreator_Id(Long userId);

    List<Routine> findByIsPublicTrue();
}