package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;

/**
 * Representa las credenciales de acceso de una persona dentro del sistema.
 *
 * Esta clase contiene la información necesaria para iniciar sesión: nombre de
 * usuario, contraseña y perfil del usuario.
 *
 * Comparte la misma clave primaria que la entidad Persona debido a la relación
 * uno a uno. Cada usuario tiene un único nombre de usuario, almacenado siempre
 * en minúsculas.
 *
 * Esta entidad se utiliza en el proceso de autenticación y en la gestión de
 * personas y perfiles.
 *
 * Forma parte del modelo de dominio y se guarda en la base de datos mediante
 * JPA.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Entity
@Table(name = "credenciales")
public class Credenciales {

	/**
	 * Identificador de las credenciales. Es el mismo ID que el de la persona
	 * asociada.
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
	 * Nombre de usuario del sistema. Es único, obligatorio y se almacena en
	 * minúsculas.
	 */
	@Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
	private String nombreUsuario;

	/**
	 * Contraseña del usuario. Es obligatoria.
	 */
	@Column(name = "contrasenia", nullable = false, length = 50)
	private String contrasenia;

	/**
	 * Perfil o rol del usuario dentro del sistema. Puede ser ADMIN, COORDINACION o
	 * ARTISTA.
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Perfiles perfil;

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public Credenciales() {
	}

	/**
	 * Constructor principal para crear credenciales.
	 *
	 * @param persona       persona asociada
	 * @param nombreUsuario nombre de usuario único
	 * @param contrasenia   contraseña del usuario
	 * @param perfil        perfil asignado
	 */
	public Credenciales(Persona persona, String nombreUsuario, String contrasenia, Perfiles perfil) {
		this.persona = persona;
		this.nombreUsuario = nombreUsuario;
		this.contrasenia = contrasenia;
		this.perfil = perfil;
		this.idPersona = persona != null ? persona.getId() : null;
	}

	/**
	 * Obtiene el identificador de las credenciales.
	 *
	 * @return ID de la persona asociada
	 */
	public Integer getIdPersona() {
		return idPersona;
	}

	/**
	 * Obtiene la persona asociada a estas credenciales.
	 *
	 * @return objeto Persona
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * Obtiene el nombre de usuario.
	 *
	 * @return nombre de usuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Asigna un nuevo nombre de usuario.
	 *
	 * @param nombreUsuario nombre de usuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Obtiene la contraseña del usuario.
	 *
	 * @return contraseña
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * Asigna una nueva contraseña.
	 *
	 * @param contrasenia contraseña nueva
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene el perfil del usuario.
	 *
	 * @return perfil asignado
	 */
	public Perfiles getPerfil() {
		return perfil;
	}

	/**
	 * Asigna un nuevo perfil al usuario.
	 *
	 * @param perfil perfil nuevo
	 */
	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}
}
