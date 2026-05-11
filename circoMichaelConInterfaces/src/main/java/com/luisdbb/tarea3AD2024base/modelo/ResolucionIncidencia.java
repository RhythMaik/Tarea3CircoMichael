package com.luisdbb.tarea3AD2024base.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class ResolucionIncidencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime fechaHoraResolucion;

	@Column(length = 1000, nullable = false)
	private String accionesRealizadas;

	@Column(nullable = false)
	private String usuarioResuelve; // ← nombrePersona

	@ManyToOne(optional = false)
	private Incidencia incidencia;

	public ResolucionIncidencia() {
	}

	public ResolucionIncidencia(Incidencia incidencia, String accionesRealizadas, String usuarioResuelve) {

		this.incidencia = incidencia;
		this.accionesRealizadas = accionesRealizadas;
		this.usuarioResuelve = usuarioResuelve;
		this.fechaHoraResolucion = LocalDateTime.now();
	}

	// GETTERS & SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaHoraResolucion() {
		return fechaHoraResolucion;
	}

	public void setFechaHoraResolucion(LocalDateTime fechaHoraResolucion) {
		this.fechaHoraResolucion = fechaHoraResolucion;
	}

	public String getAccionesRealizadas() {
		return accionesRealizadas;
	}

	public void setAccionesRealizadas(String accionesRealizadas) {
		this.accionesRealizadas = accionesRealizadas;
	}

	public String getUsuarioResuelve() {
		return usuarioResuelve;
	}

	public void setUsuarioResuelve(String usuarioResuelve) {
		this.usuarioResuelve = usuarioResuelve;
	}

	public Incidencia getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
}
