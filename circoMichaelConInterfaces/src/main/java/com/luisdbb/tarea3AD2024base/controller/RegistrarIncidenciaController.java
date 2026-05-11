package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Sesion;
import com.luisdbb.tarea3AD2024base.modelo.TipoIncidencia;
import com.luisdbb.tarea3AD2024base.services.ServicioEspectaculos;
import com.luisdbb.tarea3AD2024base.services.ServicioIncidencias;
import com.luisdbb.tarea3AD2024base.services.ServicioNavegacion;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

@Controller
public class RegistrarIncidenciaController implements Initializable {

    @Autowired
    private ServicioIncidencias servicioIncidencias;

    @Autowired
    private ServicioEspectaculos servicioEspectaculos;

    @Autowired
    private ServicioNavegacion navigation;

    @Autowired
    private Sesion sesion;

    @FXML
    private ComboBox<TipoIncidencia> cbTipo;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private ComboBox<Espectaculo> cbEspectaculo;

    @FXML
    private ComboBox<Numero> cbNumero;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cbTipo.getItems().setAll(TipoIncidencia.values());

        List<Espectaculo> espectaculos = servicioEspectaculos.findAll();
        cbEspectaculo.getItems().setAll(espectaculos);

        cbEspectaculo.setOnAction(e -> cargarNumeros());
    }

    private void cargarNumeros() {
        Espectaculo esp = cbEspectaculo.getValue();
        if (esp == null) {
            cbNumero.getItems().clear();
            return;
        }

        List<Numero> nums = servicioEspectaculos.obtenerNumeros(esp);
        cbNumero.getItems().setAll(nums);
    }

    @FXML
    private void onRegistrar() {

        TipoIncidencia tipo = cbTipo.getValue();
        String descripcion = txtDescripcion.getText().trim();

        if (tipo == null) {
            mostrarError("Debes seleccionar un tipo de incidencia.");
            return;
        }

        if (descripcion.isEmpty()) {
            mostrarError("La descripción no puede estar vacía.");
            return;
        }

        Long idEspectaculo = cbEspectaculo.getValue() != null
                ? cbEspectaculo.getValue().getId().longValue()
                : null;

        Long idNumero = cbNumero.getValue() != null
                ? cbNumero.getValue().getId().longValue()
                : null;

        String usuarioReporta = sesion.getNombrePersona();

        servicioIncidencias.registrarIncidencia(
                tipo,
                descripcion,
                usuarioReporta,
                idEspectaculo,
                idNumero
        );

        mostrarInfo("Incidencia registrada correctamente.");
        navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
    }

    @FXML
    private void onVolver() {
        navigation.goTo(FxmlView.GESTION_ESPECTACULOS);
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }
}
