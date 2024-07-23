package com.biznetbb.auth.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.biznetbb.auth.persistence.entity.UserEntity;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
}
