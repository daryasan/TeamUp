package org.example.repositories;

import jakarta.transaction.Transactional;
import org.example.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

}
