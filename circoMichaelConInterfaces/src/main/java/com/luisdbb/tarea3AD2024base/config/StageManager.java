package com.luisdbb.tarea3AD2024base.config;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;

import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase encargada de gestionar los cambios de pantalla dentro de la aplicación.
 *
 * Su función principal es cargar archivos FXML y mostrarlos en la ventana
 * principal (Stage). Actúa como un puente entre JavaFX y Spring, ya que utiliza
 * SpringFXMLLoader para cargar las vistas con sus controladores.
 *
 * Esta clase se usa para: - Cambiar de pantalla - Preparar escenas - Configurar
 * el Stage principal - Manejar errores críticos de carga
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
public class StageManager {

	/**
	 * Registro de mensajes y errores.
	 */
	private static final Logger LOG = getLogger(StageManager.class);

	/**
	 * Ventana principal de la aplicación. Se asigna desde la clase Application.
	 */
	private Stage primaryStage;

	/**
	 * Cargador FXML gestionado por Spring. Permite cargar vistas con sus
	 * controladores ya inyectados.
	 */
	private final SpringFXMLLoader springFXMLLoader;

	/**
	 * Constructor de la clase. El Stage se asigna más adelante.
	 *
	 * @param springFXMLLoader cargador FXML integrado con Spring
	 */
	public StageManager(SpringFXMLLoader springFXMLLoader) {
		this.springFXMLLoader = springFXMLLoader;
	}

	/**
	 * Asigna el Stage principal de la aplicación.
	 *
	 * @param stage ventana principal
	 */
	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;
	}

	/**
	 * Cambia la pantalla actual usando un objeto FxmlView. Este objeto contiene el
	 * archivo FXML y el título de la ventana.
	 *
	 * @param view vista que se desea mostrar
	 */
	public void switchScene(final FxmlView view) {
		Parent root = loadViewNodeHierarchy(view.getFxmlFile());
		show(root, view.getTitle());
	}

	/**
	 * Muestra una nueva escena en la ventana principal.
	 *
	 * @param rootnode nodo raíz cargado desde el FXML
	 * @param title    título de la ventana
	 */
	private void show(final Parent rootnode, String title) {
		Scene scene = prepareScene(rootnode);

		primaryStage.setTitle(title);
		primaryStage.setScene(scene);

		primaryStage.setResizable(false);
		primaryStage.setMaximized(false);

		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();

		try {
			primaryStage.show();
		} catch (Exception exception) {
			logAndExit("No se pudo mostrar la escena con título: " + title, exception);
		}
	}

	/**
	 * Prepara la escena. Si ya existe una, solo cambia su raíz.
	 *
	 * @param rootnode nodo raíz de la nueva vista
	 * @return escena lista para mostrarse
	 */
	private Scene prepareScene(Parent rootnode) {
		Scene scene = primaryStage.getScene();

		if (scene == null) {
			scene = new Scene(rootnode);
		}

		scene.setRoot(rootnode);
		return scene;
	}

	/**
	 * Carga el nodo raíz desde un archivo FXML usando SpringFXMLLoader.
	 *
	 * @param fxmlFilePath ruta del archivo FXML
	 * @return nodo raíz cargado
	 */
	private Parent loadViewNodeHierarchy(String fxmlFilePath) {
		Parent rootNode = null;

		try {
			rootNode = springFXMLLoader.load(fxmlFilePath);
			Objects.requireNonNull(rootNode, "El nodo raíz del FXML no puede ser nulo");
		} catch (Exception exception) {
			logAndExit("No se pudo cargar la vista FXML: " + fxmlFilePath, exception);
		}

		return rootNode;
	}

	/**
	 * Registra un error crítico y cierra la aplicación.
	 *
	 * @param errorMsg  mensaje de error
	 * @param exception excepción capturada
	 */
	private void logAndExit(String errorMsg, Exception exception) {
		LOG.error(errorMsg, exception, exception.getCause());
		Platform.exit();
	}
}
