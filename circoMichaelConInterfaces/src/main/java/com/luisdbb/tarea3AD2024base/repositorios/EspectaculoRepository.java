package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad Espectaculo.
 *
 * Proporciona las operaciones básicas de acceso a datos gracias a
 * JpaRepository, como guardar, buscar, actualizar y eliminar espectáculos.
 *
 * Además, incluye métodos personalizados para consultar espectáculos según
 * distintos criterios: por coordinador, por rango de fechas o por coincidencias
 * parciales en el nombre.
 *
 * Este repositorio se utiliza en: - Ver espectáculos - Ver un espectáculo
 * completo - Gestionar espectáculos
 *
 * Forma parte de la capa de acceso a datos del sistema y permite recuperar
 * información estructurada para la capa de servicios.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Repository
public interface EspectaculoRepository extends JpaRepository<Espectaculo, Integer> {

	/**
	 * Devuelve todos los espectáculos gestionados por un coordinador concreto.
	 *
	 * @param coordinador entidad Coordinacion asociada
	 * @return lista de espectáculos gestionados por ese coordinador
	 */
	List<Espectaculo> findByCoordinador(Coordinacion coordinador);

	/**
	 * Busca espectáculos cuya fecha de inicio esté dentro del rango indicado.
	 *
	 * @param inicio fecha inicial del rango
	 * @param fin    fecha final del rango
	 * @return lista de espectáculos cuyo inicio está dentro del rango
	 */
	List<Espectaculo> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

	/**
	 * Busca espectáculos cuyo nombre contenga el texto indicado, ignorando
	 * mayúsculas y minúsculas.
	 *
	 * @param nombre texto parcial a buscar
	 * @return lista de espectáculos cuyo nombre coincide parcialmente
	 */
	List<Espectaculo> findByNombreContainingIgnoreCase(String nombre);
}
