package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un espectáculo dentro del sistema del circo.
 *
 * Un espectáculo tiene un nombre único, un periodo de fechas en el que está
 * activo y un coordinador responsable. Además, contiene una lista de números
 * circenses que forman parte del espectáculo, ordenados según su campo "orden".
 *
 * Esta entidad se utiliza en: - Ver espectáculos - Ver un espectáculo completo
 * - Gestionar espectáculos y sus números
 *
 * Forma parte del modelo de dominio y se guarda en la base de datos mediante
 * JPA.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Entity
@Table(name = "espectaculo")
public class Espectaculo {

	/**
	 * FSF Identificador único del espectáculo. Se genera automáticamente con
	 * estrategia IDENTITY.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * Nombre del espectáculo. Es único, obligatorio y tiene un máximo de 25
	 * caracteres.
	 */
	@Column(length = 25, nullable = false, unique = true)
	private String nombre;

	/**
	 * Fecha de inicio del espectáculo. Es obligatoria.
	 */
	@Column(name = "fecha_inicio", nullable = false)
	private LocalDate fechaInicio;

	/**
	 * Fecha de fin del espectáculo. Es obligatoria.
	 */
	@Column(name = "fecha_fin", nullable = false)
	private LocalDate fechaFin;

	/**
	 * Coordinador responsable del espectáculo. Relación muchos a uno con la entidad
	 * Coordinacion.
	 */
	@ManyToOne
	@JoinColumn(name = "id_coord", referencedColumnName = "id_persona", nullable = true)
	private Coordinacion coordinador;

	/**
	 * Lista de números circenses que forman parte del espectáculo. Relación uno a
	 * muchos bidireccional. Se activa el borrado en cascada y la eliminación
	 * huérfana.
	 */
	@OneToMany(mappedBy = "espectaculo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Numero> numeros = new ArrayList<>();

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public Espectaculo() {
	}

	/**
	 * Constructor principal para crear un espectáculo.
	 *
	 * @param nombre      nombre del espectáculo
	 * @param fechaInicio fecha de inicio
	 * @param fechaFin    fecha de fin
	 * @param coordinador coordinador responsable
	 */
	public Espectaculo(String nombre, LocalDate fechaInicio, LocalDate fechaFin, Coordinacion coordinador) {
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.coordinador = coordinador;
	}

	/**
	 * Obtiene el identificador del espectáculo.
	 *
	 * @return id del espectáculo
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Obtiene el nombre del espectáculo.
	 *
	 * @return nombre del espectáculo
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna un nuevo nombre al espectáculo.
	 *
	 * @param nombre nombre nuevo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene la fecha de inicio del espectáculo.
	 *
	 * @return fecha de inicio
	 */
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * Asigna la fecha de inicio del espectáculo.
	 *
	 * @param fechaInicio nueva fecha de inicio
	 */
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Obtiene la fecha de fin del espectáculo.
	 *
	 * @return fecha de fin
	 */
	public LocalDate getFechaFin() {
		return fechaFin;
	}

	/**
	 * Asigna la fecha de fin del espectáculo.
	 *
	 * @param fechaFin nueva fecha de fin
	 */
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * Obtiene el coordinador del espectáculo.
	 *
	 * @return coordinador asignado
	 */
	public Coordinacion getCoordinador() {
		return coordinador;
	}

	/**
	 * Asigna un nuevo coordinador al espectáculo.
	 *
	 * @param coordinador coordinador responsable
	 */
	public void setCoordinador(Coordinacion coordinador) {
		this.coordinador = coordinador;
	}

	/**
	 * Obtiene la lista de números del espectáculo.
	 *
	 * @return lista de números
	 */
	public List<Numero> getNumeros() {
		return numeros;
	}

	/**
	 * Asigna una nueva lista completa de números al espectáculo.
	 *
	 * @param numeros lista de números
	 */
	public void setNumeros(List<Numero> numeros) {
		this.numeros = numeros;
	}

	/**
	 * Añade un número al espectáculo y mantiene la relación bidireccional.
	 *
	 * @param numero número a añadir
	 */
	public void addNumero(Numero numero) {
		numeros.add(numero);
		numero.setEspectaculo(this);
	}

	/**
	 * Representación en texto del espectáculo.
	 *
	 * @return cadena descriptiva del espectáculo
	 */
	@Override
	public String toString() {
		return "Espectaculo{" + "id=" + id + ", nombre='" + nombre + '\'' + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + ", coordinador="
				+ (coordinador != null ? coordinador.getIdPersona() : "sin asignar") + '}';
	}
}
