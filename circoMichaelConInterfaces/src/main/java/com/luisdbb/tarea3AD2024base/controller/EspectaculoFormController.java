package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.repositorios.CoordinacionRepository;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * Controlador encargado de gestionar el formulario de creación y edición de
 * espectáculos.
 *
 * Implementa la lógica de los casos de uso: - CU5A: Crear espectáculo - CU5B:
 * Editar espectáculo
 *
 * Funciones principales: - Cargar datos del espectáculo en edición - Validar
 * nombre y fechas según reglas de negocio - Asignar coordinador automáticamente
 * (perfil COORDINACION) - Permitir selección de coordinador (perfil ADMIN) -
 * Guardar cambios mediante ServicioEspectaculos
 *
 * La vista se adapta según el perfil del usuario: - COORDINACION: no puede
 * elegir coordinador - ADMIN: puede seleccionar cualquier coordinador
 * disponible
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Controller
public class EspectaculoFormController implements Initializable {

	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	@Autowired
	private ServicioPersonas servicioPersonas;

	@Autowired
	private ServicioNavegacion navigation;

	@Autowired
	private CoordinacionRepository coordinacionRepository;

	@Autowired
	private Sesion sesion;

	/**
	 * Espectáculo que se está editando. Si es null, el formulario funciona en modo
	 * creación.
	 */
	public static Espectaculo espectaculoEnEdicion;

	@FXML
	private Label lblTitulo;
	@FXML
	private TextField txtNombre;
	@FXML
	private DatePicker dpInicio;
	@FXML
	private DatePicker dpFin;

	@FXML
	private Label lblCoordinador;
	@FXML
	private ComboBox<Persona> cbCoordinador;

	/**
	 * Inicializa la vista configurando los campos según el perfil del usuario y
	 * cargando los datos del espectáculo si se está editando.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// ADMIN → cargar lista de coordinadores
		if (sesion.getPerfil() != Perfiles.COORDINACION) {
			List<Coordinacion> coords = servicioPersonas.findAllCoordinaciones();
			for (Coordinacion c : coords) {
				cbCoordinador.getItems().add(c.getPersona());
			}
		}
		// COORDINACION → ocultar selector de coordinador
		else {
			lblCoordinador.setVisible(false);
			lblCoordinador.setManaged(false);

			cbCoordinador.setVisible(false);
			cbCoordinador.setManaged(false);
		}

		if (espectaculoEnEdicion != null) {
			cargarDatos();
		}
	}

	/**
	 * Carga los datos del espectáculo en edición en los campos del formulario.
	 */
	private void cargarDatos() {
		lblTitulo.setText("Editar Espectáculo");

		txtNombre.setText(espectaculoEnEdicion.getNombre());
		dpInicio.setValue(espectaculoEnEdicion.getFechaInicio());
		dpFin.setValue(espectaculoEnEdicion.getFechaFin());

		Persona coord = espectaculoEnEdicion.getCoordinador().getPersona();
		cbCoordinador.setValue(coord);

		// COORDINACION no puede cambiar coordinador
		if (sesion.getPerfil() == Perfiles.COORDINACION) {
			cbCoordinador.setDisable(true);
		}
	}

	/**
	 * Acción del botón Guardar.
	 *
	 * Valida los datos introducidos y crea o actualiza el espectáculo según
	 * corresponda. Aplica las reglas de negocio del caso de uso CU5A/CU5B.
	 */
	@FXML
	private void onGuardar() {

		String nombre = txtNombre.getText();
		LocalDate inicio = dpInicio.getValue();
		LocalDate fin = dpFin.getValue();

		// VALIDACIONES CU5A
		if (nombre.isBlank()) {
			mostrarError("El nombre no puede estar vacío.");
			return;
		}

		if (nombre.length() > 25) {
			mostrarError("El nombre no puede superar los 25 caracteres.");
			return;
		}

		if (inicio == null || fin == null) {
			mostrarError("Debes indicar fecha de inicio y fin.");
			return;
		}

		if (!inicio.isBefore(fin)) {
			mostrarError("La fecha de inicio debe ser anterior a la fecha de fin.");
			return;
		}

		if (inicio.plusYears(1).isBefore(fin)) {
			mostrarError("La duración del espectáculo no puede superar 1 año.");
			return;
		}

		try {
			// CREAR
			if (espectaculoEnEdicion == null) {

				// COORDINACION → se asigna a sí mismo
				if (sesion.getPerfil() == Perfiles.COORDINACION) {

					Persona p = servicioPersonas.findByNombre(sesion.getNombrePersona());
					Coordinacion coord = coordinacionRepository.findByIdPersona(p.getId());

					if (coord == null) {
						mostrarError("No se encontró tu entidad de coordinación.");
						return;
					}

					servicioEspectaculos.crearEspectaculo(nombre, inicio, fin, p);
				}
				// ADMIN → selecciona coordinador
				else {
					Persona coord = cbCoordinador.getValue();
					if (coord == null) {
						mostrarError("Selecciona un coordinador.");
						return;
					}

					servicioEspectaculos.crearEspectaculo(nombre, inicio, fin, coord);
				}
			}

			// EDITAR
			else {
				servicioEspectaculos.actualizarNombre(espectaculoEnEdicion, nombre);
				servicioEspectaculos.actualizarFechas(espectaculoEnEdicion, inicio, fin);

				// ADMIN puede cambiar coordinador
				if (sesion.getPerfil() != Perfiles.COORDINACION) {
					Persona coord = cbCoordinador.getValue();
					servicioEspectaculos.actualizarCoordinador(espectaculoEnEdicion, coord.getId());
				}
			}

		} catch (Exception e) {
			mostrarError(e.getMessage());
			return;
		}

		espectaculoEnEdicion = null;
		navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
	}

	/**
	 * Acción del botón Cancelar.
	 *
	 * Limpia la referencia estática y vuelve a la pantalla de gestión.
	 */
	@FXML
	private void onCancelar() {
		espectaculoEnEdicion = null;
		navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
	}

	/**
	 * Muestra un mensaje de error en un cuadro de diálogo.
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
