package org.halder.fithomebackend.Repositories;

import ch.qos.logback.classic.spi.LoggingEventVO;
import org.halder.fithomebackend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Boolean existsByName(String name);
}