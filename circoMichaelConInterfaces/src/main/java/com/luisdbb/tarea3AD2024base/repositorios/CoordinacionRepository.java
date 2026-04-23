package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;

/**
 * Repositorio JPA para la entidad Coordinacion.
 *
 * Proporciona las operaciones básicas de acceso a datos gracias a
 * JpaRepository, como guardar, buscar, actualizar y eliminar coordinadores.
 *
 * Además, incluye métodos personalizados para consultar coordinadores
 * utilizando el identificador de la persona asociada como clave primaria.
 *
 * Este repositorio se utiliza en: - Gestión de personas - Asignación de
 * coordinadores a espectáculos - Visualización de datos de espectáculos
 *
 * Forma parte de la capa de acceso a datos del sistema.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Repository
public interface CoordinacionRepository extends JpaRepository<Coordinacion, Integer> {

	/**
	 * Busca un coordinador utilizando el id de la persona asociada.
	 *
	 * @param idPersona identificador de la persona
	 * @return instancia de Coordinacion o null si no existe
	 */
	Coordinacion findByIdPersona(Integer idPersona);

	/**
	 * Comprueba si existe un coordinador asociado al id de persona indicado.
	 *
	 * @param idPersona identificador de la persona
	 * @return true si existe, false si no existe
	 */
	boolean existsByIdPersona(Integer idPersona);
}
