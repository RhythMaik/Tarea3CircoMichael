/**
 * Controlador encargado de gestionar el formulario de creación y edición
 * de personas dentro del sistema.
 *
 * Esta pantalla forma parte del caso de uso CU3 (Gestión de Personas) y permite:
 *
 * - Crear una nueva persona (CU3A)
 * - Editar una persona existente (CU3B)
 * - Gestionar datos personales: nombre, email, nacionalidad
 * - Asignar un perfil (ADMIN, COORDINACION, ARTISTA)
 * - Gestionar datos profesionales según el perfil:
 *      * COORDINACION → senioridad y fecha de senior
 *      * ARTISTA → apodo y especialidades
 *
 * El controlador aplica todas las validaciones necesarias y utiliza:
 * - ServicioPersonas para operaciones de negocio
 * - CoordinacionRepository y ArtistaRepository para cargar datos específicos
 * - ServicioNavegacion para volver a la pantalla de gestión
 *
 * También carga dinámicamente la lista de países desde el archivo paises.xml.
 *
 * Autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
package com.luisdbb.tarea3AD2024base.controller;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.repositorios.ArtistaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.CoordinacionRepository;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

@Controller
public class PersonaFormController implements Initializable {

	/**
	 * Servicio de negocio para gestionar personas, credenciales y perfiles.
	 */
	@Autowired
	private ServicioPersonas servicioPersonas;

	/**
	 * Servicio de navegación para cambiar de pantalla dentro del layout.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	/**
	 * Repositorio para gestionar datos de coordinación.
	 */
	@Autowired
	private CoordinacionRepository coordinacionRepository;

	/**
	 * Repositorio para gestionar datos de artistas.
	 */
	@Autowired
	private ArtistaRepository artistaRepository;

	/**
	 * Persona actualmente en edición. Si es null, el formulario funciona en
	 * modo creación.
	 */
	public static Persona personaEnEdicion;

	@FXML
	private Label lblTitulo;
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<String> cmbNacionalidad;
	@FXML
	private ComboBox<Perfiles> cmbPerfil;
	@FXML
	private PasswordField txtContrasenia;

	// Paneles dinámicos
	@FXML
	private VBox panelCoordinacion;
	@FXML
	private CheckBox chkSenior;
	@FXML
	private DatePicker dpFechaSenior;

	@FXML
	private VBox panelArtista;
	@FXML
	private TextField txtApodo;

	// Especialidades
	@FXML
	private CheckBox chkAcrobacia;
	@FXML
	private CheckBox chkHumor;
	@FXML
	private CheckBox chkMagia;
	@FXML
	private CheckBox chkEquilibrismo;
	@FXML
	private CheckBox chkMalabarismo;

	/**
	 * Inicializa la vista cargando países, perfiles y datos si se está
	 * editando.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		cmbPerfil.getItems().setAll(Perfiles.values());
		cmbNacionalidad.getItems().addAll(cargarPaises());

		ocultarPaneles();

		if (personaEnEdicion != null) {
			cargarDatos();
		}
	}

	/**
	 * Oculta los paneles de datos profesionales hasta que se seleccione un
	 * perfil.
	 */
	private void ocultarPaneles() {
		panelCoordinacion.setVisible(false);
		panelCoordinacion.setManaged(false);

		panelArtista.setVisible(false);
		panelArtista.setManaged(false);
	}

	/**
	 * Muestra los paneles correspondientes al perfil seleccionado.
	 */
	@FXML
	private void onPerfilSeleccionado() {
		ocultarPaneles();

		Perfiles perfil = cmbPerfil.getValue();

		if (perfil == Perfiles.COORDINACION) {
			panelCoordinacion.setVisible(true);
			panelCoordinacion.setManaged(true);
		}

		if (perfil == Perfiles.ARTISTA) {
			panelArtista.setVisible(true);
			panelArtista.setManaged(true);
		}
	}

	/**
	 * Muestra u oculta la fecha de senior según el checkbox.
	 */
	@FXML
	private void onSeniorSeleccionado() {
		boolean esSenior = chkSenior.isSelected();
		dpFechaSenior.setVisible(esSenior);
		dpFechaSenior.setManaged(esSenior);
	}

	/**
	 * Carga la lista de países desde el archivo paises.xml.
	 *
	 * @return lista de países en formato "ID - Nombre"
	 */
	private List<String> cargarPaises() {
		List<String> lista = new ArrayList<>();

		try (InputStream is = getClass().getClassLoader()
				.getResourceAsStream("paises.xml")) {

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = db.parse(is);

			NodeList paises = doc.getElementsByTagName("pais");

			for (int i = 0; i < paises.getLength(); i++) {

				String id = paises.item(i).getChildNodes().item(1)
						.getTextContent().trim();
				String nombre = paises.item(i).getChildNodes().item(3)
						.getTextContent().trim();

				lista.add(id + " - " + nombre);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	/**
	 * Carga los datos de la persona en edición en los campos del formulario.
	 */
	private void cargarDatos() {

		lblTitulo.setText("Editar Persona");

		txtNombre.setText(personaEnEdicion.getNombre());
		txtEmail.setText(personaEnEdicion.getEmail());

		String id = personaEnEdicion.getNacionalidad();
		for (String item : cmbNacionalidad.getItems()) {
			if (item.startsWith(id)) {
				cmbNacionalidad.setValue(item);
				break;
			}
		}

		Credenciales cred = servicioPersonas.getCredenciales(personaEnEdicion);
		cmbPerfil.setValue(cred.getPerfil());

		txtContrasenia.setDisable(true);

		// Datos profesionales según perfil
		if (cred.getPerfil() == Perfiles.COORDINACION) {

			Coordinacion c = coordinacionRepository
					.findByIdPersona(personaEnEdicion.getId());

			if (c != null) {
				panelCoordinacion.setVisible(true);
				panelCoordinacion.setManaged(true);

				chkSenior.setSelected(c.isSenior());

				if (c.isSenior()) {
					dpFechaSenior.setVisible(true);
					dpFechaSenior.setManaged(true);
					dpFechaSenior.setValue(c.getFechaSenior());
				}
			}
		}

		if (cred.getPerfil() == Perfiles.ARTISTA) {

			Artista a = artistaRepository
					.findByIdPersonaConEspecialidades(personaEnEdicion.getId());

			if (a != null) {
				panelArtista.setVisible(true);
				panelArtista.setManaged(true);

				txtApodo.setText(a.getApodo());

				for (Especialidad esp : a.getEspecialidades()) {
					switch (esp) {
					case ACROBACIA -> chkAcrobacia.setSelected(true);
					case HUMOR -> chkHumor.setSelected(true);
					case MAGIA -> chkMagia.setSelected(true);
					case EQUILIBRISMO -> chkEquilibrismo.setSelected(true);
					case MALABARISMO -> chkMalabarismo.setSelected(true);
					}
				}
			}
		}
	}

	/**
	 * Acción del botón Guardar.
	 *
	 * Valida los datos introducidos y crea o actualiza la persona según
	 * corresponda. Este método implementa toda la lógica del caso de uso CU3:
	 *
	 * CU3A – Crear persona: - Requiere contraseña obligatoria - Genera
	 * automáticamente un nombre de usuario normalizado - Crea la entidad
	 * Persona y su perfil asociado - Crea entidades profesionales según el
	 * perfil (Coordinación o Artista)
	 *
	 * CU3B – Editar persona: - Actualiza datos personales (nombre, email,
	 * nacionalidad) - Actualiza datos profesionales según el perfil - Mantiene
	 * la contraseña existente
	 *
	 * Validaciones aplicadas: - Todos los campos obligatorios deben estar
	 * completos - Si el perfil es COORDINACIÓN y es senior → fecha obligatoria
	 * - Si el perfil es ARTISTA → especialidades opcionales, apodo opcional
	 *
	 * Tras completar la operación, vuelve a la pantalla de gestión de personas.
	 */
	@FXML
	private void onGuardar() {

		String nombre = txtNombre.getText().trim();
		String email = txtEmail.getText().trim();
		String nacionalidad = cmbNacionalidad.getValue();
		Perfiles perfil = cmbPerfil.getValue();

		if (nombre.isBlank() || email.isBlank() || nacionalidad == null
				|| perfil == null) {
			mostrarError("Rellena todos los campos obligatorios.");
			return;
		}

		// ============================================================
		// VALIDACIÓN DE NOMBRE SIN TILDES, Ñ NI ESPACIOS
		// ============================================================
		if (!nombre.matches("^[A-Za-z]+$")) {
			mostrarError(
					"El nombre solo puede contener letras sin tildes, sin ñ y sin espacios.");
			return;
		}

		String idPais = nacionalidad.substring(0, 2);

		boolean senior = false;
		LocalDate fechaSenior = null;
		String apodo = null;
		List<Especialidad> especialidades = new ArrayList<>();

		// Datos específicos de COORDINACIÓN
		if (perfil == Perfiles.COORDINACION) {
			senior = chkSenior.isSelected();
			if (senior) {
				fechaSenior = dpFechaSenior.getValue();
				if (fechaSenior == null) {
					mostrarError("Debes indicar la fecha desde que es senior.");
					return;
				}
			}
		}

		// Datos específicos de ARTISTA
		if (perfil == Perfiles.ARTISTA) {
			apodo = txtApodo.getText().trim();
			if (apodo.isBlank())
				apodo = null;

			if (chkAcrobacia.isSelected())
				especialidades.add(Especialidad.ACROBACIA);
			if (chkHumor.isSelected())
				especialidades.add(Especialidad.HUMOR);
			if (chkMagia.isSelected())
				especialidades.add(Especialidad.MAGIA);
			if (chkEquilibrismo.isSelected())
				especialidades.add(Especialidad.EQUILIBRISMO);
			if (chkMalabarismo.isSelected())
				especialidades.add(Especialidad.MALABARISMO);
		}

		// ============================================================
		// CREAR PERSONA (CU3A)
		// ============================================================
		if (personaEnEdicion == null) {

			String pass = txtContrasenia.getText();
			if (pass.isBlank()) {
				mostrarError("La contraseña es obligatoria al crear.");
				return;
			}

			// ============================================================
			// GENERAR USUARIO DIRECTAMENTE DEL NOMBRE (ya validado)
			// ============================================================
			String usuario = nombre.toLowerCase();

			try {
				servicioPersonas.registrarPersona(nombre, email, idPais,
						usuario, pass, perfil, senior, fechaSenior, apodo,
						especialidades);
			} catch (Exception e) {
				mostrarError(e.getMessage());
				return;
			}

		} else {

			// ============================================================
			// EDITAR PERSONA (CU3B)
			// ============================================================
			personaEnEdicion.setNombre(nombre);
			personaEnEdicion.setEmail(email);
			personaEnEdicion.setNacionalidad(idPais);

			servicioPersonas.actualizarPersona(personaEnEdicion);

			// Actualizar datos profesionales
			if (perfil == Perfiles.COORDINACION) {
				Coordinacion c = coordinacionRepository
						.findByIdPersona(personaEnEdicion.getId());
				if (c == null)
					c = new Coordinacion(personaEnEdicion, senior, fechaSenior);
				else {
					c.setSenior(senior);
					c.setFechaSenior(fechaSenior);
				}
				coordinacionRepository.save(c);
			}

			if (perfil == Perfiles.ARTISTA) {
				Artista a = artistaRepository
						.findByIdPersona(personaEnEdicion.getId());
				if (a == null)
					a = new Artista(personaEnEdicion, apodo, especialidades);
				else {
					a.setApodo(apodo);
					a.setEspecialidades(especialidades);
				}
				artistaRepository.save(a);
			}

			servicioPersonas.actualizarPerfil(personaEnEdicion, perfil);
		}

		personaEnEdicion = null;
		navigation.goTo(FxmlView.GESTION_PERSONAS);
	}

	/**
	 * Acción del botón Cancelar. Limpia la referencia estática y vuelve a la
	 * pantalla de gestión.
	 */
	@FXML
	private void onCancelar() {
		personaEnEdicion = null;
		navigation.goTo(FxmlView.GESTION_PERSONAS);
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