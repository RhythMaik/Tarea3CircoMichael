package com.luisdbb.tarea3AD2024base.services;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.repositorios.*;

import jakarta.transaction.Transactional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con
 * espectáculos.
 *
 * Forma parte de la capa de negocio y actúa como intermediario entre los
 * controladores y los repositorios. Proporciona validaciones, operaciones CRUD,
 * creación y modificación de espectáculos, así como la gestión de sus números.
 *
 * Este servicio participa en los casos de uso: - CU1: Visualización de
 * espectáculos - CU4: Visualización completa de un espectáculo - CU5: Gestión
 * de espectáculos y números
 *
 * También coordina la carga de artistas y evita problemas de inicialización
 * perezosa mediante transacciones cuando es necesario.
 *
 * Se gestiona mediante Spring como un componente de la capa de servicios.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Service
public class ServicioEspectaculos {

	/**
	 * Repositorio para acceder a datos de espectáculos.
	 */
	@Autowired
	private EspectaculoRepository espectaculoRepository;

	/**
	 * Repositorio para acceder a datos de números circenses.
	 */
	@Autowired
	private NumeroRepository numeroRepository;

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
	 * Repositorio para acceder a datos de coordinadores.
	 */
	@Autowired
	private CoordinacionRepository coordinacionRepository;

	// ============================================================
	// VALIDACIONES
	// ============================================================

	/**
	 * Valida el nombre de un espectáculo según las reglas de negocio. El nombre
	 * debe: - No ser nulo - No estar en blanco - Tener un máximo de 25 caracteres -
	 * No contener el carácter '|'
	 *
	 * @param nombre nombre a validar
	 * @return true si es válido, false en caso contrario
	 */
	public boolean nombreValido(String nombre) {
		return nombre != null && !nombre.isBlank() && nombre.length() <= 25 && !nombre.contains("|");
	}

	/**
	 * Valida un rango de fechas según las reglas de negocio: - La fecha fin no
	 * puede ser anterior a la fecha inicio - La duración máxima del espectáculo es
	 * de 1 año
	 *
	 * @param inicio fecha de inicio
	 * @param fin    fecha de fin
	 * @return true si el rango es válido, false en caso contrario
	 */
	public boolean fechasValidas(LocalDate inicio, LocalDate fin) {
		if (fin.isBefore(inicio))
			return false;
		return !fin.isAfter(inicio.plusYears(1));
	}

	// ============================================================
	// CRUD BÁSICO
	// ============================================================

	/**
	 * Obtiene todos los espectáculos registrados.
	 *
	 * @return lista de espectáculos
	 */
	@Transactional
	public List<Espectaculo> findAll() {
	    List<Espectaculo> lista = espectaculoRepository.findAll();

	    // Metodo con calzador para forzar la carga que si no me tengo que meter de nuevo con los repositorios
	    for (Espectaculo e : lista) {
	        if (e.getNumeros() != null) {
	            e.getNumeros().size(); 
	        }
	    }

	    return lista;
	}


	/**
	 * Busca un espectáculo por su identificador.
	 *
	 * @param id identificador del espectáculo
	 * @return espectáculo encontrado o null si no existe
	 */
	public Espectaculo findById(Integer id) {
		return espectaculoRepository.findById(id).orElse(null);
	}

	/**
	 * Guarda o actualiza un espectáculo en la base de datos.
	 *
	 * @param esp espectáculo a persistir
	 * @return espectáculo guardado
	 */
	public Espectaculo save(Espectaculo esp) {
		return espectaculoRepository.save(esp);
	}

	/**
	 * Elimina un espectáculo por su identificador.
	 *
	 * @param id identificador del espectáculo
	 */
	public void delete(Integer id) {
		espectaculoRepository.deleteById(id);
	}

	// ============================================================
	// CREAR ESPECTÁCULO
	// ============================================================

	/**
	 * Crea un nuevo espectáculo validando nombre, fechas y coordinador.
	 *
	 * @param nombre       nombre del espectáculo
	 * @param inicio       fecha de inicio
	 * @param fin          fecha de fin
	 * @param personaCoord persona que actuará como coordinador
	 * @return espectáculo creado
	 */
	public Espectaculo crearEspectaculo(String nombre, LocalDate inicio, LocalDate fin, Persona personaCoord) {

		if (!nombreValido(nombre))
			throw new IllegalArgumentException("Nombre inválido");

		if (!fechasValidas(inicio, fin))
			throw new IllegalArgumentException("Fechas inválidas");

		Coordinacion coordinacion = coordinacionRepository.findByIdPersona(personaCoord.getId());
		if (coordinacion == null)
			throw new IllegalArgumentException("La persona no es coordinador");

		Espectaculo esp = new Espectaculo(nombre, inicio, fin, coordinacion);

		return espectaculoRepository.save(esp);
	}

