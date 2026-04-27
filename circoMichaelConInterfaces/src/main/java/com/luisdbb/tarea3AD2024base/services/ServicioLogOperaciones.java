package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;
import com.luisdbb.tarea3AD2024base.config.DB40Manager;
import com.luisdbb.tarea3AD2024base.modelo.LogOperacion;
import com.luisdbb.tarea3AD2024base.modelo.LogOperacionDB4O;
import com.luisdbb.tarea3AD2024base.modelo.TipoOperacion;

/**
 * Servicio encargado de registrar operaciones del sistema en la base de datos
 * embebida DB4O.
 *
 * Debido a las limitaciones de DB4O con tipos modernos (LocalDateTime, Enum),
 * este servicio realiza una conversión previa a String antes de almacenar
 * los datos en la base de datos.
 *
 * @autor: Michael Quintero Petroche
 * @version 1.1
 * @since 27/04/2026
 */
@Service
public class ServicioLogOperaciones {

    @Autowired
    private DB40Manager db40Manager;

    /**
     * Formato estándar para almacenar la fecha y hora como texto,
     * compatible con DB4O.
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra una operación en el historial del sistema.
     *
     * Este método:
     *  - Crea un objeto LogOperacion con los datos originales
     *  - Convierte los campos no soportados por DB4O (fecha, enum) a String
     *  - Crea un objeto LogOperacionDB4O compatible con DB4O
     *  - Lo almacena en la base de datos embebida
     *  - Gestiona commit/rollback y cierre seguro de la conexión
     *
     * @param usuario nombre del usuario que realiza la operación
     * @param tipo tipo de operación (NUEVO, ACTUALIZACION, BORRADO)
     * @param resumen descripción breve de la operación realizada
     */
    public void registrarOperacion(String usuario, TipoOperacion tipo, String resumen) {

        ObjectContainer db = null;

        try {
            // Abrir conexión con DB4O
            db = db40Manager.open();

            // Crear objeto original con tipos Java modernos
            LogOperacion log = new LogOperacion();
            log.setFechaHora(LocalDateTime.now());
            log.setUsuario(usuario);
            log.setTipoOperacion(tipo);
            log.setResumen(resumen);

            // DB4O no soporta LocalDateTime ni Enum, así que convertimos a String
            LogOperacionDB4O logDB = new LogOperacionDB4O(
                    log.getFechaHora().format(FORMATTER), // fecha como String
                    log.getUsuario(),
                    log.getTipoOperacion().name(),        // enum como String
                    log.getResumen()
            );

            // Guardar en DB4O
            db.store(logDB);
            db.commit();

        } catch (Exception e) {
            if (db != null) db.rollback();

            System.err.println("Error registrando operación DB4O: " + e.getMessage());

        } finally {
            db40Manager.close(db);
        }
    }
}
