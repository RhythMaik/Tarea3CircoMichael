package com.luisdbb.tarea3AD2024base.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectDBConfig {

    @Bean(name = "objectdbEntityManagerFactory")
    public EntityManagerFactory objectdbEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("objectdb-pu");
    }
}