	// ============================================================
	// MODIFICAR ESPECTÁCULO
	// ============================================================

	/**
	 * Actualiza el nombre de un espectáculo.
	 *
	 * @param esp         espectáculo a modificar
	 * @param nuevoNombre nuevo nombre
	 * @return espectáculo actualizado
	 */
	public Espectaculo actualizarNombre(Espectaculo esp, String nuevoNombre) {
		if (!nombreValido(nuevoNombre))
			throw new IllegalArgumentException("Nombre inválido");

		esp.setNombre(nuevoNombre);
		return espectaculoRepository.save(esp);
	}

	/**
	 * Actualiza las fechas de un espectáculo.
	 *
	 * @param esp    espectáculo a modificar
	 * @param inicio nueva fecha de inicio
	 * @param fin    nueva fecha de fin
	 * @return espectáculo actualizado
	 */
	public Espectaculo actualizarFechas(Espectaculo esp, LocalDate inicio, LocalDate fin) {
		if (!fechasValidas(inicio, fin))
			throw new IllegalArgumentException("Fechas inválidas");

		esp.setFechaInicio(inicio);
		esp.setFechaFin(fin);
		return espectaculoRepository.save(esp);
	}

	/**
	 * Actualiza el coordinador de un espectáculo.
	 *
	 * @param espDetached espectáculo recibido desde la vista
	 * @param idPersona   id de la persona que será el nuevo coordinador
	 * @return espectáculo actualizado
	 */
	@Transactional
	public Espectaculo actualizarCoordinador(Espectaculo espDetached, Integer idPersona) {

		Espectaculo esp = espectaculoRepository.findById(espDetached.getId())
				.orElseThrow(() -> new IllegalArgumentException("Espectáculo no encontrado"));

		personaRepository.findById(idPersona).orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

		Coordinacion coordinacion = coordinacionRepository.findByIdPersona(idPersona);
		if (coordinacion == null)
			throw new IllegalArgumentException("La persona no es coordinador");

		esp.setCoordinador(coordinacion);

		return espectaculoRepository.save(esp);
	}

	// ============================================================
	// NÚMEROS DEL ESPECTÁCULO
	// ============================================================

	/**
	 * Obtiene los números de un espectáculo y fuerza la carga de artistas para
	 * evitar LazyInitializationException.
	 *
	 * @param esp espectáculo asociado
	 * @return lista de números ordenados por su campo "orden"
	 */
	@Transactional
	public List<Numero> obtenerNumeros(Espectaculo esp) {
		List<Numero> numeros = numeroRepository.findByEspectaculoOrderByOrdenAsc(esp);

		for (Numero n : numeros) {
			n.getArtistas().size();
		}

		return numeros;
	}

	/**
	 * Crea un número dentro de un espectáculo.
	 *
	 * Valida que el espectáculo exista y que el orden sea mayor o igual que 1. El
	 * número se añade a la lista del espectáculo manteniendo la relación
	 * bidireccional.
	 *
	 * @param idEspectaculo id del espectáculo
	 * @param orden         orden del número dentro del espectáculo
	 * @param nombre        nombre del número
	 * @param duracion      duración en minutos
	 * @return número creado
	 */
	@Transactional
	public Numero crearNumero(Integer idEspectaculo, int orden, String nombre, double duracion) {

		Espectaculo esp = espectaculoRepository.findById(idEspectaculo)
				.orElseThrow(() -> new IllegalArgumentException("Espectaculo no encontrado"));

		if (orden < 1)
			throw new IllegalArgumentException("El orden debe ser >= 1");

		Numero n = new Numero(orden, nombre, duracion);
		esp.addNumero(n);

		espectaculoRepository.save(esp);

		return n;
	}

