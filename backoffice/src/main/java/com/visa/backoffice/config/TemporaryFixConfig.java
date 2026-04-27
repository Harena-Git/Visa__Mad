package com.visa.backoffice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.visa.backoffice.entity.Visa;
import com.visa.backoffice.entity.Demandeur;

@Configuration
@ConditionalOnProperty(name = "app.jpa.temporary-fix", havingValue = "true", matchIfMissing = false)
public class TemporaryFixConfig {

    @Bean
    @Primary
    public JpaRepository<Visa, String> visaRepositoryFallback(EntityManager entityManager) {
        return new SimpleJpaRepository<>(Visa.class, entityManager) {
            // Implémentation minimale pour éviter les erreurs JPA
            // Cette configuration désactive temporairement les requêtes problématiques
        };
    }
}
