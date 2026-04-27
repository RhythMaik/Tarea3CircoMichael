package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;
import com.luisdbb.tarea3AD2024base.config.DB40Manager;
import com.luisdbb.tarea3AD2024base.modelo.LogOperacion;
import com.luisdbb.tarea3AD2024base.modelo.TipoOperacion;

/**
 * Clase encargada de el registro de operaciones en la base de datos
 * DB4O, como solo es registrar operaciones pues solo consiste en un metodo
 * 
 * @author Michael Quintero Petroche
 * @since 27/04/2026
 * @version 1.1
 * Lo de la tarea 4
 */

@Service
public class ServicioLogOperaciones {

    @Autowired
    private DB40Manager db40Manager;

    /**
     * Registra una operación en la base de datos DB4O.
     *
     * @param usuario nombre del usuario que realiza la operación
     * @param tipo tipo de operación (NUEVO, ACTUALIZACION, BORRADO)
     * @param resumen descripción breve de la operación
     */
    public void registrarOperacion(String usuario, TipoOperacion tipo, String resumen) {

        ObjectContainer db = null;

        try {
            db = db40Manager.open();

            LogOperacion log = new LogOperacion();
            log.setFechaHora(LocalDateTime.now());
            log.setUsuario(usuario);
            log.setTipoOperacion(tipo);
            log.setResumen(resumen);

            db.store(log);
            db.commit();

        } catch (Exception e) {
            if (db != null) {
                db.rollback();
            }
            System.err.println("Error registrando operación en DB4O: " + e.getMessage());

        } finally {
            db40Manager.close(db);
        }
    }
}
