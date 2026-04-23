package com.luisdbb.tarea3AD2024base.controller;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.repositorios.ArtistaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.NumeroRepository;
import com.luisdbb.tarea3AD2024base.services.ServicioPersonas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Controlador encargado de mostrar la ficha completa del artista logueado.
 *
 * Esta pantalla forma parte del caso de uso CU6 (Ficha del Artista) y permite
 * visualizar: - Datos personales (nombre, email, nacionalidad) - Datos
 * profesionales (apodo, especialidades) - Trayectoria del artista agrupada por
 * espectáculos y números en los que participa
 *
 * Utiliza ServicioPersonas para obtener datos personales y exportar la ficha,
 * ArtistaRepository para cargar información profesional completa, y
 * NumeroRepository para obtener la trayectoria del artista.
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Controller
public class FichaArtistaController implements Initializable {

	@Autowired
	private Sesion sesion;

	@Autowired
	private ServicioPersonas servicioPersonas;

	@Autowired
	private ArtistaRepository artistaRepository;

	@Autowired
	private NumeroRepository numeroRepository;

	@FXML
	private Label lblNombre;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblNacionalidad;
	@FXML
	private Label lblApodo;
	@FXML
	private Label lblEspecialidades;
	@FXML
	private VBox panelTrayectoria;

	/**
	 * Artista actualmente logueado. Se carga durante la inicialización.
	 */
	private Artista artistaActual;

	/**
	 * Inicializa la vista cargando los datos personales, profesionales y la
	 * trayectoria del artista logueado.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// Obtener la persona logueada
		Persona p = servicioPersonas.findByNombre(sesion.getNombrePersona());

		// Obtener el artista asociado con especialidades
		Artista artista = artistaRepository.findByIdPersonaConEspecialidades(p.getId());
		this.artistaActual = artista;

		// Datos personales
		lblNombre.setText(p.getNombre());
		lblEmail.setText(p.getEmail());
		lblNacionalidad.setText(p.getNacionalidad());

		// Datos profesionales
		lblApodo.setText(artista.getApodo() != null ? artista.getApodo() : "-");

		List<String> esp = artista.getEspecialidades().stream().map(Enum::name).collect(Collectors.toList());

		lblEspecialidades.setText(esp.isEmpty() ? "Ninguna" : String.join(", ", esp));

		// Trayectoria
		cargarTrayectoria(artista);
	}

	/**
	 * Carga la trayectoria del artista agrupando los números por espectáculo.
	 *
	 * @param artista artista del que se mostrará la trayectoria
	 */
	private void cargarTrayectoria(Artista artista) {

		List<Numero> numeros = numeroRepository.findByArtistasContains(artista);

		Map<Espectaculo, List<Numero>> agrupado = numeros.stream()
				.collect(Collectors.groupingBy(Numero::getEspectaculo));

		List<Espectaculo> espectaculosOrdenados = agrupado.keySet().stream()
				.sorted(Comparator.comparing(Espectaculo::getId)).toList();

		for (Espectaculo e : espectaculosOrdenados) {

			Label lblEsp = new Label("Espectáculo " + e.getId() + " – " + e.getNombre());
			lblEsp.getStyleClass().add("detail-label");
			panelTrayectoria.getChildren().add(lblEsp);

			List<Numero> nums = agrupado.get(e).stream().sorted(Comparator.comparing(Numero::getId)).toList();

			for (Numero n : nums) {
				Label lblNum = new Label("    Número " + n.getId() + " – " + n.getNombre());
				lblNum.getStyleClass().add("detail-value");
				panelTrayectoria.getChildren().add(lblNum);
			}
		}
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

	/**
	 * Muestra un mensaje informativo en un cuadro de diálogo.
	 *
	 * @param titulo título del mensaje
	 * @param msg    contenido del mensaje
	 */
	private void mostrarInfo(String titulo, String msg) {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setHeaderText(titulo);
		a.setContentText(msg);
		a.showAndWait();
	}

	/**
	 * Acción del botón "Exportar XML".
	 *
	 * Permite al artista exportar su ficha completa en formato XML, incluyendo
	 * datos personales, profesionales y especialidades.
	 */
	@FXML
	private void onExportarXML() {

		if (artistaActual == null) {
			mostrarError("No se pudo cargar la información del artista.");
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Exportar ficha a XML");
		alert.setContentText("¿Deseas exportar tu ficha de artista en formato XML?");

		if (alert.showAndWait().get() != ButtonType.OK) {
			return;
		}

		// Seleccionar ubicación
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar ficha de artista");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo XML", "*.xml"));
		fileChooser.setInitialFileName("artista_" + artistaActual.getPersona().getId() + ".xml");

		File destino = fileChooser.showSaveDialog(null);

		if (destino == null) {
			return; // usuario canceló
		}

		try {
			servicioPersonas.exportarFichaArtistaXML(artistaActual, destino);
			mostrarInfo("Exportado", "La ficha se ha exportado correctamente.");
		} catch (Exception e) {
			mostrarError("No se pudo exportar la ficha: " + e.getMessage());
		}
	}
}
