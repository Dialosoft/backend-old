package com.dialosoft.auth.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name="seed_phrase")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeedPhraseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "phrase", nullable = false)
    private String hashPhrase;
}
