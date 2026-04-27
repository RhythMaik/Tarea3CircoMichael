package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.LogOperacionDB4O;
import com.luisdbb.tarea3AD2024base.services.ServicioLogOperaciones;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * Controller que carga todos los logs desde DB4O con filtros en tiempo real
 * 
 * @autor Michael Quintero Petroche
 * @since 27/04/2026
 * @version 1.1
 */

@Controller
public class LogsController implements Initializable {

	@Autowired
	private ServicioLogOperaciones servicioLog;

	@Autowired
	private ServicioNavegacion navigation;

	@FXML
	private TableView<LogOperacionDB4O> tablaLogs;

	@FXML
	private TableColumn<LogOperacionDB4O, String> colFecha;

	@FXML
	private TableColumn<LogOperacionDB4O, String> colUsuario;

	@FXML
	private TableColumn<LogOperacionDB4O, String> colTipo;

	@FXML
	private TableColumn<LogOperacionDB4O, String> colResumen;

	@FXML
	private TextField txtFiltroUsuario;

	@FXML
	private TextField txtFiltroResumen;

	@FXML
	private ComboBox<String> cbFiltroTipo;

	private ObservableList<LogOperacionDB4O> listaLogs = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// Configurar columnas
		colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaHora()));

		colUsuario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsuario()));

		colTipo.setCellValueFactory(
				c -> new SimpleStringProperty(c.getValue().getTipoOperacion()));

		colResumen.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getResumen()));

		// Tipos de operación
		cbFiltroTipo.getItems().addAll("", "NUEVO", "ACTUALIZACION", "BORRADO");
		cbFiltroTipo.setValue("");

		cargarLogs();
	}

	/**
	 * Carga todos los logs desde DB4O.
	 */
	private void cargarLogs() {
		List<LogOperacionDB4O> logs = servicioLog.findAll();
		listaLogs.setAll(logs);
		tablaLogs.setItems(listaLogs);
	}

	/**
	 * Aplica los filtros de usuario, tipo y resumen.
	 */
	@FXML
	private void onFiltrar() {

		String usuario = txtFiltroUsuario.getText().toLowerCase().trim();
		String resumen = txtFiltroResumen.getText().toLowerCase().trim();
		String tipo = cbFiltroTipo.getValue();

		tablaLogs.setItems(
				listaLogs.filtered(log -> (usuario.isEmpty() || log.getUsuario().toLowerCase().contains(usuario))
						&& (resumen.isEmpty() || log.getResumen().toLowerCase().contains(resumen))
						&& (tipo.isEmpty() || log.getTipoOperacion().equals(tipo))));
	}

	/**
	 * Limpia todos los filtros.
	 */
	@FXML
	private void onLimpiar() {
		txtFiltroUsuario.clear();
		txtFiltroResumen.clear();
		cbFiltroTipo.setValue("");
		tablaLogs.setItems(listaLogs);
	}

	/**
	 * Vuelve al menú principal del admin.
	 */
	@FXML
	private void onVolver() {
		navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
	}
}
