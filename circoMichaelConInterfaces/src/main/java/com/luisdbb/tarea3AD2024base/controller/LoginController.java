/**
 * Controlador encargado de gestionar el proceso de inicio de sesión.
 *
 * Esta pantalla forma parte del caso de uso CU2 (Login/Logout) y permite:
 *
 * - Introducir usuario y contraseña
 * - Mostrar u ocultar la contraseña escrita
 * - Validar credenciales mediante ServicioLogin
 * - Acceder al sistema si el login es correcto
 * - Volver a la pantalla pública de espectáculos
 * - Acceder a la pantalla de recuperación de contraseña
 *
 * El controlador utiliza ServicioLogin para validar credenciales,
 * Sesion para almacenar el contexto del usuario autenticado,
 * y StageManager para cambiar de escena.
 *
 * @author MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.services.ServicioLogin;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.scene.control.*;

@Controller
public class LoginController {

	@FXML
	private TextField txtUsuario;
	@FXML
	private PasswordField txtContrasenia;
	@FXML
	private TextField txtContraseniaVisible;
	@FXML
	private CheckBox chkMostrarContrasenia;
	@FXML
	private Label lblError;

	@Autowired
	private ServicioLogin servicioLogin;

	@Autowired
	private Sesion sesion;

	@Lazy
	@Autowired
	private StageManager stageManager;

	// ============================================================
	// Inicialización
	// ============================================================

	/**
	 * Inicializa la vista configurando el comportamiento del campo de contraseña y
	 * sincronizando la versión visible y oculta.
	 */
	@FXML
	public void initialize() {

		// El campo visible empieza oculto
		txtContraseniaVisible.setVisible(false);
		txtContraseniaVisible.setManaged(false);

		// Sincronizar ambos campos
		txtContraseniaVisible.textProperty().bindBidirectional(txtContrasenia.textProperty());
	}

	// ============================================================
	// Mostrar / ocultar contraseña
	// ============================================================

	/**
	 * Acción del checkbox "Mostrar contraseña". Alterna entre el campo
	 * PasswordField y el TextField visible.
	 */
	@FXML
	private void onMostrarContrasenia() {
		boolean mostrar = chkMostrarContrasenia.isSelected();

		txtContrasenia.setVisible(!mostrar);
		txtContrasenia.setManaged(!mostrar);

		txtContraseniaVisible.setVisible(mostrar);
		txtContraseniaVisible.setManaged(mostrar);
	}

	// ============================================================
	// Login
	// ============================================================

	/**
	 * Acción del botón "Iniciar sesión".
	 *
	 * Valida las credenciales introducidas mediante ServicioLogin. Si son
	 * correctas, carga el layout principal. Si son incorrectas, muestra un mensaje
	 * de error.
	 */
	@FXML
	private void onLogin() {

		String usuario = txtUsuario.getText();
		String contrasenia = txtContrasenia.getText(); 
		boolean ok = servicioLogin.login(usuario, contrasenia, sesion);

		if (ok) {
			stageManager.switchScene(FxmlView.MAIN_LAYOUT);
		} else {
			lblError.setText("Usuario o contraseña incorrectos");
		}
	}

	// ============================================================
	// Volver
	// ============================================================

	/**
	 * Acción del botón "Volver".
	 *
	 * Restablece la sesión a modo invitado y regresa a la pantalla pública de lista
	 * de espectáculos.
	 */
	@FXML
	private void onVolver() {
		sesion.setPerfil(Perfiles.INVITADO);
		sesion.setNombrePersona(null);
		stageManager.switchScene(FxmlView.LISTA_ESPECTACULOS);
	}

	// ============================================================
	// Olvidé mi contraseña
	// ============================================================

	/**
	 * Acción del botón "Olvidé mi contraseña". Redirige a la pantalla de
	 * recuperación de contraseña.
	 */
	@FXML
	private void onOlvideContrasenia() {
		stageManager.switchScene(FxmlView.OLVIDE_CONTRASENIA);
	}
}
