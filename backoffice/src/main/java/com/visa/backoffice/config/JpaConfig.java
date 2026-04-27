package com.visa.backoffice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.visa.backoffice.repository")
@EntityScan(basePackages = "com.visa.backoffice.entity")
@EnableTransactionManagement
public class JpaConfig {
    // Configuration JPA pour éviter les erreurs de mapping
}
