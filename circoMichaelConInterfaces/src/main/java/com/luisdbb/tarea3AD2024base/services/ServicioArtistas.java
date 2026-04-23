package com.luisdbb.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.repositorios.*;

import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con artistas.
 *
 * Actúa como intermediario entre los controladores y los repositorios,
 * proporcionando operaciones de consulta, creación, modificación y eliminación
 * de artistas. También se encarga de generar la ficha completa del artista
 * según la sesión activa.
 *
 * Este servicio garantiza la carga de especialidades para evitar problemas de
 * inicialización perezosa y coordina la información relacionada con
 * participaciones en números y espectáculos.
 *
 * Casos de uso en los que participa: - CU3C: Gestión de datos de artistas -
 * CU5C: Asignación de artistas a números - CU6: Visualización de la ficha del
 * artista
 *
 * Forma parte de la capa de servicios dentro del modelo de aplicación.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Service
public class ServicioArtistas {

	/**
	 * Repositorio para acceder a datos de personas.
	 */
	@Autowired
	private PersonaRepository personaRepository;

	/**
	 * Repositorio para acceder a datos de artistas.
	 */
	@Autowired
	private ArtistaRepository artistaRepository;

	/**
	 * Repositorio para acceder a datos de números circenses.
	 */
	@Autowired
	private NumeroRepository numeroRepository;

	// ============================================================
	// BÚSQUEDAS BÁSICAS
	// ============================================================

	/**
	 * Busca un artista a partir de la persona asociada.
	 *
	 * @param persona entidad Persona asociada al artista
	 * @return artista correspondiente o null si no existe
	 */
	public Artista findByPersona(Persona persona) {
		return artistaRepository.findByIdPersona(persona.getId());
	}

	/**
	 * Comprueba si existe un artista asociado a una persona concreta.
	 *
	 * @param persona entidad Persona a verificar
	 * @return true si existe, false en caso contrario
	 */
	public boolean existsByPersona(Persona persona) {
		return artistaRepository.existsByIdPersona(persona.getId());
	}

	/**
	 * Guarda o actualiza un artista en la base de datos.
	 *
	 * @param artista entidad Artista a guardar
	 * @return artista persistido
	 */
	public Artista save(Artista artista) {
		return artistaRepository.save(artista);
	}

	/**
	 * Elimina un artista de la base de datos.
	 *
	 * @param artista entidad Artista a eliminar
	 */
	public void delete(Artista artista) {
		artistaRepository.delete(artista);
	}

	/**
	 * Obtiene todos los artistas registrados y fuerza la carga de especialidades
	 * para evitar LazyInitializationException.
	 *
	 * @return lista completa de artistas
	 */
	@Transactional(readOnly = true)
	public List<Artista> findAll() {
		List<Artista> lista = artistaRepository.findAll();

		// Fuerza la carga de especialidades
		for (Artista a : lista) {
			a.getEspecialidades().size();
		}

		return lista;
	}

	// ============================================================
	// MÉTODO PRINCIPAL DEL SERVICIO
	// ============================================================

	/**
	 * Muestra por consola la ficha completa del artista asociado a la sesión
	 * activa.
	 *
	 * La ficha incluye: - Datos personales (nombre, email, nacionalidad) - Datos
	 * profesionales (apodo, especialidades) - Participación en números y
	 * espectáculos
	 *
	 * Este método se utiliza en el caso de uso CU6.
	 *
	 * @param sesion sesión activa del usuario
	 */
	public void verFichaArtista(Sesion sesion) {

		if (sesion == null || sesion.getPerfil() != Perfiles.ARTISTA) {
			System.out.println("Solo un usuario con perfil ARTISTA puede ver su ficha.");
			return;
		}

		Persona persona = personaRepository.findByNombre(sesion.getNombrePersona());
		if (persona == null) {
			System.out.println("No se encontró la persona en la base de datos.");
			return;
		}

		Artista artista = artistaRepository.findByIdPersona(persona.getId());
		if (artista == null) {
			System.out.println("No se encontraron datos de artista para esta persona.");
			return;
		}

		// === FICHA DEL ARTISTA ===
		System.out.println("=== FICHA DEL ARTISTA ===");
		System.out.println("Nombre: " + persona.getNombre());
		System.out.println("Email: " + persona.getEmail());
		System.out.println("Nacionalidad: " + persona.getNacionalidad());
		System.out.println("Apodo: " + (artista.getApodo() != null ? artista.getApodo() : "(sin apodo)"));
		System.out.println();

		// === PARTICIPACIONES ===
		List<Numero> numeros = numeroRepository.findByArtistasContains(artista);

		System.out.println("=== PARTICIPACION EN NUMEROS Y ESPECTACULOS ===");

		if (numeros.isEmpty()) {
			System.out.println("Este artista no está asignado a ningún número.");
			return;
		}

		for (Numero n : numeros) {
			Espectaculo e = n.getEspectaculo();

			System.out.println("----------------------------------------");
			System.out.println("Espectáculo: " + e.getNombre() + " (id " + e.getId() + ")");
			System.out.println("  Fechas: " + e.getFechaInicio() + " - " + e.getFechaFin());
			System.out.println("Número: " + n.getNombre() + " (id " + n.getId() + ")");
			System.out.println("  Orden: " + n.getOrden() + "  Duración: " + n.getDuracion() + " minutos");
		}

		System.out.println("----------------------------------------");
	}
}
