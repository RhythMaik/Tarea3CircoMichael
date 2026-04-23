/**
* Clase LogOperacion.java
*
*@author Michael Quintero Petroche
*@version 1.0
*/

package com.luisdbb.tarea3AD2024base.modelo;

import java.time.LocalDateTime;

public class LogOperacion {
	private Integer id;
	private LocalDateTime fechaHora;
	private String nombreUsuario; //Es la carga ligera para meter del modelo User
	private TipoOperacion tipoOperacion;
	private String resumen; //Es una pequeña descripcion que deberia de inicar el nombre de las entidades afectadas
	
	public LogOperacion(Integer id, LocalDateTime fechaHora,
			String nombreUsuario, TipoOperacion tipoOperacion, String resumen) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.nombreUsuario = nombreUsuario;
		this.tipoOperacion = tipoOperacion;
		this.resumen = resumen;
	}

	public LogOperacion() {
		super();
	}
		
		
	
}
