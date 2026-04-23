/**
* Clase DB40Manager.java
*
*@author Michael Quintero Petroche
*@version 1.0
*/

package com.luisdbb.tarea3AD2024base.config;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class DB40Manager {
	public static final String DB_PATH = "log.db4o"; //Esto deberia ir en el aplication.properties pero para pruebas de momento me sirve
	public static ObjectContainer open() {
		return Db4oEmbedded.openFile(DB_PATH);
	}
}
