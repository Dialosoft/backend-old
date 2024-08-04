package com.biznetbb.postmanager.repository;

import com.biznetbb.postmanager.models.entities.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentsRepository extends JpaRepository<CommentsEntity, UUID> {
}
