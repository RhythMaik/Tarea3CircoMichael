package com.luisdbb.tarea3AD2024base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.repositorios.ArtistaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.CoordinacionRepository;
import com.luisdbb.tarea3AD2024base.repositorios.CredencialesRepository;
import com.luisdbb.tarea3AD2024base.repositorios.PersonaRepository;

/**
 * Servicio encargado de gestionar el proceso de inicio de sesión.
 *
 * Centraliza toda la lógica de autenticación del sistema: - Verifica usuario y
 * contraseña - Obtiene la entidad Persona asociada - Determina el perfil real
 * del usuario (ADMIN, COORDINACION, ARTISTA) - Actualiza el objeto Sesion, que
 * representa al usuario conectado
 *
 * Este servicio se utiliza en el caso de uso CU2 (login/logout) y actúa como
 * punto único para validar credenciales y establecer el contexto de sesión.
 *
 * Forma parte de la capa de servicios dentro del modelo de aplicación.
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Service
public class ServicioLogin {

	/**
	 * Repositorio para validar credenciales de acceso.
	 */
	@Autowired
	private CredencialesRepository credencialesRepository;

	/**
	 * Repositorio para obtener información de personas.
	 */
	@Autowired
	private PersonaRepository personaRepository;

	/**
	 * Repositorio para verificar si una persona pertenece a coordinación.
	 */
	@Autowired
	private CoordinacionRepository coordinacionRepository;

	/**
	 * Repositorio para verificar si una persona es artista.
	 */
	@Autowired
	private ArtistaRepository artistaRepository;

	@Value("${admin.usuario}")
	private String adminUsuario;

	@Value("${admin.contrasenia}")
	private String adminContrasenia;

	/**
	 * Método principal de inicio de sesión.
	 *
	 * Comprueba usuario y contraseña, determina el perfil real del usuario y
	 * actualiza el objeto Sesion si las credenciales son correctas.
	 *
	 * @param usuario     nombre de usuario introducido
	 * @param contrasenia contraseña introducida
	 * @param sesion      objeto Sesion que se actualizará si el login es correcto
	 * @return true si las credenciales son válidas, false en caso contrario
	 */
	public boolean login(String usuario, String contrasenia, Sesion sesion) {

		// Buscar credenciales por nombre de usuario
		Credenciales cred = credencialesRepository.findByNombreUsuario(usuario);
		if (cred == null)
			return false;

		// Validar contraseña
		if (!cred.getContrasenia().equals(contrasenia))
			return false;

		// Obtener la persona asociada
		Persona persona = cred.getPersona();
		if (persona == null)
			return false;

		// Determinar el perfil real según las tablas del sistema
		Perfiles perfil = obtenerPerfil(persona);

		// Actualizar la sesión activa
		sesion.setNombrePersona(persona.getNombre());
		sesion.setPerfil(perfil);

		return true;
	}

	/**
	 * Determina el perfil real de una persona según su rol en el sistema.
	 *
	 * Se consulta por idPersona para evitar problemas con entidades desconectadas.
	 *
	 * @param persona persona autenticada
	 * @return perfil real del usuario
	 */
	private Perfiles obtenerPerfil(Persona persona) {

		// Si aparece en la tabla de coordinación → COORDINACION
		if (coordinacionRepository.existsByIdPersona(persona.getId()))
			return Perfiles.COORDINACION;

		// Si aparece en la tabla de artistas → ARTISTA
		if (artistaRepository.existsByIdPersona(persona.getId()))
			return Perfiles.ARTISTA;

		// Si no aparece en ninguna → ADMIN
		return Perfiles.ADMIN;
	}
}
