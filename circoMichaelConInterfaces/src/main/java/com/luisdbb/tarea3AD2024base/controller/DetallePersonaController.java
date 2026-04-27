package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.repositorios.ArtistaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.CoordinacionRepository;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controlador encargado de mostrar la información detallada de una persona.
 *
 * Esta pantalla forma parte del caso de uso CU3C (gestión de personas) y
 * permite visualizar: - Datos básicos de la persona (nombre, email) - Perfil
 * asignado (ADMIN, COORDINACION, ARTISTA) - Información adicional según el
 * perfil: * Coordinación: senioridad y fecha de senior * Artista: apodo y
 * especialidades
 *
 * Utiliza ServicioPersonas para obtener credenciales y datos asociados, y los
 * repositorios de Coordinacion y Artista para cargar información específica de
 * cada rol.
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Controller
public class DetallePersonaController implements Initializable {

	/**
	 * Servicio para obtener información general de personas y credenciales.
	 */
	@Autowired
	private ServicioPersonas servicioPersonas;

	/**
	 * Servicio de navegación para cambiar de pantalla.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	/**
	 * Repositorio para cargar información de coordinadores.
	 */
	@Autowired
	private CoordinacionRepository coordinacionRepository;

	/**
	 * Repositorio para cargar información de artistas.
	 */
	@Autowired
	private ArtistaRepository artistaRepository;

	/**
	 * Persona que se mostrará en la vista. Se establece desde el controlador
	 * anterior.
	 */
	public static Persona personaMostrada;

	@FXML
	private Label lblTitulo;
	@FXML
	private Label lblNombre;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblPerfil;

	// Panel de coordinación
	@FXML
	private VBox panelCoordinacion;
	@FXML
	private Label lblSenior;
	@FXML
	private Label lblFechaSenior;

	// Panel de artista
	@FXML
	private VBox panelArtista;
	@FXML
	private Label lblApodo;
	@FXML
	private Label lblEspecialidades;

	/**
	 * Inicializa la vista cargando los datos de la persona seleccionada.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if (personaMostrada != null) {
			cargarDatos();
		}
	}

	/**
	 * Carga los datos generales de la persona y muestra la información específica
	 * según su perfil.
	 */
	private void cargarDatos() {

		lblTitulo.setText("Detalle de la Persona");

		lblNombre.setText(personaMostrada.getNombre());
		lblEmail.setText(personaMostrada.getEmail());

		Credenciales cred = servicioPersonas.getCredenciales(personaMostrada);

		if (cred != null) {
			lblPerfil.setText(cred.getPerfil().name());

			switch (cred.getPerfil()) {

			case COORDINACION -> cargarCoordinacion();
			case ARTISTA -> cargarArtista();

			default -> {
				panelCoordinacion.setVisible(false);
				panelCoordinacion.setManaged(false);
				panelArtista.setVisible(false);
				panelArtista.setManaged(false);
			}
			}

		} else {
			lblPerfil.setText("N/A");
		}
	}

	/**
	 * Carga la información específica del perfil COORDINACION.
	 */
	private void cargarCoordinacion() {

		Coordinacion c = coordinacionRepository.findByIdPersona(personaMostrada.getId());

		if (c != null) {
			panelCoordinacion.setVisible(true);
			panelCoordinacion.setManaged(true);

			lblSenior.setText(c.isSenior() ? "Si" : "No");

			if (c.isSenior() && c.getFechaSenior() != null) {
				lblFechaSenior.setText(c.getFechaSenior().toString());
			} else {
				lblFechaSenior.setText("-");
			}
		}
	}

	/**
	 * Carga la información específica del perfil ARTISTA.
	 */
	private void cargarArtista() {

		Artista a = artistaRepository.findByIdPersonaConEspecialidades(personaMostrada.getId());

		if (a != null) {
			panelArtista.setVisible(true);
			panelArtista.setManaged(true);

			lblApodo.setText(a.getApodo() != null ? a.getApodo() : "-");

			List<String> lista = a.getEspecialidades().stream().map(Enum::name).collect(Collectors.toList());

			lblEspecialidades.setText(lista.isEmpty() ? "Ninguna" : String.join(", ", lista));
		}
	}

	/**
	 * Acción del botón "Volver".
	 *
	 * Limpia la referencia estática y regresa a la pantalla de gestión de personas.
	 */
	@FXML
	private void onVolver() {
		personaMostrada = null;
		navigation.goTo(FxmlView.GESTION_PERSONAS);
	}
}
