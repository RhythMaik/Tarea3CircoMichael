/**
 * Controlador encargado de gestionar la lista de personas del sistema.
 *
 * Esta pantalla forma parte del caso de uso CU3 (Gestión de Personas) y permite:
 * - Crear una nueva persona (CU3A)
 * - Editar una persona existente (CU3B)
 * - Eliminar una persona junto con sus entidades asociadas (CU3C)
 * - Ver el detalle completo de una persona
 *
 * El controlador utiliza ServicioPersonas como capa de negocio para realizar
 * operaciones CRUD y CredencialesRepository para obtener el perfil asociado
 * a cada persona.
 *
 * Autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.repositorios.CredencialesRepository;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class GestionPersonasController implements Initializable {

	/**
	 * Servicio de negocio para gestionar personas y credenciales.
	 */
	@Autowired
	private ServicioPersonas servicioPersonas;
	
	@Autowired
	private Sesion sesion;

	/**
	 * Repositorio para obtener el perfil asociado a cada persona.
	 */
	@Autowired
	private CredencialesRepository credencialesRepository;

	/**
	 * Servicio de navegación para cambiar de pantalla.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	@FXML
	private TableView<Persona> tablaPersonas;
	@FXML
	private TableColumn<Persona, String> colNombre;
	@FXML
	private TableColumn<Persona, String> colEmail;
	@FXML
	private TableColumn<Persona, String> colPerfil;

	@FXML
	private Button btnCrear, btnEditar, btnEliminar, btnVer, btnVolver;

	private ObservableList<Persona> listaPersonas;

	/**
	 * Inicializa la vista configurando la tabla y cargando la lista de personas.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		configurarTabla();
		cargarPersonas();
	}

	/**
	 * Configura las columnas de la tabla, incluyendo una columna calculada para
	 * mostrar el perfil de cada persona.
	 */
	private void configurarTabla() {
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

		colPerfil.setCellValueFactory(cellData -> {
			Persona p = cellData.getValue();
			Credenciales c = credencialesRepository.findByIdPersona(p.getId());

			String perfil = (c != null) ? c.getPerfil().name() : "N/A";

			return javafx.beans.property.SimpleStringProperty
					.stringExpression(javafx.beans.binding.Bindings.createStringBinding(() -> perfil));
		});
	}

	/**
	 * Carga todas las personas del sistema en la tabla.
	 */
	private void cargarPersonas() {
		listaPersonas = FXCollections.observableArrayList(servicioPersonas.findAll());
		tablaPersonas.setItems(listaPersonas);
	}

	// ============================================================
	// CREAR PERSONA
	// ============================================================

	/**
	 * Acción del botón "Crear". Abre el formulario en modo creación.
	 */
	@FXML
	private void onCrear() {
		PersonaFormController.personaEnEdicion = null;
		navigation.goTo(FxmlView.PERSONA_FORM);
	}

	// ============================================================
	// EDITAR PERSONA
	// ============================================================

	/**
	 * Acción del botón "Editar". Abre el formulario con los datos de la persona
	 * seleccionada.
	 */
	@FXML
	private void onEditar() {
		Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
		if (seleccionada == null) {
			mostrarError("Selecciona una persona para editar.");
			return;
		}

		PersonaFormController.personaEnEdicion = seleccionada;
		navigation.goTo(FxmlView.PERSONA_FORM);
	}

	// ============================================================
	// ELIMINAR PERSONA
	// ============================================================

	/**
	 * Acción del botón "Eliminar". Elimina la persona seleccionada y recarga la
	 * tabla.
	 */
	@FXML
	private void onEliminar() {
		Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
		if (seleccionada == null) {
			mostrarError("Selecciona una persona para eliminar.");
			return;
		}

		servicioPersonas.delete(seleccionada.getId(), sesion.getNombrePersona());
		cargarPersonas();
		mostrarInfo("Eliminado", "La persona ha sido eliminada correctamente.");
	}

	// ============================================================
	// VER DETALLES
	// ============================================================

	/**
	 * Acción del botón "Ver". Abre la pantalla con la información completa de la
	 * persona seleccionada.
	 */
	@FXML
	private void onVer() {
		Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
		if (seleccionada == null) {
			mostrarError("Selecciona una persona para ver detalles.");
			return;
		}

		DetallePersonaController.personaMostrada = seleccionada;
		navigation.goTo(FxmlView.DETALLE_PERSONA);
	}

	// ============================================================
	// VOLVER
	// ============================================================

	/**
	 * Acción del botón "Volver". Regresa a la pantalla principal de gestión de
	 * personas.
	 */
	@FXML
	private void onVolver() {
		navigation.goTo(FxmlView.GESTION_PERSONAS);
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
	 * @param titulo título del cuadro
	 * @param msg    mensaje a mostrar
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
		a.setHeaderText("Ayuda – Gestión de Personas");
		a.setContentText("""
				En esta pantalla puedes gestionar todas las personas registradas en el sistema.

				Acciones disponibles:

				• Crear una nueva persona
				• Editar la persona seleccionada
				• Eliminar una persona (junto con sus entidades asociadas)
				• Ver el detalle completo de una persona
				• Consultar su perfil (ARTISTA, COORDINADOR, ADMIN…)

				Notas importantes:

				• Debes seleccionar una persona antes de editar, eliminar o ver detalles.
				• No podrás eliminar personas que estén asociadas a espectáculos o números.
				""");
		a.showAndWait();
	}
}
