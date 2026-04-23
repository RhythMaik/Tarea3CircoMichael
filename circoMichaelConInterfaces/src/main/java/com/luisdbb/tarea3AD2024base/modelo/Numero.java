package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un número artístico dentro de un espectáculo.
 *
 * Un número tiene un orden dentro del espectáculo, un nombre descriptivo y una
 * duración en minutos. Cada número pertenece a un único espectáculo y puede
 * tener varios artistas asociados mediante una relación muchos a muchos.
 *
 * Esta entidad se utiliza en: - Ver un espectáculo completo - Crear o modificar
 * números - Asignar artistas a un número
 *
 * Forma parte del modelo de dominio y se guarda en la base de datos mediante
 * JPA.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Entity
@Table(name = "numero")
public class Numero {

	/**
	 * Identificador único del número. Se genera automáticamente con estrategia
	 * IDENTITY.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * Posición del número dentro del espectáculo. Es obligatorio y debe ser un
	 * número entero positivo.
	 */
	@Column(nullable = false)
	private int orden;

	/**
	 * Nombre descriptivo del número. Es obligatorio y tiene un máximo de 50
	 * caracteres.
	 */
	@Column(length = 50, nullable = false)
	private String nombre;

	/**
	 * Duración del número en minutos. Según la TAREA 2, debe ser un valor X.0 o
	 * X.5.
	 */
	@Column(nullable = false)
	private double duracion;

	/**
	 * Espectáculo al que pertenece el número. Relación muchos a uno obligatoria.
	 */
	@ManyToOne
	@JoinColumn(name = "id_espectaculo", nullable = false)
	private Espectaculo espectaculo;

	/**
	 * Lista de artistas que participan en el número. Relación muchos a muchos
	 * mediante tabla intermedia.
	 */
	@ManyToMany
	@JoinTable(name = "numero_artista", joinColumns = @JoinColumn(name = "id_numero"), inverseJoinColumns = @JoinColumn(name = "id_artista", referencedColumnName = "id_persona"))
	private List<Artista> artistas = new ArrayList<>();

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public Numero() {
	}

	/**
	 * Constructor principal para crear un número.
	 *
	 * @param orden    orden dentro del espectáculo
	 * @param nombre   nombre del número
	 * @param duracion duración en minutos
	 */
	public Numero(int orden, String nombre, double duracion) {
		this.orden = orden;
		this.nombre = nombre;
		this.duracion = duracion;
	}

	/**
	 * Obtiene el identificador del número.
	 *
	 * @return id del número
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Obtiene el orden del número dentro del espectáculo.
	 *
	 * @return orden del número
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * Asigna un nuevo orden al número.
	 *
	 * @param orden nuevo orden
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * Obtiene el nombre del número.
	 *
	 * @return nombre del número
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna un nuevo nombre al número.
	 *
	 * @param nombre nombre nuevo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene la duración del número.
	 *
	 * @return duración en minutos
	 */
	public double getDuracion() {
		return duracion;
	}

	/**
	 * Asigna una nueva duración al número.
	 *
	 * @param duracion duración nueva
	 */
	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}

	/**
	 * Obtiene el espectáculo al que pertenece el número.
	 *
	 * @return espectáculo asociado
	 */
	public Espectaculo getEspectaculo() {
		return espectaculo;
	}

	/**S
	 * Asigna el espectáculo al que pertenece el número.
	 *
	 * @param espectaculo nuevo espectáculo
	 */
	public void setEspectaculo(Espectaculo espectaculo) {
		this.espectaculo = espectaculo;
	}

	/**
	 * Obtiene la lista de artistas que participan en el número.
	 *
	 * @return lista de artistas
	 */
	public List<Artista> getArtistas() {
		return artistas;
	}

	/**
	 * Asigna una nueva lista de artistas al número.
	 *
	 * @param artistas lista de artistas
	 */
	public void setArtistas(List<Artista> artistas) {
		this.artistas = artistas;
	}

	/**
	 * Representación en texto del número.
	 *
	 * @return cadena descriptiva del número
	 */
	@Override
	public String toString() {
		return "Numero{" + "id=" + id + ", orden=" + orden + ", nombre='" + nombre + '\'' + ", duracion=" + duracion
				+ '}';
	}
}
