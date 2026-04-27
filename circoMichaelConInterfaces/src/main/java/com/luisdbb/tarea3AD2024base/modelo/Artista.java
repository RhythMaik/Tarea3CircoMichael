package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;
import java.util.List;

/**
 * Representa a un artista dentro del sistema del circo.
 *
 * Un artista está directamente asociado a una persona mediante una relación uno
 * a uno. El identificador del artista es el mismo que el de la persona, por lo
 * que ambos comparten la misma clave primaria.
 *
 * Un artista puede tener un apodo profesional y una lista de especialidades que
 * describen las disciplinas en las que trabaja dentro del circo.
 *
 * Esta clase forma parte del modelo de dominio y se utiliza en: - Gestión de
 * artistas - Asignación de artistas a números - Visualización de la ficha del
 * artista
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Entity
@Table(name = "artista")
public class Artista {

	/**
	 * Identificador del artista. Coincide con el ID de la persona asociada.
	 */
	@Id
	@Column(name = "id_persona")
	private Integer idPersona;

	/**
	 * Relación uno a uno con la entidad Persona. Se usa @MapsId para compartir la
	 * misma clave primaria.
	 */
	@OneToOne
	@MapsId
	@JoinColumn(name = "id_persona")
	private Persona persona;

	/**
	 * Apodo profesional del artista.
	 */
	private String apodo;

	/**
	 * Lista de especialidades del artista. Se almacena en una tabla secundaria
	 * llamada "artista_especialidad".
	 */
	@ElementCollection(targetClass = Especialidad.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "artista_especialidad", joinColumns = @JoinColumn(name = "id_artista"))
	@Column(name = "especialidad")
	private List<Especialidad> especialidades;

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public Artista() {
	}

	/**
	 * Constructor principal para crear un artista.
	 *
	 * @param persona        persona asociada al artista
	 * @param apodo          apodo profesional
	 * @param especialidades lista de especialidades del artista
	 */
	public Artista(Persona persona, String apodo, List<Especialidad> especialidades) {
		this.persona = persona;
		this.apodo = apodo;
		this.especialidades = especialidades;
		// El idPersona se gestiona automáticamente por @MapsId
	}

	/**
	 * Obtiene el identificador del artista.
	 *
	 * @return ID de la persona asociada
	 */
	public Integer getIdPersona() {
		return idPersona;
	}

	/**
	 * Obtiene la persona asociada al artista.
	 *
	 * @return objeto Persona
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * Asigna la persona asociada al artista.
	 *
	 * @param persona nueva persona asociada
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
		// No modificar idPersona manualmente
	}

	/**
	 * Obtiene el apodo del artista.
	 *
	 * @return apodo profesional
	 */
	public String getApodo() {
		return apodo;
	}

	/**
	 * Asigna un apodo al artista.
	 *
	 * @param apodo apodo profesional
	 */
	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	/**
	 * Obtiene la lista de especialidades del artista.
	 *
	 * @return lista de especialidades
	 */
	public List<Especialidad> getEspecialidades() {
		return especialidades;
	}

	/**
	 * Asigna la lista de especialidades del artista.
	 *
	 * @param especialidades lista de especialidades
	 */
	public void setEspecialidades(List<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * Representación en texto del artista.
	 *
	 * @return cadena con información básica del artista
	 */
	@Override
	public String toString() {
		return "Artista{" + "idPersona=" + idPersona + ", apodo='" + apodo + '\'' + ", especialidades=" + especialidades
				+ '}';
	}
}
