package com.dialosoft.auth.persistence.repository;

import com.dialosoft.auth.persistence.entity.SeedPhraseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeedPhraseRepository extends JpaRepository<SeedPhraseEntity,String> {
    Optional<SeedPhraseEntity> findSeedPhraseEntityByUserId(UUID userId);
    Optional<SeedPhraseEntity> findByHashPhrase(String hashPhrase);
}
