package org.example.repositories;

import org.example.models.EventStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStepRepository extends JpaRepository<EventStep, Long> {
}
