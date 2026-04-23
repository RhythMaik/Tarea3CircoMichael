/**
 * Controlador principal del layout maestro de la aplicación.
 *
 * Esta clase gestiona:
 * - El mensaje de bienvenida con el nombre del usuario autenticado
 * - La configuración del menú lateral según el perfil del usuario
 * - La carga dinámica de pantallas dentro del contenedor central
 * - La navegación interna entre módulos sin cambiar de escena
 * - El cierre de sesión y retorno a la pantalla pública
 *
 * Este controlador es el núcleo del flujo de trabajo tras el login
 * y actúa como “router” interno del sistema, permitiendo que el resto
 * de controladores carguen vistas dentro del layout principal.
 *
 * @author MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.SpringFXMLLoader;
import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

@Controller
public class MainLayoutController {

	@FXML
	private Label lblBienvenida;
	@FXML
	private StackPane contenedorPantallas;

	@FXML
	private Button btnPersonas;
	@FXML
	private Button btnEspectaculos;
	@FXML
	private Button btnVerEspectaculos;
	@FXML
	private Button btnFichaArtista;

	/**
	 * Cargador FXML integrado con Spring.
	 */
	@Autowired
	private SpringFXMLLoader loader;

	/**
	 * Sesión activa del usuario.
	 */
	@Autowired
	private Sesion sesion;

	/**
	 * Gestor de escenas principal.
	 */
	@Autowired
	private StageManager stageManager;

	/**
	 * Servicio de navegación para cargar pantallas dentro del layout.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	/**
	 * Inicializa el layout maestro: - Muestra el mensaje de bienvenida - Configura
	 * el menú según el rol - Carga la pantalla inicial correspondiente al perfil
	 */
	@FXML
	public void initialize() {

		lblBienvenida.setText("Bienvenido, " + sesion.getNombrePersona());
		navigation.setMainController(this);

		configurarMenuSegunRol();

		switch (sesion.getPerfil()) {

		case ARTISTA:
			cargarPantalla(FxmlView.LISTA_ESPECTACULOS_ARTISTA.getFxmlFile());
			break;

		case COORDINACION:
			cargarPantalla(FxmlView.GESTION_ESPECTACULOS.getFxmlFile());
			break;

		case ADMIN:
		default:
			cargarPantalla(FxmlView.GESTION_PERSONAS.getFxmlFile());
			break;
		}
	}

	/**
	 * Configura la visibilidad del menú lateral según el perfil del usuario.
	 */
	private void configurarMenuSegunRol() {

		Perfiles perfil = sesion.getPerfil();

		switch (perfil) {

		case ADMIN:
			btnPersonas.setVisible(true);
			btnEspectaculos.setVisible(true);

			btnVerEspectaculos.setVisible(false);
			btnVerEspectaculos.setManaged(false);

			btnFichaArtista.setVisible(false);
			btnFichaArtista.setManaged(false);
			break;

		case COORDINACION:
			btnPersonas.setVisible(false);
			btnPersonas.setManaged(false);

			btnEspectaculos.setVisible(true);

			btnVerEspectaculos.setVisible(false);
			btnVerEspectaculos.setManaged(false);

			btnFichaArtista.setVisible(false);
			btnFichaArtista.setManaged(false);
			break;

		case ARTISTA:
			btnPersonas.setVisible(false);
			btnPersonas.setManaged(false);

			btnEspectaculos.setVisible(false);
			btnEspectaculos.setManaged(false);

			btnVerEspectaculos.setVisible(true);
			btnVerEspectaculos.setManaged(true);

			btnFichaArtista.setVisible(true);
			btnFichaArtista.setManaged(true);
			break;
		}
	}

	/**
	 * Carga una vista FXML dentro del contenedor central del layout.
	 *
	 * @param fxml ruta del archivo FXML a cargar
	 */
	public void cargarPantalla(String fxml) {
		try {
			Parent vista = loader.load(fxml);
			contenedorPantallas.getChildren().setAll(vista);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ============================================================
	// ACCIONES DEL MENÚ
	// ============================================================

	@FXML
	private void goPersonas() {
		cargarPantalla(FxmlView.GESTION_PERSONAS.getFxmlFile());
	}

	@FXML
	private void goEspectaculos() {
		cargarPantalla(FxmlView.GESTION_ESPECTACULOS.getFxmlFile());
	}

	@FXML
	private void goVerEspectaculos() {
		cargarPantalla(FxmlView.LISTA_ESPECTACULOS_ARTISTA.getFxmlFile());
	}

	@FXML
	private void goFichaArtista() {
		cargarPantalla(FxmlView.FICHA_ARTISTA.getFxmlFile());
	}

	/**
	 * Cierra la sesión y vuelve a la pantalla pública.
	 */
	@FXML
	private void logout() {
		stageManager.switchScene(FxmlView.LISTA_ESPECTACULOS);
	}
}
