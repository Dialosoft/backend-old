package com.dialosoft.auth.service.utils;

import com.dialosoft.auth.persistence.entity.RoleEntity;
import com.dialosoft.auth.persistence.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {

        if (roleRepository.findByRoleType(RoleType.USER).isEmpty()) {
            RoleEntity userRole = RoleEntity.builder()
                    .roleType(RoleType.USER)
                    .adminRole(false)
                    .modRole(false)
                    .build();
            roleRepository.save(userRole);
        }

        if (roleRepository.findByRoleType(RoleType.ADMIN).isEmpty()) {
            RoleEntity adminRole = RoleEntity.builder()
                    .roleType(RoleType.ADMIN)
                    .adminRole(true)
                    .modRole(true)
                    .build();
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByRoleType(RoleType.MOD).isEmpty()) {
            RoleEntity modRole = RoleEntity.builder()
                    .roleType(RoleType.MOD)
                    .adminRole(false)
                    .modRole(true)
                    .build();
            roleRepository.save(modRole);
        }
    }
}
