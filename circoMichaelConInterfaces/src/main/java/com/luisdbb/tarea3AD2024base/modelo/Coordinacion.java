package com.luisdbb.tarea3AD2024base.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Representa la información de coordinación asociada a una persona del circo.
 *
 * Esta clase modela el rol de Coordinación. Comparte la misma clave primaria
 * que la entidad Persona debido a la relación uno a uno.
 *
 * Un coordinador puede ser senior. Si lo es, se guarda la fecha en la que
 * obtuvo dicha categoría. Esta información se utiliza en la gestión de
 * personas, la gestión de espectáculos y la visualización de datos.
 *
 * Forma parte del modelo de dominio y se guarda en la base de datos mediante
 * JPA.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Entity
@Table(name = "coordinacion")
public class Coordinacion {

	/**
	 * Identificador del coordinador. Es el mismo ID que el de la persona asociada.
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
	 * Indica si el coordinador tiene categoría senior.
	 */
	private boolean senior;

	/**
	 * Fecha en la que obtuvo la categoría senior. Solo se usa si el campo senior es
	 * true.
	 */
	@Column(name = "fecha_senior")
	private LocalDate fechaSenior;

	/**
	 * Constructor vacío requerido por JPA.
	 */
	public Coordinacion() {
	}

	/**
	 * Constructor principal para crear un objeto Coordinacion.
	 *
	 * @param persona     persona asociada
	 * @param senior      indica si es senior
	 * @param fechaSenior fecha en la que obtuvo la categoría senior
	 */
	public Coordinacion(Persona persona, boolean senior, LocalDate fechaSenior) {
		this.persona = persona;
		this.senior = senior;
		this.fechaSenior = fechaSenior;
		// El idPersona se gestiona automáticamente por @MapsId
	}

	/**
	 * Obtiene el identificador del coordinador.
	 *
	 * @return ID de la persona asociada
	 */
	public Integer getIdPersona() {
		return idPersona;
	}

	/**
	 * Obtiene la persona asociada al coordinador.
	 *
	 * @return objeto Persona
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * Indica si el coordinador es senior.
	 *
	 * @return true si es senior, false si no lo es
	 */
	public boolean isSenior() {
		return senior;
	}

	/**
	 * Asigna si el coordinador es senior.
	 *
	 * @param senior nuevo valor del estado senior
	 */
	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	/**
	 * Obtiene la fecha en la que el coordinador obtuvo la categoría senior.
	 *
	 * @return fecha de categoría senior
	 */
	public LocalDate getFechaSenior() {
		return fechaSenior;
	}

	/**
	 * Asigna la fecha en la que el coordinador obtuvo la categoría senior.
	 *
	 * @param fechaSenior nueva fecha
	 */
	public void setFechaSenior(LocalDate fechaSenior) {
		this.fechaSenior = fechaSenior;
	}

	/**
	 * Representación en texto del objeto Coordinacion.
	 *
	 * @return cadena con información básica del coordinador
	 */
	@Override
	public String toString() {
		return "Coordinacion{" + "idPersona=" + idPersona + ", senior=" + senior + ", fechaSenior=" + fechaSenior + '}';
	}
}
