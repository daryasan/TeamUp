package org.example.repositories;

import org.example.models.UserTagMapping;
import org.example.models.UserTagKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTagMappingRepository extends JpaRepository<UserTagMapping, UserTagKey> {
}
