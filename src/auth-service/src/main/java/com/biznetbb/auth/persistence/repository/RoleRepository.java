package com.biznetbb.auth.persistence.repository;

import com.biznetbb.auth.persistence.entity.RoleEntity;
import com.biznetbb.auth.service.utils.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByRoleType(RoleType roleType);
}