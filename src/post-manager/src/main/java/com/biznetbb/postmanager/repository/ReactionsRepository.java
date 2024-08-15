package com.biznetbb.postmanager.repository;

import com.biznetbb.postmanager.models.entities.ReactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReactionsRepository  extends JpaRepository<ReactionsEntity, UUID> {

}
