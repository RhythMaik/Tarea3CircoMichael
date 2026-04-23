package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Representa a una persona dentro del sistema del circo.
 *
 * Una persona contiene información básica como nombre, email y nacionalidad.
 * Además, puede tener asociadas credenciales de acceso mediante una relación
 * uno a uno, lo que permite autenticarla dentro del sistema.
 *
 * Esta entidad se utiliza en: - Gestión de personas - Login y autenticación -
 * Visualización de la ficha del artista (cuando aplica)
 *
 * Forma parte del modelo de dominio y se guarda en la base de datos mediante
 * JPA. Implementa Serializable para permitir su transporte en sesiones si fuera
 * necesario.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Entity
@Table(name = "persona")
public class Persona implements Serializable {

	/**
	 * Identificador único de la persona. Se genera automáticamente con estrategia
	 * IDENTITY.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * Nombre completo de la persona. Es obligatorio y tiene un máximo de 50
	 * caracteres.
	 */
	@Column(length = 50, nullable = false)
	private String nombre;

	/**
	 * Correo electrónico de la persona. Es obligatorio, único y tiene un máximo de
	 * 50 caracteres.
	 */
	@Column(length = 50, nullable = false, unique = true)
	private String email;

	/**
	 * Nacionalidad de la persona. Es obligatoria y tiene un máximo de 50
	 * caracteres.
	 */
	@Column(length = 50, nullable = false)
	private String nacionalidad;

	/**
	 * Credenciales asociadas a la persona. Relación uno a uno bidireccional. Se
	 * activa el borrado en cascada para mantener integridad.
	 */
	@OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
	private Credenciales credenciales;

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public Persona() {
	}

	/**
	 * Constructor principal para crear una persona.
	 *
	 * @param nombre       nombre completo
	 * @param email        correo electrónico único
	 * @param nacionalidad nacionalidad de la persona
	 */
	public Persona(String nombre, String email, String nacionalidad) {
		this.nombre = nombre;
		this.email = email;
		this.nacionalidad = nacionalidad;
	}

	/**
	 * Obtiene el identificador de la persona.
	 *
	 * @return id de la persona
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Asigna el identificador de la persona.
	 *
	 * @param id nuevo id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de la persona.
	 *
	 * @return nombre completo
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna un nuevo nombre a la persona.
	 *
	 * @param nombre nombre nuevo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene el email de la persona.
	 *
	 * @return correo electrónico
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Asigna un nuevo email a la persona.
	 *
	 * @param email correo electrónico nuevo
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtiene la nacionalidad de la persona.
	 *
	 * @return nacionalidad
	 */
	public String getNacionalidad() {
		return nacionalidad;
	}

	/**
	 * Asigna una nueva nacionalidad a la persona.
	 *
	 * @param nacionalidad nacionalidad nueva
	 */
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	/**
	 * Obtiene las credenciales asociadas a la persona.
	 *
	 * @return credenciales del usuario
	 */
	public Credenciales getCredenciales() {
		return credenciales;
	}

	/**
	 * Asigna nuevas credenciales a la persona.
	 *
	 * @param credenciales credenciales nuevas
	 */
	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}

	/**
	 * Representación en texto de la persona.
	 *
	 * @return cadena descriptiva de la persona
	 */
	@Override
	public String toString() {
		return "Persona:\n" + "Id: " + id + "\nNombre: " + nombre + "\nEmail: " + email + "\nNacionalidad: "
				+ nacionalidad;
	}
}
