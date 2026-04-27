/**
 * Enum que centraliza todas las vistas FXML de la aplicacion.
 *
 * Cada constante representa una pantalla y proporciona:
 * - El titulo que debe mostrarse en la ventana
 * - La ruta del archivo FXML correspondiente
 *
 * Este enum es utilizado por StageManager y ServicioNavegacion para
 * cargar pantallas de forma segura, evitando el uso de rutas literales
 * repartidas por el codigo.
 *
 * Algunas vistas obtienen su titulo desde un ResourceBundle para permitir
 * internacionalizacion.
 *
 * Autor: Michael
 * @version 1.0
 */

package com.luisdbb.tarea3AD2024base.view;

import java.util.ResourceBundle;

public enum FxmlView {

	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	},
	OLVIDE_CONTRASENIA {
		@Override
		public String getTitle() {
			return "Recuperar contraseña";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/olvide_contrasenia.fxml";
		}
	},

	MENU_PRINCIPAL {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menu.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/MenuPrincipal.fxml";
		}
	},

	MAIN_LAYOUT {
		@Override
		public String getTitle() {
			return "Menu Principal";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/MainLayout.fxml";
		}
	},

	// PERSONAS
	GESTION_PERSONAS {
		@Override
		public String getTitle() {
			return "Gestion de Personas";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/GestionPersonas.fxml";
		}
	},

	PERSONA_FORM {
		@Override
		public String getTitle() {
			return "Formulario Persona";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/PersonaForm.fxml";
		}
	},

	DETALLE_PERSONA {
		@Override
		public String getTitle() {
			return "Detalle Persona";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/DetallePersona.fxml";
		}
	},

	// ESPECTACULOS
	GESTION_ESPECTACULOS {
		@Override
		public String getTitle() {
			return "Gestion de Espectaculos";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/GestionEspectaculos.fxml";
		}
	},

	ESPECTACULO_FORM {
		@Override
		public String getTitle() {
			return "Formulario Espectaculo";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/EspectaculoForm.fxml";
		}
	},

	DETALLE_ESPECTACULO {
		@Override
		public String getTitle() {
			return "Detalle Espectaculo";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/DetalleEspectaculo.fxml";
		}
	},

	NUMERO_FORM {
		@Override
		public String getTitle() {
			return "Formulario Numero";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/NumeroForm.fxml";
		}
	},

	GESTION_ARTISTAS_NUMERO {
		@Override
		public String getTitle() {
			return "Artistas del Numero";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/GestionArtistasNumero.fxml";
		}
	},

	GESTION_NUMEROS {
		@Override
		public String getTitle() {
			return "Gestion de Numeros";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/GestionNumeros.fxml";
		}
	},

	LISTA_ESPECTACULOS {
		@Override
		public String getTitle() {
			return "Listado inicial de espectaculos";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ListaEspectaculos.fxml";
		}
	},

	// VISTAS PARA ARTISTA
	LISTA_ESPECTACULOS_ARTISTA {
		@Override
		public String getTitle() {
			return "Espectaculos (Artista)";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ListaEspectaculosArtista.fxml";
		}
	},

	FICHA_ARTISTA {
		@Override
		public String getTitle() {
			return "Mi Ficha de Artista";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/FichaArtista.fxml";
		}
	},
	LOGS {
		@Override
		public String getTitle() {
			return "Registro de Operaciones";
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Logs.fxml";
		}
	};

	/**
	 * Devuelve el titulo de la vista.
	 */
	public abstract String getTitle();

	/**
	 * Devuelve la ruta del archivo FXML asociado.
	 */
	public abstract String getFxmlFile();

	/**
	 * Obtiene un texto desde el ResourceBundle principal.
	 *
	 * @param key clave del texto
	 * @return valor asociado en el bundle
	 */
	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
