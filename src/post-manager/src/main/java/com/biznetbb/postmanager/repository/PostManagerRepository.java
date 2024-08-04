package com.biznetbb.postmanager.repository;

import com.biznetbb.postmanager.models.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface PostManagerRepository extends JpaRepository<PostEntity, UUID> {
    // remeber using optinal for the methods of this repository
}
