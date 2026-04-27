package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;

import java.util.List;

/**
 * Repositorio JPA para la entidad Numero.
 *
 * Proporciona las operaciones básicas de acceso a datos gracias a
 * JpaRepository, como guardar, buscar, actualizar y eliminar números circenses.
 *
 * Además, incluye métodos personalizados para consultar números según distintos
 * criterios: por espectáculo, por orden dentro del espectáculo o por la
 * participación de un artista concreto.
 *
 * Este repositorio se utiliza en: - Ver un espectáculo completo - Crear o
 * modificar números - Asignar artistas a números
 *
 * Forma parte de la capa de acceso a datos del sistema y permite recuperar
 * información estructurada para la capa de servicios.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Repository
public interface NumeroRepository extends JpaRepository<Numero, Integer> {

	/**
	 * Devuelve todos los números pertenecientes a un espectáculo concreto.
	 *
	 * @param espectaculo espectáculo asociado
	 * @return lista de números del espectáculo
	 */
	List<Numero> findByEspectaculo(Espectaculo espectaculo);

	/**
	 * Devuelve los números de un espectáculo ordenados por su campo "orden" de
	 * forma ascendente.
	 *
	 * @param espectaculo espectáculo asociado
	 * @return lista de números ordenados ascendentemente
	 */
	List<Numero> findByEspectaculoOrderByOrdenAsc(Espectaculo espectaculo);

	/**
	 * Busca todos los números en los que participa un artista concreto.
	 *
	 * @param artista artista participante
	 * @return lista de números en los que interviene el artista
	 */
	List<Numero> findByArtistasContains(Artista artista);
}
