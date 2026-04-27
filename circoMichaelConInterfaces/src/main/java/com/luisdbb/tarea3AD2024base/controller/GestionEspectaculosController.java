package com.luisdbb.tarea3AD2024base.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * Controlador encargado de gestionar la pantalla principal de administración de
 * espectáculos.
 *
 * Esta vista forma parte de los casos de uso: - CU5A: Crear espectáculo - CU5B:
 * Editar espectáculo - CU5C: Gestionar números y artistas - CU4: Visualización
 * completa de un espectáculo
 *
 * Funciones principales: - Mostrar todos los espectáculos registrados - Filtrar
 * por nombre o por fecha - Crear, editar y eliminar espectáculos - Acceder a la
 * gestión de números - Exportar todos los espectáculos en formato JSON
 *
 * Utiliza ServicioEspectaculos para las operaciones CRUD y ServicioNavegacion
 * para cambiar de pantalla.
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Controller
public class GestionEspectaculosController implements Initializable {

	@Autowired
	private Sesion sesion;

	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	@Autowired
	private ServicioNavegacion navigation;

	@FXML
	private TableView<Espectaculo> tablaEspectaculos;
	@FXML
	private TableColumn<Espectaculo, Integer> colId;
	@FXML
	private TableColumn<Espectaculo, String> colNombre;
	@FXML
	private TableColumn<Espectaculo, LocalDate> colInicio;
	@FXML
	private TableColumn<Espectaculo, LocalDate> colFin;
	@FXML
	private TableColumn<Espectaculo, Espectaculo> colValidacion;

	// Campos de filtrado
	@FXML
	private TextField txtBuscar;
	@FXML
	private DatePicker dpFechaFiltro;

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
	 * Metodo auxiliar para validar que los espectaculos tengan un minimo de 3
	 * 
	 * @param espectaculo
	 * @return falso o true dependiendo de si cumple la condicion
	 */

	private boolean validarEspectaculo(Espectaculo espectaculo) {
		if (espectaculo.getNumeros() == null) {
			return false;
		}
		return espectaculo.getNumeros().size() >= 3;
	}

	/**
	 * Configura las columnas de la tabla de espectáculos.
	 */
	private void configurarTabla() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
		colFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
		colValidacion.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

		colValidacion.setCellFactory(col -> new TableCell<Espectaculo, Espectaculo>() {
			@Override
			protected void updateItem(Espectaculo espec, boolean empty) {
				super.updateItem(espec, empty);

				if (empty || espec == null) {
					setText(null);
					return;
				}

				boolean ok = validarEspectaculo(espec);

				setText(ok ? "V" : "X");
				setStyle("-fx-font-size: 20px; -fx-alignment: CENTER;");
			}
		});

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
	// CRUD Y ACCIONES
	// ============================================================

	/**
	 * Devuelve el espectáculo seleccionado en la tabla.
	 *
	 * @return espectáculo seleccionado o null si no hay selección
	 */
	private Espectaculo getSeleccionado() {
		Espectaculo esp = tablaEspectaculos.getSelectionModel().getSelectedItem();
		if (esp == null) {
			mostrarError("Selecciona un espectáculo.");
		}
		return esp;
	}

	/**
	 * Acción del botón "Crear". Abre el formulario en modo creación.
	 */
	@FXML
	private void onCrear() {
		EspectaculoFormController.espectaculoEnEdicion = null;
		navigation.goTo(FxmlView.ESPECTACULO_FORM);
	}

	/**
	 * Acción del botón "Editar". Abre el formulario con los datos del espectáculo
	 * seleccionado.
	 */
	@FXML
	private void onEditar() {
		Espectaculo esp = getSeleccionado();
		if (esp == null)
			return;

		EspectaculoFormController.espectaculoEnEdicion = esp;
		navigation.goTo(FxmlView.ESPECTACULO_FORM);
	}

	/**
	 * Acción del botón "Eliminar". Elimina el espectáculo seleccionado.
	 */
	@FXML
	private void onEliminar() {
		Espectaculo esp = getSeleccionado();
		if (esp == null)
			return;

		servicioEspectaculos.delete(esp.getId(), sesion.getNombrePersona());
		cargarEspectaculos();

		mostrarInfo("Eliminado", "El espectáculo ha sido eliminado correctamente.");
	}

	/**
	 * Acción del botón "Ver detalle". Abre la pantalla de detalle del espectáculo
	 * seleccionado.
	 */
	@FXML
	private void onVerDetalle() {
		Espectaculo esp = getSeleccionado();
		if (esp == null)
			return;

		DetalleEspectaculoController.espectaculoMostrado = esp;
		navigation.goTo(FxmlView.DETALLE_ESPECTACULO);
	}

	/**
	 * Acción del botón "Gestionar números". Abre la pantalla de gestión de números
	 * del espectáculo seleccionado.
	 */
	@FXML
	private void onGestionarNumeros() {
		Espectaculo esp = getSeleccionado();
		if (esp == null)
			return;

		GestionNumerosController.espectaculoActual = esp;
		navigation.goTo(FxmlView.GESTION_NUMEROS);
	}

	/**
	 * Acción del botón "Volver".
	 */
	@FXML
	private void onVolver() {
		navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
	}

	// ============================================================
	// EXPORTAR JSON
	// ============================================================

	/**
	 * Exporta todos los espectáculos del sistema en formato JSON.
	 */
	@FXML
	private void onExportarJSON() {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Exportar espectáculos");
		alert.setContentText("¿Deseas exportar todos los espectáculos en formato JSON?");

		if (alert.showAndWait().get() != ButtonType.OK) {
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar espectáculos");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo JSON", "*.json"));
		fileChooser.setInitialFileName("espectaculos.json");

		File destino = fileChooser.showSaveDialog(null);

		if (destino == null) {
			return;
		}

		try {
			servicioEspectaculos.exportarEspectaculosJSON(destino);
			mostrarInfo("Exportado", "Los espectáculos se han exportado correctamente.");
		} catch (Exception e) {
			mostrarError("No se pudo exportar los espectáculos: " + e.getMessage());
		}
	}

	// ============================================================
	// UTILIDADES
	// ============================================================

	/**
	 * Muestra un mensaje de error en un cuadro de diálogo.
	 */
	private void mostrarError(String msg) {
		Alert a = new Alert(Alert.AlertType.ERROR);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}

	/**
	 * Muestra un mensaje informativo en un cuadro de diálogo.
	 */
	private void mostrarInfo(String titulo, String msg) {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setHeaderText(titulo);
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
		a.setHeaderText("Ayuda – Gestión de Espectáculos");
		a.setContentText("""
				En esta pantalla puedes realizar las siguientes acciones:

				• Crear un nuevo espectáculo
				• Editar el espectáculo seleccionado
				• Eliminarlo
				• Ver su detalle completo
				• Gestionar sus números
				• Filtrar por nombre o por fecha
				• Exportar todos los espectáculos en formato JSON

				Recuerda seleccionar un espectáculo antes de editar, eliminar, ver detalle o gestionar números.
				""");
		a.showAndWait();
	}
}