	/**
	 * Actualiza los datos de un número.
	 *
	 * No realiza validaciones adicionales; se asume que los datos ya han sido
	 * verificados en la capa de presentación.
	 *
	 * @param n        número a modificar
	 * @param orden    nuevo orden
	 * @param nombre   nuevo nombre
	 * @param duracion nueva duración
	 * @return número actualizado
	 */
	public Numero actualizarNumero(Numero n, int orden, String nombre, double duracion) {
		n.setOrden(orden);
		n.setNombre(nombre);
		n.setDuracion(duracion);
		return numeroRepository.save(n);
	}

	/**
	 * Elimina un número y reordena los restantes del espectáculo.
	 *
	 * Tras eliminar el número, se reasignan los órdenes de forma consecutiva
	 * comenzando desde 1.
	 *
	 * @param idNumero identificador del número
	 */
	@Transactional
	public void borrarNumero(Integer idNumero) {

		Numero num = numeroRepository.findById(idNumero)
				.orElseThrow(() -> new IllegalArgumentException("Numero no encontrado"));

		Espectaculo esp = espectaculoRepository.findById(num.getEspectaculo().getId())
				.orElseThrow(() -> new IllegalArgumentException("Espectaculo no encontrado"));

		// Fuerza carga de números
		esp.getNumeros().size();
		esp.getNumeros().remove(num);

		numeroRepository.delete(num);

		// Reordenar
		int orden = 1;
		for (Numero n : esp.getNumeros()) {
			n.setOrden(orden++);
		}
	}

	// ============================================================
	// ARTISTAS DE UN NÚMERO
	// ============================================================

	/**
	 * Obtiene los artistas asignados a un número.
	 *
	 * @param numero número asociado
	 * @return lista de artistas participantes
	 */
	@Transactional
	public List<Artista> obtenerArtistasDeNumero(Numero numero) {

		Numero n = numeroRepository.findById(numero.getId())
				.orElseThrow(() -> new IllegalArgumentException("Numero no encontrado"));

		n.getArtistas().size();

		return new ArrayList<>(n.getArtistas());
	}

	/**
	 * Asigna un artista a un número.
	 *
	 * Valida que el número exista, que la persona exista y que sea artista.
	 *
	 * @param idNumero  id del número
	 * @param idPersona id de la persona artista
	 */
	@Transactional
	public void anadirArtistaANumero(Integer idNumero, Integer idPersona) {

		Numero numero = numeroRepository.findById(idNumero)
				.orElseThrow(() -> new IllegalArgumentException("Numero no encontrado"));

		numero.getArtistas().size();

		personaRepository.findById(idPersona).orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

		Artista artista = artistaRepository.findByIdPersona(idPersona);
		if (artista == null)
			throw new IllegalArgumentException("La persona no es artista");

		numero.getArtistas().add(artista);

		numeroRepository.save(numero);
	}

	/**
	 * Elimina un artista de un número.
	 *
	 * @param idNumero  id del número
	 * @param idPersona id de la persona artista
	 */
	@Transactional
	public void quitarArtistaDeNumero(Integer idNumero, Integer idPersona) {

		Numero numero = numeroRepository.findById(idNumero)
				.orElseThrow(() -> new IllegalArgumentException("Numero no encontrado"));

		numero.getArtistas().size();

		personaRepository.findById(idPersona).orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

		Artista artista = artistaRepository.findByIdPersona(idPersona);
		if (artista == null)
			throw new IllegalArgumentException("La persona no es artista");

		numero.getArtistas().remove(artista);

		numeroRepository.save(numero);
	}

	// ============================================================
	// INFORMES
	// ============================================================

