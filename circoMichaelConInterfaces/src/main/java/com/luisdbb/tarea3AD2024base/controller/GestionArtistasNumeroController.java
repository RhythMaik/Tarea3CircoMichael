package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

/**
 * Controlador encargado de gestionar la asignación de artistas a un número.
 *
 * Esta pantalla forma parte del caso de uso CU5C (Asignar artistas a números) y
 * permite: - Mostrar todos los artistas del sistema - Mostrar los artistas ya
 * asignados al número - Añadir o quitar artistas del número seleccionado
 *
 * Utiliza ServicioEspectaculos para modificar las asignaciones y
 * ServicioPersonas para obtener la lista completa de artistas disponibles.
 *
 * @autor: MichaelQP 
 * @version 1.1
 * @since 2026
 */
@Controller
public class GestionArtistasNumeroController implements Initializable {

	/**
	 * Servicio para gestionar espectáculos y asignaciones de artistas.
	 */
	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	/**
	 * Servicio para obtener información de personas y artistas.
	 */
	@Autowired
	private ServicioPersonas servicioPersonas;

	/**
	 * Servicio de navegación para cambiar de pantalla.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	/**
	 * Número actualmente seleccionado para asignar o quitar artistas. Se establece
	 * desde el controlador anterior.
	 */
	public static Numero numeroActual;

	@FXML
	private ListView<Persona> listaDisponibles;
	@FXML
	private ListView<Persona> listaAsignados;

	/**
	 * Inicializa la vista cargando las listas de artistas disponibles y asignados.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cargarListas();
	}

	/**
	 * Carga las listas de artistas: - Disponibles: todos los artistas del sistema
	 * que no están asignados al número - Asignados: artistas que ya participan en
	 * el número
	 */
	private void cargarListas() {

		List<Persona> artistasSistema = servicioPersonas.findAllArtistas();
		List<Artista> artistasNumero = servicioEspectaculos.obtenerArtistasAsignados(numeroActual.getId());

		List<Persona> asignados = artistasNumero.stream().map(Artista::getPersona).toList();

		List<Persona> disponibles = artistasSistema.stream().filter(p -> !asignados.contains(p)).toList();

		listaDisponibles.setItems(FXCollections.observableArrayList(disponibles));
		listaAsignados.setItems(FXCollections.observableArrayList(asignados));
	}

	/**
	 * Acción del botón "Añadir".
	 *
	 * Asigna el artista seleccionado al número actual.
	 */
	@FXML
	private void onAnadir() {
		Persona p = listaDisponibles.getSelectionModel().getSelectedItem();
		if (p == null)
			return;

		servicioEspectaculos.anadirArtistaANumero(numeroActual.getId(), p.getId());
		cargarListas();
	}

	/**
	 * Acción del botón "Quitar".
	 *
	 * Elimina el artista seleccionado del número actual.
	 */
	@FXML
	private void onQuitar() {
		Persona p = listaAsignados.getSelectionModel().getSelectedItem();
		if (p == null)
			return;

		servicioEspectaculos.quitarArtistaDeNumero(numeroActual.getId(), p.getId());
		cargarListas();
	}

	/**
	 * Acción del botón "Volver".
	 *
	 * Regresa a la pantalla de gestión de números.
	 */
	@FXML
	private void onVolver() {
		navigation.goTo(FxmlView.GESTION_NUMEROS);
	}

	// ============================================================
	// AYUDA
	// ============================================================

	/**
	 * Muestra una ventana de ayuda explicando el funcionamiento de la pantalla.
	 */
	@FXML
	private void onAyuda() {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setHeaderText("Ayuda – Asignación de Artistas a Número");
		a.setContentText("""
				En esta pantalla puedes gestionar qué artistas participan en el número seleccionado.

				Acciones disponibles:

				• Ver todos los artistas disponibles en el sistema
				• Ver los artistas ya asignados al número
				• Añadir un artista al número (botón “Añadir →”)
				• Quitar un artista del número (botón “← Quitar”)

				Notas importantes:

				• Solo pueden asignarse personas que sean artistas.
				• Debes seleccionar un artista en la lista correspondiente antes de añadirlo o quitarlo.
				• Los cambios se guardan automáticamente al añadir o quitar artistas.
				""");
		a.showAndWait();
	}
}
