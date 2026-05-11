package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.Incidencia;
import com.luisdbb.tarea3AD2024base.modelo.ResolucionIncidencia;
import com.luisdbb.tarea3AD2024base.modelo.TipoIncidencia;
import com.luisdbb.tarea3AD2024base.repositorios.IncidenciaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.ResolucionIncidenciaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

@Service
public class ServicioIncidencias {

	@Autowired
	private IncidenciaRepository repoIncidencias;

	@Autowired
	private ResolucionIncidenciaRepository repoResoluciones;

	@Autowired
	private EntityManagerFactory emf;

	/**
	 * Registra una nueva incidencia en el sistema.
	 *
	 * @param tipo           tipo de incidencia
	 * @param descripcion    descripción detallada
	 * @param usuarioReporta nombre de la persona que reporta (Sesion.nombrePersona)
	 * @param idEspectaculo  id del espectáculo afectado (puede ser null)
	 * @param idNumero       id del número afectado (puede ser null)
	 */
	public void registrarIncidencia(TipoIncidencia tipo, String descripcion, String usuarioReporta, Long idEspectaculo,
			Long idNumero) {

		Incidencia incidencia = new Incidencia(tipo, descripcion, usuarioReporta, idEspectaculo, idNumero);

		repoIncidencias.save(incidencia);
	}

	/**
	 * Registra la resolución de una incidencia y la marca como resuelta.
	 *
	 * @param idIncidencia    id de la incidencia a resolver
	 * @param acciones        acciones realizadas para resolverla
	 * @param usuarioResuelve nombre de la persona que resuelve
	 *                        (Sesion.nombrePersona)
	 */
	public void resolverIncidencia(Long idIncidencia, String acciones, String usuarioResuelve) {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Incidencia inc = em.find(Incidencia.class, idIncidencia);
		if (inc == null) {
			em.getTransaction().rollback();
			em.close();
			return;
		}

		inc.setResuelta(true);

		ResolucionIncidencia r = new ResolucionIncidencia(inc, acciones, usuarioResuelve);

		em.persist(r);
		em.merge(inc);

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Búsqueda avanzada de incidencias con filtros opcionales.
	 */
	public List<Incidencia> buscarIncidencias(TipoIncidencia tipo, Boolean resuelta, Long idEspectaculo, Long idNumero,
			LocalDateTime desde, LocalDateTime hasta) {

		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT i FROM Incidencia i WHERE 1=1";

		if (tipo != null)
			jpql += " AND i.tipo = :tipo";

		if (resuelta != null)
			jpql += " AND i.resuelta = :resuelta";

		if (idEspectaculo != null)
			jpql += " AND i.idEspectaculo = :esp";

		if (idNumero != null)
			jpql += " AND i.idNumero = :num";

		if (desde != null)
			jpql += " AND i.fechaHora >= :desde";

		if (hasta != null)
			jpql += " AND i.fechaHora <= :hasta";

		TypedQuery<Incidencia> q = em.createQuery(jpql, Incidencia.class);

		if (tipo != null)
			q.setParameter("tipo", tipo);

		if (resuelta != null)
			q.setParameter("resuelta", resuelta);

		if (idEspectaculo != null)
			q.setParameter("esp", idEspectaculo);

		if (idNumero != null)
			q.setParameter("num", idNumero);

		if (desde != null)
			q.setParameter("desde", desde);

		if (hasta != null)
			q.setParameter("hasta", hasta);

		List<Incidencia> lista = q.getResultList();
		em.close();
		return lista;
	}
}
