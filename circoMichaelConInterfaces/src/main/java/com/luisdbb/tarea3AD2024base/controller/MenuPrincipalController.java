package com.luisdbb.tarea3AD2024base.controller;

//SIN USAR
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@Controller
public class MenuPrincipalController {

	@FXML
	private Label lblBienvenida;

	@FXML
	private Button btnPersonas, btnEspectaculos, btnNumeros, btnArtistas, btnVerEspectaculos, btnCerrarSesion;

	@Lazy
	@Autowired
	private StageManager stageManager;

	private Persona usuario;

	public void setUsuario(Persona persona) {
		this.usuario = persona;
		lblBienvenida.setText("Bienvenido, " + persona.getNombre());
		configurarSegunPerfil(persona);
	}

	private void configurarSegunPerfil(Persona p) {
		// Ejemplo:
		// Si es artista → ocultar botones de admin
		// Si es admin → mostrar todo
		// Si es coordinación → mostrar solo lo suyo
	}

	/*
	 * @FXML private void onPersonas() {
	 * stageManager.switchScene(FxmlView.GESTION_PERSONAS); }
	 * 
	 * @FXML private void onEspectaculos() {
	 * stageManager.switchScene(FxmlView.GESTION_ESPECTACULOS); }
	 * 
	 * @FXML private void onNumeros() {
	 * stageManager.switchScene(FxmlView.GESTION_NUMEROS); }
	 * 
	 * @FXML private void onArtistas() {
	 * stageManager.switchScene(FxmlView.GESTION_ARTISTAS); }
	 * 
	 * @FXML private void onVerEspectaculos() {
	 * stageManager.switchScene(FxmlView.LISTA_ESPECTACULOS); }
	 * 
	 * @FXML private void onCerrarSesion() {
	 * stageManager.switchScene(FxmlView.LOGIN); }
	 */
}
