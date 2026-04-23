package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Artista;

/**
 * Repositorio JPA para la entidad Artista.
 *
 * Proporciona las operaciones básicas de acceso a datos gracias a
 * JpaRepository, como guardar, buscar, actualizar y eliminar artistas.
 *
 * Además, incluye métodos personalizados para consultar artistas utilizando el
 * identificador de la persona asociada. También incorpora una consulta con JOIN
 * FETCH para cargar las especialidades del artista y evitar problemas de carga
 * perezosa.
 *
 * Este repositorio se utiliza en: - Gestión de artistas - Asignación de
 * artistas a números - Visualización de la ficha del artista
 *
 * Forma parte de la capa de acceso a datos del sistema.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Integer> {

	/**
	 * Busca un artista utilizando el id de la persona asociada.
	 *
	 * @param idPersona identificador de la persona
	 * @return artista encontrado o null si no existe
	 */
	Artista findByIdPersona(Integer idPersona);

	/**
	 * Comprueba si existe un artista con el id de persona indicado.
	 *
	 * @param idPersona identificador de la persona
	 * @return true si existe, false si no existe
	 */
	boolean existsByIdPersona(Integer idPersona);

	/**
	 * Recupera un artista junto con sus especialidades utilizando JOIN FETCH. Esto
	 * permite cargar la lista de especialidades en la misma consulta, evitando
	 * errores de carga perezosa fuera del contexto de persistencia.
	 *
	 * @param idPersona identificador de la persona asociada
	 * @return artista con especialidades cargadas o null si no existe
	 */
	@Query("SELECT a FROM Artista a LEFT JOIN FETCH a.especialidades WHERE a.persona.id = :idPersona")
	Artista findByIdPersonaConEspecialidades(Integer idPersona);
}
