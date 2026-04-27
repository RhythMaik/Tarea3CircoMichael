package com.luisdbb.tarea3AD2024base.modelo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Gestiona la información de la sesión activa dentro del sistema.
 *
 * Esta clase almacena los datos básicos del usuario autenticado, como su nombre
 * y su perfil. Se declara como un componente Spring con ámbito singleton, lo
 * que significa que solo existirá una única instancia durante toda la ejecución
 * de la aplicación.
 *
 * La sesión se utiliza para: - Mantener el estado del usuario tras iniciar
 * sesión - Determinar qué funcionalidades puede ver o usar según su perfil -
 * Mostrar información personalizada, como mensajes de bienvenida
 *
 * Forma parte del modelo de aplicación, no del modelo de dominio.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Component
@Scope("singleton")
public class Sesion {

	/**
	 * Nombre de la persona autenticada. Se usa para mostrar mensajes personalizados
	 * y para trazabilidad.
	 */
	private String nombrePersona;

	/**
	 * Perfil del usuario autenticado. Determina los permisos y funcionalidades
	 * disponibles.
	 */
	private Perfiles perfil;

	/**
	 * Constructor vacío requerido por Spring.
	 */
	public Sesion() {
	}

	/**
	 * Constructor principal para inicializar la sesión.
	 *
	 * @param nombrePersona nombre de la persona autenticada
	 * @param perfil        perfil asignado al usuario
	 */
	public Sesion(String nombrePersona, Perfiles perfil) {
		this.nombrePersona = nombrePersona;
		this.perfil = perfil;
	}

	/**
	 * Obtiene el nombre de la persona autenticada.
	 *
	 * @return nombre de la persona
	 */
	public String getNombrePersona() {
		return nombrePersona;
	}

	/**
	 * Asigna un nuevo nombre a la persona autenticada.
	 *
	 * @param nombrePersona nombre nuevo
	 */
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	/**
	 * Obtiene el perfil del usuario autenticado.
	 *
	 * @return perfil del usuario
	 */
	public Perfiles getPerfil() {
		return perfil;
	}

	/**
	 * Asigna un nuevo perfil al usuario autenticado.
	 *
	 * @param perfil perfil nuevo
	 */
	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}
}
