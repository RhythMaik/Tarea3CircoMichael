package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Persona;

/**
 * Repositorio JPA para la entidad Persona.
 *
 * Proporciona las operaciones básicas de acceso a datos gracias a
 * JpaRepository, como guardar, buscar, actualizar y eliminar personas.
 *
 * Además, incluye métodos personalizados para consultar personas por email o
 * por nombre. Esto permite validar restricciones importantes como la unicidad
 * del correo electrónico.
 *
 * Este repositorio se utiliza en: - Registro de personas - Gestión de datos
 * personales - Login y autenticación
 *
 * Forma parte de la capa de acceso a datos del sistema y actúa como punto
 * principal para recuperar información de personas desde la base de datos.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

	/**
	 * Comprueba si existe una persona con el email indicado.
	 *
	 * @param email correo electrónico a verificar
	 * @return true si existe, false si no existe
	 */
	boolean existsByEmail(String email);

	/**
	 * Busca una persona utilizando su email.
	 *
	 * @param email correo electrónico
	 * @return persona encontrada o null si no existe
	 */
	Persona findByEmail(String email);

	/**
	 * Busca una persona por su nombre exacto.
	 *
	 * @param nombre nombre completo de la persona
	 * @return persona encontrada o null si no existe
	 */
	Persona findByNombre(String nombre);
}
