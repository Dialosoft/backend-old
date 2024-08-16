package com.dialosoft.postmanager.repository;

import com.dialosoft.postmanager.models.entities.ReactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReactionsRepository  extends JpaRepository<ReactionsEntity, UUID> {

}
