package com.luisdbb.tarea3AD2024base.config;

import org.springframework.stereotype.Component;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

/**
 * Gestor centralizado para la base de datos DB4O.
 * Se encarga de abrir y cerrar el fichero log.db4o.
 *  @author Michael Quintero Petroche
 *  @since 27/04/2026
 *  @version 1.1 
 *  Es de la tarea 4 
 */
@Component
public class DB40Manager {

    // Ruta de donde esta el fichero
    private static final String DB_PATH = "ficheros/log.db4o";

    /**
     * Abre la base de datos DB4O.
     */
    public ObjectContainer open() {
        return Db4oEmbedded.openFile(DB_PATH);
    }

    /**
     * Cierra la base de datos DB4O en caso de que este abierta
     */
    public void close(ObjectContainer db) {
        if (db != null) {
            db.close();
        }
    }
}
