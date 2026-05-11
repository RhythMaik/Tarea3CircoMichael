package com.luisdbb.tarea3AD2024base.repositorios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Incidencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

@Repository
public class IncidenciaRepository {

    @Autowired
    private EntityManagerFactory emf;

    public void save(Incidencia incidencia) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(incidencia);
        em.getTransaction().commit();
        em.close();
    }

    public Incidencia findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Incidencia i = em.find(Incidencia.class, id);
        em.close();
        return i;
    }

    public void update(Incidencia incidencia) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(incidencia);
        em.getTransaction().commit();
        em.close();
    }

    public List<Incidencia> findAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Incidencia> q = em.createQuery("SELECT i FROM Incidencia i", Incidencia.class);
        List<Incidencia> lista = q.getResultList();
        em.close();
        return lista;
    }
}
