package com.luisdbb.tarea3AD2024base.services;

import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.controller.MainLayoutController;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

/**
 * Servicio encargado de gestionar la navegación entre pantallas de la
 * aplicación.
 *
 * Actúa como intermediario entre la capa de servicios y el controlador
 * principal del layout (MainLayoutController), permitiendo cargar dinámicamente
 * diferentes vistas FXML dentro del contenedor principal.
 *
 * Este servicio se utiliza en todos los casos de uso que requieren cambiar de
 * pantalla, garantizando una navegación centralizada, ordenada y desacoplada.
 *
 * Forma parte del modelo de aplicación y es gestionado por Spring.
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Service
public class ServicioNavegacion {

	/**
	 * Controlador principal del layout, encargado de cargar pantallas. Se inyecta
	 * desde la capa de control cuando la aplicación inicia.
	 */
	private MainLayoutController mainController;

	/**
	 * Establece el controlador principal del layout.
	 *
	 * Este método debe ser llamado desde el MainLayoutController al inicializarse,
	 * permitiendo que el servicio pueda gestionar los cambios de pantalla.
	 *
	 * @param controller instancia de MainLayoutController
	 */
	public void setMainController(MainLayoutController controller) {
		this.mainController = controller;
	}

	/**
	 * Cambia la vista actual a la pantalla indicada.
	 *
	 * Si el controlador principal no está inicializado, la operación se ignora
	 * silenciosamente para evitar errores durante el arranque de la aplicación.
	 *
	 * @param view enumeración que representa la vista FXML a cargar
	 */
	public void goTo(FxmlView view) {
		if (mainController != null) {
			mainController.cargarPantalla(view.getFxmlFile());
		}
	}
}
