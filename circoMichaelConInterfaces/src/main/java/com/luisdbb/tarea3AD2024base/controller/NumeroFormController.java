/**
 * Controlador encargado de gestionar el formulario de creación y edición
 * de números dentro de un espectáculo.
 *
 * Esta pantalla forma parte del caso de uso CU5B (Gestión de Números) y permite:
 * - Crear un número nuevo dentro de un espectáculo
 * - Editar un número existente
 *
 * El controlador aplica todas las validaciones exigidas por el caso de uso:
 * - Nombre obligatorio
 * - Orden entero mayor o igual que 1
 * - Duración numérica mayor que 0
 * - Duración con formato X.0 o X.5
 * - Orden correlativo al crear un número nuevo
 *
 * Utiliza ServicioEspectaculos como capa de negocio para persistir los cambios
 * y ServicioNavegacion para volver a la pantalla de gestión de números.
 *
 * @author MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Controller
public class NumeroFormController implements Initializable {

	@Autowired
	private Sesion sesion;

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
	 * Número actualmente en edición. Si es null, el formulario funciona en modo
	 * creación.
	 */
	public static Numero numeroEnEdicion;

	/**
	 * Espectáculo al que pertenece el número que se está creando o editando.
	 */
	public static Espectaculo espectaculoPadre;

	@FXML
	private Label lblTitulo;
	@FXML
	private TextField txtOrden;
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtDuracion;

	/**
	 * Inicializa la vista cargando los datos del número si se está editando.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if (numeroEnEdicion != null) {
			cargarDatos();
		}
	}

	/**
	 * Carga los datos del número en edición en los campos del formulario.
	 */
	private void cargarDatos() {
		lblTitulo.setText("Editar Número");

		txtOrden.setText(String.valueOf(numeroEnEdicion.getOrden()));
		txtNombre.setText(numeroEnEdicion.getNombre());
		txtDuracion.setText(String.valueOf(numeroEnEdicion.getDuracion()));
	}

	/**
	 * Acción del botón "Guardar".
	 *
	 * Valida los datos introducidos y crea o actualiza el número según corresponda.
	 * Aplica todas las reglas del caso de uso CU5B.
	 */
	@FXML
	private void onGuardar() {

		// -----------------------------
		// VALIDACIONES CU5B
		// -----------------------------

		// Nombre obligatorio
		String nombre = txtNombre.getText().trim();
		if (nombre.isEmpty()) {
			mostrarError("El nombre no puede estar vacío.");
			return;
		}

		// Orden entero >= 1
		int orden;
		try {
			orden = Integer.parseInt(txtOrden.getText());
		} catch (NumberFormatException e) {
			mostrarError("El orden debe ser un número entero.");
			return;
		}

		if (orden < 1) {
			mostrarError("El orden debe ser 1 o superior.");
			return;
		}

		// Duración numérica
		double duracion;
		try {
			duracion = Double.parseDouble(txtDuracion.getText());
		} catch (NumberFormatException e) {
			mostrarError("La duración debe ser un número válido.");
			return;
		}

		if (duracion <= 0) {
			mostrarError("La duración debe ser mayor que 0.");
			return;
		}

		// Duración X.0 o X.5
		double decimal = duracion - Math.floor(duracion);
		if (!(decimal == 0.0 || decimal == 0.5)) {
			mostrarError("La duración debe ser X.0 o X.5 minutos.");
			return;
		}

		// Orden correlativo si estamos creando
		List<Numero> existentes = servicioEspectaculos.obtenerNumeros(espectaculoPadre);

		if (numeroEnEdicion == null) {
			int ordenEsperado = existentes.size() + 1;
			if (orden != ordenEsperado) {
				mostrarError("El orden debe ser correlativo. El siguiente número debe ser " + ordenEsperado + ".");
				return;
			}
		}

		// -----------------------------
		// GUARDAR
		// -----------------------------

		try {
			if (numeroEnEdicion == null) {
				servicioEspectaculos.crearNumero(espectaculoPadre.getId(), orden, nombre, duracion,
						sesion.getNombrePersona());
			} else {
				servicioEspectaculos.actualizarNumero(numeroEnEdicion, orden, nombre, duracion,
						sesion.getNombrePersona());
			}

		} catch (Exception e) {
			mostrarError(e.getMessage());
			return;
		}

		numeroEnEdicion = null;
		espectaculoPadre = null;
		navigation.goTo(FxmlView.GESTION_NUMEROS);
	}

	/**
	 * Acción del botón "Cancelar". Limpia las referencias estáticas y vuelve a la
	 * pantalla de gestión.
	 */
	@FXML
	private void onCancelar() {
		numeroEnEdicion = null;
		espectaculoPadre = null;
		navigation.goTo(FxmlView.GESTION_NUMEROS);
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
