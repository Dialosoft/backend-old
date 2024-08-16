package com.dialosoft.auth.persistence.entity;

import com.dialosoft.auth.service.utils.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, unique = true)
    private RoleType roleType;

    @Column(nullable = false)
    private Integer permission;

    @Column(nullable = false)
    private Boolean adminRole;

    @Column(nullable = false)
    private Boolean modRole;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void initializePermission() {
        if (this.roleType != null) {
            this.permission = this.roleType.getPermissionName();
        }
    }
}
