package com.dialosoft.postmanager.repository;

import com.dialosoft.postmanager.models.entities.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentsRepository extends JpaRepository<CommentsEntity, UUID> {
}
