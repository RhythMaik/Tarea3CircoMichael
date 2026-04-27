package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Credenciales;

/**
 * Repositorio JPA para la entidad Credenciales.
 *
 * Proporciona las operaciones básicas de acceso a datos gracias a
 * JpaRepository, como guardar, buscar, actualizar y eliminar credenciales.
 *
 * Además, incluye métodos personalizados para consultar credenciales por nombre
 * de usuario o por el identificador de la persona asociada. Esto permite
 * validar restricciones importantes como la unicidad del nombre de usuario.
 *
 * Este repositorio se utiliza en: - Login y logout - Registro y asignación de
 * credenciales - Gestión de datos de personas y perfiles
 *
 * Forma parte de la capa de acceso a datos del sistema.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales, Integer> {

	/**
	 * Comprueba si existe un usuario con el nombre indicado.
	 *
	 * @param nombreUsuario nombre de usuario a verificar
	 * @return true si existe, false si no existe
	 */
	boolean existsByNombreUsuario(String nombreUsuario);

	/**
	 * Busca las credenciales asociadas a un nombre de usuario.
	 *
	 * @param nombreUsuario nombre de usuario
	 * @return credenciales encontradas o null si no existen
	 */
	Credenciales findByNombreUsuario(String nombreUsuario);

	/**
	 * Busca las credenciales utilizando el id de persona como clave primaria.
	 *
	 * @param idPersona identificador de la persona asociada
	 * @return credenciales encontradas o null si no existen
	 */
	Credenciales findByIdPersona(Integer idPersona);
}
