/**
* Clase  de configuracion para DB4O
*
* @author Michael Quintero Petroche
* @version 1.1
* Cosas de la tarea 4 
*/

package com.luisdbb.tarea3AD2024base.config;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class DB40Manager {
	public static final String DB_PATH = "/ficheros/log.db4o\n"; //Esto deberia ir en el aplication.properties pero para pruebas de momento me sirve
	public static ObjectContainer open() {
		return Db4oEmbedded.openFile(DB_PATH);
	}
}
