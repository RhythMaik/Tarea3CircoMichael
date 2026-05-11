package com.luisdbb.tarea3AD2024base.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Incidencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime fechaHora;

	@Enumerated(EnumType.STRING)
	private TipoIncidencia tipo;

	@Column(length = 1000, nullable = false)
	private String descripcion;

	private boolean resuelta = false;

	@Column(nullable = false)
	private String usuarioReporta; 

	private Long idEspectaculo;

	private Long idNumero;

	public Incidencia() {
	}

	public Incidencia(TipoIncidencia tipo, String descripcion, String usuarioReporta, Long idEspectaculo,
			Long idNumero) {

		this.fechaHora = LocalDateTime.now();
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.usuarioReporta = usuarioReporta;
		this.idEspectaculo = idEspectaculo;
		this.idNumero = idNumero;
		this.resuelta = false;
	}

	// GETTERS & SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public TipoIncidencia getTipo() {
		return tipo;
	}

	public void setTipo(TipoIncidencia tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isResuelta() {
		return resuelta;
	}

	public void setResuelta(boolean resuelta) {
		this.resuelta = resuelta;
	}

	public String getUsuarioReporta() {
		return usuarioReporta;
	}

	public void setUsuarioReporta(String usuarioReporta) {
		this.usuarioReporta = usuarioReporta;
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(Long idNumero) {
		this.idNumero = idNumero;
	}
}
