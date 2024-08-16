package com.dialosoft.postmanager.repository;

import com.dialosoft.postmanager.models.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface PostManagerRepository extends JpaRepository<PostEntity, UUID> {
    // remember using optinal for the methods of this repository

    Optional<List<PostEntity>> findByUsername(String username);
}
