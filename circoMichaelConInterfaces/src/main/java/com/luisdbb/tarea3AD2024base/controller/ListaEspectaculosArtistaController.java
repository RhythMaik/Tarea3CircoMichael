/**
 * Controlador encargado de mostrar a un artista la lista completa
 * de espectáculos disponibles en el sistema.
 *
 * Esta pantalla forma parte del caso de uso CU6 (Consulta de Espectáculos
 * por parte del Artista) y permite:
 *
 * - Visualizar todos los espectáculos registrados
 * - Acceder al detalle completo de un espectáculo (CU4)
 *
 * A diferencia de la gestión realizada por coordinación o administración,
 * esta vista es exclusivamente consultiva: el artista no puede crear,
 * editar ni eliminar espectáculos.
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
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class ListaEspectaculosArtistaController implements Initializable {

	/**
	 * Servicio para obtener la lista de espectáculos.
	 */
	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	/**
	 * Servicio de navegación para cambiar de pantalla dentro del layout principal.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	@FXML
	private TableView<Espectaculo> tablaEspectaculos;
	@FXML
	private TableColumn<Espectaculo, Integer> colId;
	@FXML
	private TableColumn<Espectaculo, String> colNombre;
	@FXML
	private TableColumn<Espectaculo, String> colInicio;
	@FXML
	private TableColumn<Espectaculo, String> colFin;

	/**
	 * Inicializa la vista configurando la tabla y cargando los espectáculos.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		configurarTabla();
		cargarEspectaculos();
	}

	/**
	 * Configura las columnas de la tabla de espectáculos.
	 */
	private void configurarTabla() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
		colFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
	}

	/**
	 * Carga todos los espectáculos del sistema en la tabla.
	 */
	private void cargarEspectaculos() {
		tablaEspectaculos.setItems(FXCollections.observableArrayList(servicioEspectaculos.findAll()));
	}

	/**
	 * Acción del botón "Detalle". Abre la pantalla con la información completa del
	 * espectáculo seleccionado.
	 */
	@FXML
	private void onDetalle() {
		Espectaculo seleccionado = tablaEspectaculos.getSelectionModel().getSelectedItem();

		if (seleccionado == null) {
			mostrarError("Selecciona un espectáculo.");
			return;
		}

		DetalleEspectaculoController.espectaculoMostrado = seleccionado;
		navigation.goTo(FxmlView.DETALLE_ESPECTACULO);
	}

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
}
