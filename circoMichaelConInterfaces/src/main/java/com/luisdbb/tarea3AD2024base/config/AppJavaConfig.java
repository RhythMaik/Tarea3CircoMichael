package com.luisdbb.tarea3AD2024base.config;

import java.util.ResourceBundle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Clase de configuración principal de la aplicación. Aquí se crean los objetos
 * (beans) que Spring gestionará automáticamente.
 *
 * Esta clase se encarga de: - Cargar los textos de la aplicación desde un
 * archivo de propiedades. - Crear el gestor de ventanas que usará JavaFX.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
@Configuration
public class AppJavaConfig {

	/**
	 * Carga el archivo de propiedades llamado "Bundle". Este archivo contiene los
	 * textos que se muestran en la interfaz.
	 *
	 * @return un ResourceBundle con los textos de la aplicación.
	 */
	@Bean
	public static ResourceBundle resourceBundle() {
		return ResourceBundle.getBundle("Bundle");
	}

	/**
	 * Crea el StageManager, que es el encargado de cambiar las pantallas dentro de
	 * la aplicación JavaFX.
	 *
	 * El Stage (ventana principal) se asigna más adelante en la clase Application.
	 *
	 * @param springFXMLLoader cargador de archivos FXML integrado con Spring.
	 * @return una instancia de StageManager lista para usarse.
	 */
	@Bean
	@Lazy
	public StageManager stageManager(SpringFXMLLoader springFXMLLoader) {
		return new StageManager(springFXMLLoader);
	}
}
