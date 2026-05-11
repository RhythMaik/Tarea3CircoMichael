package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.ResolucionIncidencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Repository
public class ResolucionIncidenciaRepository {

    @Autowired
    private EntityManagerFactory emf;

    public void save(ResolucionIncidencia resolucion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(resolucion);
        em.getTransaction().commit();
        em.close();
    }
}