	/**
	 * Genera un informe completo del espectáculo.
	 *
	 * El informe incluye: - Datos generales del espectáculo - Información del
	 * coordinador - Listado de números - Artistas participantes en cada número
	 *
	 * @param idEsp identificador del espectáculo
	 * @return informe en formato texto
	 */
	@Transactional
	public String generarInformeEspectaculo(Integer idEsp) {

		Espectaculo esp = espectaculoRepository.findById(idEsp)
				.orElseThrow(() -> new IllegalArgumentException("Espectaculo no encontrado"));

		esp.getNumeros().size();

		for (Numero n : esp.getNumeros()) {
			n.getArtistas().size();
		}

		StringBuilder sb = new StringBuilder();

		sb.append("\n=== INFORME COMPLETO DEL ESPECTACULO ===\n");
		sb.append("ID: ").append(esp.getId()).append("\n");
		sb.append("Nombre: ").append(esp.getNombre()).append("\n");
		sb.append("Fechas: ").append(esp.getFechaInicio()).append(" - ").append(esp.getFechaFin()).append("\n\n");

		Coordinacion coord = esp.getCoordinador();
		Persona p = coord.getPersona();

		sb.append("=== COORDINACION ===\n");
		sb.append("Nombre: ").append(p.getNombre()).append("\n");
		sb.append("Email: ").append(p.getEmail()).append("\n");
		sb.append("Nacionalidad: ").append(p.getNacionalidad()).append("\n");
		sb.append("Senior: ").append(coord.isSenior() ? "Si" : "No").append("\n");
		if (coord.isSenior()) {
			sb.append("Fecha senior: ").append(coord.getFechaSenior()).append("\n");
		}
		sb.append("\n");

		sb.append("=== NUMEROS ===\n");

		for (Numero n : esp.getNumeros()) {
			sb.append("\nNumero ").append(n.getOrden()).append(" (ID ").append(n.getId()).append(")\n");
			sb.append("Nombre: ").append(n.getNombre()).append("\n");
			sb.append("Duracion: ").append(n.getDuracion()).append(" minutos\n");

			sb.append("Artistas:\n");
			for (Artista a : n.getArtistas()) {
				Persona pa = a.getPersona();
				sb.append(" - ").append(pa.getNombre()).append(" (").append(pa.getNacionalidad()).append(")");

				if (a.getApodo() != null) {
					sb.append(" | Apodo: ").append(a.getApodo());
				}

				sb.append(" | Especialidades: ").append(a.getEspecialidades()).append("\n");
			}
		}

		sb.append("\n=== FIN DEL INFORME ===\n");

		return sb.toString();
	}

	/**
	 * Obtiene los artistas asignados a un número.
	 *
	 * @param idNumero identificador del número
	 * @return lista de artistas
	 */
	@Transactional
	public List<Artista> obtenerArtistasAsignados(Integer idNumero) {

		Numero numero = numeroRepository.findById(idNumero)
				.orElseThrow(() -> new IllegalArgumentException("Numero no encontrado"));

		numero.getArtistas().size();
		return new ArrayList<>(numero.getArtistas());
	}

	/**
	 * Muestra por consola los números de un espectáculo.
	 *
	 * @param idEspectaculo identificador del espectáculo
	 */
	@Transactional
	public void verNumerosDeEspectaculo(Integer idEspectaculo) {

		Espectaculo esp = espectaculoRepository.findById(idEspectaculo)
				.orElseThrow(() -> new IllegalArgumentException("Espectaculo no encontrado"));

		esp.getNumeros().size();

		System.out.println("=== NUMEROS DEL ESPECTACULO ===");

		for (Numero n : esp.getNumeros()) {
			System.out.println("ID " + n.getId() + " | Orden " + n.getOrden() + " | " + n.getNombre());
		}
	}

	// ============================================================
	// EXPORTAR ESPECTÁCULOS A JSON
	// ============================================================

	/**
	 * Exporta todos los espectáculos del sistema en formato JSON.
	 *
	 * El archivo generado incluye: - ID - Nombre - Fecha de inicio - Fecha de fin
	 *
	 * @param destino archivo donde se guardará el JSON
	 */
	public void exportarEspectaculosJSON(File destino) {

		try {
			List<Espectaculo> lista = findAll();

			StringBuilder sb = new StringBuilder();
			sb.append("[\n");

			for (int i = 0; i < lista.size(); i++) {
				Espectaculo e = lista.get(i);

				sb.append("  {\n");
				sb.append("    \"id\": ").append(e.getId()).append(",\n");
				sb.append("    \"nombre\": \"").append(e.getNombre()).append("\",\n");
				sb.append("    \"fechaInicio\": \"").append(e.getFechaInicio()).append("\",\n");
				sb.append("    \"fechaFin\": \"").append(e.getFechaFin()).append("\"\n");
				sb.append("  }");

				if (i < lista.size() - 1) {
					sb.append(",");
				}
				sb.append("\n");
			}

			sb.append("]");

			try (FileWriter fw = new FileWriter(destino)) {
				fw.write(sb.toString());
			}

		} catch (Exception e) {
			throw new RuntimeException("Error exportando espectáculos JSON: " + e.getMessage(), e);
		}
	}
}