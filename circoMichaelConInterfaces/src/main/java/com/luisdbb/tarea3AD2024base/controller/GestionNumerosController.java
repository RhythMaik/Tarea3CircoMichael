/**
 * Controlador encargado de gestionar los números pertenecientes a un espectáculo.
 *
 * Esta pantalla forma parte del caso de uso CU5C (Gestión de Números) y permite:
 * - Crear un número nuevo dentro del espectáculo
 * - Editar un número existente
 * - Eliminar un número y reordenar los restantes
 * - Ver los artistas asignados a cada número
 * - Acceder a la pantalla de asignación de artistas
 *
 * El controlador utiliza ServicioEspectaculos para obtener y modificar los datos,
 * y ServicioNavegacion para cambiar de pantalla.
 *
 * @author MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

@Controller
public class GestionNumerosController implements Initializable {

	/**
	 * Servicio para gestionar espectáculos y sus números.
	 */
	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	/**
	 * Servicio de navegación para cambiar de pantalla.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	/**
	 * Espectáculo actualmente seleccionado para gestionar sus números. Se
	 * establece desde el controlador anterior.
	 */
	public static Espectaculo espectaculoActual;

	@FXML
	private Label lblTitulo;

	@FXML
	private TableView<Numero> tablaNumeros;
	@FXML
	private TableColumn<Numero, Integer> colOrden;
	@FXML
	private TableColumn<Numero, String> colNombre;
	@FXML
	private TableColumn<Numero, Double> colDuracion;
	@FXML
	private TableColumn<Numero, String> colArtistas;
	@FXML
	private TableColumn<Numero, Numero> colValidacion;

	/**
	 * Inicializa la vista configurando la tabla y cargando los números del
	 * espectáculo.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		configurarTabla();
		cargarNumeros();

		if (espectaculoActual != null) {
			lblTitulo.setText("Números de: " + espectaculoActual.getNombre());
		}
	}
	/**
	 * Metodo auxiliar para validar si un numero tiene un minimo de un artista
	 * @param numero
	 * @return
	 */
	private boolean validarNumero(Numero numero) {
	    // Solo valida si tiene 1 o más artistas
	    return numero.getArtistas() != null && !numero.getArtistas().isEmpty();
	}


	/**
	 * Configura las columnas de la tabla de números. Incluye una columna
	 * calculada para mostrar los artistas asignados.
	 */
	private void configurarTabla() {
		colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

		colArtistas.setCellValueFactory(c -> {
			Numero n = c.getValue();

			String nombres = n.getArtistas().stream()
					.map(a -> a.getPersona().getNombre()).sorted()
					.reduce((a, b) -> a + ", " + b).orElse("");

			return new ReadOnlyStringWrapper(nombres);
		});
		colValidacion.setCellValueFactory(
				param -> new ReadOnlyObjectWrapper<>(param.getValue()));

		colValidacion.setCellFactory(col -> new TableCell<Numero, Numero>() {
			@Override
			protected void updateItem(Numero n, boolean empty) {
				super.updateItem(n, empty);

				if (empty || n == null) {
					setText(null);
					return;
				}

				boolean ok = validarNumero(n);

				setText(ok ? "V" : "X");
				setStyle("-fx-font-size: 20px; -fx-alignment: CENTER;");
			}
		});

	}

	/**
	 * Carga los números del espectáculo actual en la tabla.
	 */
	private void cargarNumeros() {
		tablaNumeros.setItems(FXCollections.observableArrayList(
				servicioEspectaculos.obtenerNumeros(espectaculoActual)));
	}

	/**
	 * Obtiene el número seleccionado en la tabla.
	 *
	 * @return número seleccionado o null si no hay selección
	 */
	private Numero getSeleccionado() {
		Numero n = tablaNumeros.getSelectionModel().getSelectedItem();
		if (n == null) {
			mostrarError("Selecciona un número.");
		}
		return n;
	}

	// ============================================================
	// CREAR
	// ============================================================

	/**
	 * Acción del botón "Crear". Abre el formulario para crear un número nuevo.
	 */
	@FXML
	private void onCrear() {
		NumeroFormController.numeroEnEdicion = null;
		NumeroFormController.espectaculoPadre = espectaculoActual;
		navigation.goTo(FxmlView.NUMERO_FORM);
	}

	// ============================================================
	// EDITAR
	// ============================================================

	/**
	 * Acción del botón "Editar". Abre el formulario con los datos del número
	 * seleccionado.
	 */
	@FXML
	private void onEditar() {
		Numero n = getSeleccionado();
		if (n == null)
			return;

		NumeroFormController.numeroEnEdicion = n;
		NumeroFormController.espectaculoPadre = espectaculoActual;
		navigation.goTo(FxmlView.NUMERO_FORM);
	}

	// ============================================================
	// ELIMINAR
	// ============================================================

	/**
	 * Acción del botón "Eliminar". Elimina el número seleccionado y recarga la
	 * tabla.
	 */
	@FXML
	private void onEliminar() {
		Numero n = getSeleccionado();
		if (n == null)
			return;

		servicioEspectaculos.borrarNumero(n.getId());
		cargarNumeros();
		mostrarInfo("Número eliminado correctamente.");
	}

	// ============================================================
	// GESTIONAR ARTISTAS
	// ============================================================

	/**
	 * Acción del botón "Gestionar Artistas". Abre la pantalla para asignar o
	 * quitar artistas del número seleccionado.
	 */
	@FXML
	private void onGestionarArtistas() {
		Numero n = getSeleccionado();
		if (n == null)
			return;

		GestionArtistasNumeroController.numeroActual = n;
		navigation.goTo(FxmlView.GESTION_ARTISTAS_NUMERO);
	}

	// ============================================================
	// VOLVER
	// ============================================================

	/**
	 * Acción del botón "Volver". Regresa a la pantalla de gestión de
	 * espectáculos.
	 */
	@FXML
	private void onVolver() {
		navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
	}

	// ============================================================
	// UTILIDADES
	// ============================================================

	/**
	 * Muestra un mensaje de error.
	 *
	 * @param msg mensaje a mostrar
	 */
	private void mostrarError(String msg) {
		Alert a = new Alert(Alert.AlertType.ERROR);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}

	/**
	 * Muestra un mensaje informativo.
	 *
	 * @param msg mensaje a mostrar
	 */
	private void mostrarInfo(String msg) {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
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
		a.setHeaderText("Ayuda – Gestión de Números");
		a.setContentText(
				"""
						En esta pantalla puedes gestionar los números del espectáculo seleccionado.

						Acciones disponibles:

						• Crear un número nuevo dentro del espectáculo
						• Editar el número seleccionado
						• Eliminar un número (los restantes se reordenan automáticamente)
						• Ver los artistas asignados a cada número
						• Acceder a la pantalla para añadir o quitar artistas

						Recuerda seleccionar un número antes de editarlo, eliminarlo o gestionar sus artistas.
						""");
		a.showAndWait();
	}
}
