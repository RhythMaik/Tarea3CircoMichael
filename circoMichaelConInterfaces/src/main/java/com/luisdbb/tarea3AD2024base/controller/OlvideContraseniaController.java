/**
 * Controlador encargado de gestionar la recuperación de contraseña.
 *
 * Esta pantalla forma parte del caso de uso CU2 (Login/Recuperación de contraseña)
 * y permite:
 *
 * - Introducir un email registrado en el sistema
 * - Validar si existe una persona asociada a dicho email
 * - Mostrar la contraseña directamente (según requisitos del proyecto)
 *
 * A diferencia de un sistema real, donde se enviaría un correo de recuperación,
 * este caso de uso muestra la contraseña en pantalla por simplicidad académica.
 *
 * Utiliza ServicioPersonas para localizar la cuenta y obtener sus credenciales,
 * y StageManager para volver a la pantalla de login.
 *
 * Autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Controller
public class OlvideContraseniaController {

	@FXML
	private TextField txtEmail;
	@FXML
	private Label lblError;

	@Autowired
	private ServicioPersonas servicioPersonas;

	@Lazy
	@Autowired
	private StageManager stageManager;

	// ============================================================
	// ENVIAR
	// ============================================================

	/**
	 * Acción del botón "Enviar".
	 *
	 * Valida el email introducido y, si existe una cuenta asociada, muestra la
	 * contraseña directamente en pantalla.
	 *
	 * Este comportamiento cumple con los requisitos del proyecto, aunque no sería
	 * adecuado en un entorno real.
	 */
	@FXML
	private void onEnviar() {

		String email = txtEmail.getText().trim();

		if (email.isBlank()) {
			lblError.setText("Introduce un email.");
			return;
		}

		Persona persona = servicioPersonas.findByEmail(email);

		if (persona == null) {
			lblError.setText("No existe ninguna cuenta con ese email.");
			return;
		}

		Credenciales cred = servicioPersonas.getCredenciales(persona);

		if (cred == null) {
			lblError.setText("Esta persona no tiene credenciales asociadas.");
			return;
		}

		// Mostrar la contraseña directamente
		lblError.setStyle("-fx-text-fill: green;");
		lblError.setText("Tu contraseña es: " + cred.getContrasenia());
	}

	// ============================================================
	// VOLVER
	// ============================================================

	/**
	 * Acción del botón "Volver". Regresa a la pantalla de login.
	 */
	@FXML
	private void onVolver() {
		stageManager.switchScene(FxmlView.LOGIN);
	}
}
