package com.luisdbb.tarea3AD2024base.modelo;
/*
 * Clase auxiliar para hacer el guardado al fichero de DB4O
 * ya que DB40 es antiguo y no es capaz de guardar tipos modernos
 * 
 * @author Michael Quintero Petroche
 * @since 27/04/2026
 * @version 1.1
 */
public class LogOperacionDB4O {

	private String fechaHora;
	private String usuario;
	private String tipoOperacion;
	private String resumen;

	public LogOperacionDB4O() {
	}

	public LogOperacionDB4O(String fechaHora, String usuario, String tipoOperacion, String resumen) {
		this.fechaHora = fechaHora;
		this.usuario = usuario;
		this.tipoOperacion = tipoOperacion;
		this.resumen = resumen;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
}
