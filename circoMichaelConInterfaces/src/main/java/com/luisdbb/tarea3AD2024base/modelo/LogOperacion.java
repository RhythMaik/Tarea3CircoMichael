package com.luisdbb.tarea3AD2024base.modelo;

import java.time.LocalDateTime;

/**
 * Representa una operación realizada en el sistema para el historial (log).
 *
 * Se persiste exclusivamente en la base de datos embebida DB4O.
 * @author Michael Quintero Petroche
 * @since 27/04/26
 * @version 1.1
 * Bueno es la tarea 4
 */
public class LogOperacion {

    private Integer id;
    private LocalDateTime fechaHora;
    private String usuario;          // nombre de usuario que realiza la operación
    private TipoOperacion tipoOperacion;
    private String resumen;          // breve descripción de la operación

    public LogOperacion() {
        // Se necesita un constructor vacio para db4o por lo visto
    }

    public LogOperacion(Integer id, LocalDateTime fechaHora,
                        String usuario, TipoOperacion tipoOperacion, String resumen) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.tipoOperacion = tipoOperacion;
        this.resumen = resumen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
}
