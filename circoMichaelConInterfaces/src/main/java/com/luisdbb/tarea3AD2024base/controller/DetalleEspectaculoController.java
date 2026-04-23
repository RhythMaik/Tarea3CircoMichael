package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;

import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador encargado de mostrar la información detallada de un espectáculo.
 *
 * Esta pantalla forma parte de los casos de uso: - CU4: Visualización completa
 * de un espectáculo - CU5B/C: Gestión de números y artistas (solo para
 * coordinación) - CU6: Consulta de espectáculos por parte de artistas
 *
 * Funciones principales: - Cargar los datos del espectáculo seleccionado -
 * Mostrar sus números y artistas participantes - Adaptar la navegación según el
 * perfil del usuario (ARTISTA o COORDINACION/ADMIN)
 *
 * Utiliza ServicioEspectaculos para obtener información actualizada y
 * ServicioNavegacion para cambiar de pantalla.
 *
 * @autor: MichaelQP
 * @version 1.1
 * @since 2026
 */
@Controller
public class DetalleEspectaculoController implements Initializable {

	/**
	 * Servicio para obtener información de espectáculos y números.
	 */
	@Autowired
	private ServicioEspectaculos servicioEspectaculos;

	/**
	 * Servicio de navegación para cambiar de pantalla.
	 */
	@Autowired
	private ServicioNavegacion navigation;

	/**
	 * Sesión activa del usuario. Permite adaptar la navegación según el perfil.
	 */
	@Autowired
	private Sesion sesion;

	/**
	 * Espectáculo que se mostrará en la vista. Se establece desde el controlador
	 * anterior.
	 */
	public static Espectaculo espectaculoMostrado;

	@FXML
	private Label lblTitulo;
	@FXML
	private Label lblInicio;
	@FXML
	private Label lblFin;
	@FXML
	private Label lblCoordinador;

	@FXML
	private TableView<Numero> tablaNumeros;
	@FXML
	private TableColumn<Numero, Integer> colOrden;
	@FXML
	private TableColumn<Numero, String> colNombre;
	@FXML
	private TableColumn<Numero, Double> colDuracion;
	@FXML
	private TableColumn<Numero, String> colArtistas;

	/**
	 * Inicializa la vista configurando la tabla y cargando los datos del
	 * espectáculo.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		configurarTabla();
		cargarDatos();
	}

	/**
	 * Configura las columnas de la tabla de números. Incluye una columna calculada
	 * para mostrar los nombres de los artistas participantes.
	 */
	private void configurarTabla() {
		colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

		colArtistas.setCellValueFactory(c -> {
			Numero n = c.getValue();
			String nombres = n.getArtistas().stream().map(a -> a.getPersona().getNombre())
					.reduce((a, b) -> a + ", " + b).orElse("");
			return new ReadOnlyStringWrapper(nombres);
		});
	}

	/**
	 * Carga los datos del espectáculo seleccionado en los labels y la tabla.
	 */
	private void cargarDatos() {
		if (espectaculoMostrado == null)
			return;

		lblTitulo.setText(espectaculoMostrado.getNombre());
		lblInicio.setText(espectaculoMostrado.getFechaInicio().toString());
		lblFin.setText(espectaculoMostrado.getFechaFin().toString());
		lblCoordinador.setText(espectaculoMostrado.getCoordinador().getPersona().getNombre());

		tablaNumeros
				.setItems(FXCollections.observableArrayList(servicioEspectaculos.obtenerNumeros(espectaculoMostrado)));
	}

	/**
	 * Acción del botón "Volver".
	 *
	 * Redirige a una pantalla distinta según el perfil del usuario: - ARTISTA →
	 * lista de espectáculos para artistas - COORDINACION/ADMIN → gestión general de
	 * espectáculos
	 */
	@FXML
	private void onVolver() {
		if (sesion.getPerfil() == Perfiles.ARTISTA) {
			navigation.goTo(FxmlView.LISTA_ESPECTACULOS_ARTISTA);
		} else {
			navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
		}
	}
}
