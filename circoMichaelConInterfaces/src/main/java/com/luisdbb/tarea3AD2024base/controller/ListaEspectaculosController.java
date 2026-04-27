/**
 * Controlador encargado de mostrar la lista pública de espectáculos
 * disponible en la pantalla inicial de la aplicación.
 *
 * Esta vista forma parte del flujo previo al inicio de sesión y permite:
 *
 * - Consultar todos los espectáculos registrados en el sistema
 * - Filtrar por nombre o por fecha
 * - Acceder a la pantalla de inicio de sesión
 *
 * A diferencia de las vistas internas del sistema, esta pantalla es
 * completamente consultiva y no permite crear, editar ni eliminar
 * espectáculos.
 *
 * @author MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class ListaEspectaculosController implements Initializable {

	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	@Autowired
	private StageManager stageManager;

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

	@FXML
	private TextField txtBuscar;
	@FXML
	private DatePicker dpFechaFiltro;
	@FXML
	private Button btnLogin;

	// Lista completa desde BD
	private List<Espectaculo> listaCompleta;

	// Lista observable filtrada
	private ObservableList<Espectaculo> listaFiltrada = FXCollections.observableArrayList();

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
	 * Carga todos los espectáculos desde la base de datos.
	 */
	private void cargarEspectaculos() {
		listaCompleta = servicioEspectaculos.findAll();
		listaFiltrada.setAll(listaCompleta);
		tablaEspectaculos.setItems(listaFiltrada);
	}

	// ============================================================
	// FILTROS
	// ============================================================

	/**
	 * Aplica el filtro de texto.
	 */
	@FXML
	private void onBuscar() {
		aplicarFiltros();
	}

	/**
	 * Aplica el filtro por fecha.
	 */
	@FXML
	private void onFiltrarFecha() {
		aplicarFiltros();
	}

	/**
	 * Limpia todos los filtros aplicados.
	 */
	@FXML
	private void onLimpiarFiltros() {
		txtBuscar.clear();
		dpFechaFiltro.setValue(null);
		listaFiltrada.setAll(listaCompleta);
	}

	/**
	 * Aplica los filtros de texto y fecha sobre la lista completa.
	 */
	private void aplicarFiltros() {

		String texto = txtBuscar.getText().toLowerCase().trim();
		LocalDate fechaFiltro = dpFechaFiltro.getValue();

		listaFiltrada.setAll(listaCompleta.stream()

				// Filtro por texto
				.filter(e -> texto.isEmpty() || e.getNombre().toLowerCase().contains(texto))

				// Filtro por fecha comprendida entre inicio y fin
				.filter(e -> {
					if (fechaFiltro == null)
						return true;

					LocalDate inicio = e.getFechaInicio();
					LocalDate fin = e.getFechaFin();

					return (inicio == null || !fechaFiltro.isBefore(inicio))
							&& (fin == null || !fechaFiltro.isAfter(fin));
				})

				.toList());
	}

	// ============================================================
	// LOGIN
	// ============================================================

	/**
	 * Acción del botón "Login". Redirige a la pantalla de inicio de sesión.
	 */
	@FXML
	private void onLogin() {
		stageManager.switchScene(FxmlView.LOGIN);
	}
}
