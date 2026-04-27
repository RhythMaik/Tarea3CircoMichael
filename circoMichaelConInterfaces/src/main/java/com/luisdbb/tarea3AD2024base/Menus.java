/**
 * @author Michael Quintero Petroche
 */
//YA NO SE USA, ERA PARA LA VERSION DE COMANDOS

package com.luisdbb.tarea3AD2024base;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.services.*;

/**
 * Clase encargada de gestionar todos los menus interactivos del sistema.
 * Controla la navegacion segun el perfil del usuario (invitado, artista,
 * coordinacion o admin) y delega las operaciones en los servicios
 * correspondientes.
 */
public class Menus {

	// Servicios necesarios para ejecutar las acciones de los menus
	private final ServicioEspectaculos servicioEspectaculos;
	private final ServicioPersonas servicioPersonas;
	private final ServicioArtistas servicioArtistas;
	private final ServicioLogin servicioLogin;

	// Persona actualmente autenticada (solo para opciones de admin)
	private Persona usuarioActual;

	// Constructor que recibe las dependencias necesarias
	public Menus(ServicioEspectaculos servicioEspectaculos, ServicioPersonas servicioPersonas,
			ServicioArtistas servicioArtistas, ServicioLogin servicioLogin) {
		this.servicioEspectaculos = servicioEspectaculos;
		this.servicioPersonas = servicioPersonas;
		this.servicioArtistas = servicioArtistas;
		this.servicioLogin = servicioLogin;
	}
	// ============================================================
	// MENU PRINCIPAL
	// ============================================================

	/**
	 * Metodo inicial del programa. Muestra el menu de invitado y permite acceder al
	 * login o consultar espectaculos sin iniciar sesion.
	 */
	public void iniciar() {
		System.out.println("==============================================================");
		System.out.println("========== Bienvenido al programa de gestion del circo =======");
		System.out.println("==============================================================");

		Scanner leer = new Scanner(System.in);
		Sesion sesion = new Sesion();

		int opcion = -1;

		while (opcion != 0) {
			mostrarMenuInvitado();

			try {
				opcion = leer.nextInt();
				leer.nextLine();
			} catch (InputMismatchException e) {
				leer.nextLine();
				continue;
			}

			switch (opcion) {
			case 1:
				verEspectaculos();
				break;
			case 2:
				manejarLogin(leer, sesion);
				break;
			case 0:
				System.out.println("Hasta luego!");
				break;
			default:
				System.out.println("Opcion invalida");
			}
		}
	}

	// ============================================================
	// LOGIN
	// ============================================================

	/**
	 * Gestiona el proceso de login y redirige al menu correspondiente segun el
	 * perfil del usuario autenticado.
	 */
	private void manejarLogin(Scanner leer, Sesion sesion) {
		System.out.print("Usuario: ");
		String usuario = leer.nextLine().trim();
		System.out.print("Contrasena: ");
		String contra = leer.nextLine().trim();

		if (servicioLogin.login(usuario, contra, sesion)) {
			System.out.println("Login correcto como " + sesion.getPerfil());

			switch (sesion.getPerfil()) {
			case ARTISTA:
				menuArtista(leer, sesion);
				break;
			case COORDINACION:
				menuCoordinacion(leer, sesion);
				break;
			case ADMIN:
				menuAdmin(leer, sesion);
				break;
			default:
				System.out.println("Perfil desconocido.");
			}

		} else {
			System.out.println("Usuario o contrasena incorrectos.");
		}
	}

	// ============================================================
	// MENUS POR PERFIL
	// ============================================================

	/**
	 * Menu exclusivo para usuarios con perfil ARTISTA.
	 */
	private void menuArtista(Scanner leer, Sesion sesion) {
		int op = -1;

		while (op != 0) {
			mostrarMenuArtista();

			try {
				op = leer.nextInt();
				leer.nextLine();
			} catch (Exception e) {
				leer.nextLine();
				continue;
			}

			switch (op) {
			case 1:
				servicioArtistas.verFichaArtista(sesion);
				break;
			case 2:
				verEspectaculos();
				break;
			case 3:
				verEspectaculoCompleto(leer);
				break;
			case 0:
				cerrarSesion(sesion);
				break;
			default:
				System.out.println("Opcion invalida");
			}
		}
	}

	/**
	 * Menu exclusivo para usuarios con perfil COORDINACION.
	 */
	private void menuCoordinacion(Scanner leer, Sesion sesion) {
		int op = -1;

		while (op != 0) {
			mostrarMenuCoordinacion();

			try {
				op = leer.nextInt();
				leer.nextLine();
			} catch (Exception e) {
				leer.nextLine();
				continue;
			}

			switch (op) {
			case 1:
				crearEspectaculo(leer, sesion);
				break;
			case 2:
				modificarEspectaculo(leer);
				break;
			case 3:
				verEspectaculos();
				break;
			case 4:
				gestionarNumeros(leer);
				break;
			case 5:
				gestionarArtistas(leer);
				break;
			case 6:
				verEspectaculoCompleto(leer);
				break;
			case 0:
				cerrarSesion(sesion);
				break;
			default:
				System.out.println("Opcion invalida");
			}
		}
	}

	/**
	 * Menu exclusivo para usuarios con perfil ADMIN.
	 */
	private void menuAdmin(Scanner leer, Sesion sesion) {
		int op = -1;

		while (op != 0) {
			mostrarMenuAdmin();

			try {
				op = leer.nextInt();
				leer.nextLine();
			} catch (Exception e) {
				leer.nextLine();
				continue;
			}

			switch (op) {
			case 1:
				registrarPersona(leer, sesion);
				break;
			case 2:

				break;
			case 3:
				crearEspectaculo(leer, sesion);
				break;
			case 4:
				verEspectaculos();
				break;
			case 5:
				modificarEspectaculo(leer);
				break;
			case 6:
				gestionarNumeros(leer);
				break;
			case 7:
				gestionarArtistas(leer);
				break;
			case 8:
				verEspectaculoCompleto(leer);
				break;
			case 0:
				cerrarSesion(sesion);
				break;
			default:
				System.out.println("Opcion invalida");
			}
		}
	}

	// ============================================================
	// FUNCIONES DE MENU
	// ============================================================

	/**
	 * Muestra todos los espectaculos registrados.
	 */
	private void verEspectaculos() {
		List<Espectaculo> lista = servicioEspectaculos.findAll();

		if (lista.isEmpty()) {
			System.out.println("No hay espectaculos.");
			return;
		}

		System.out.println("=== ESPECTACULOS ===");
		lista.forEach(System.out::println);
	}

	/**
	 * Muestra el informe completo de un espectaculo seleccionado por ID.
	 */
	private void verEspectaculoCompleto(Scanner leer) {

		verEspectaculos();

		System.out.print("ID del espectaculo: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		String informe = servicioEspectaculos.generarInformeEspectaculo(id);

		if (informe == null) {
			System.out.println("No existe ese espectaculo.");
			return;
		}

		System.out.println(informe);
	}

	/**
	 * Crea un espectaculo nuevo segun los datos introducidos por el usuario.
	 */
	private void crearEspectaculo(Scanner leer, Sesion sesion) {

		System.out.print("Nombre: ");
		String nombre = leer.nextLine();

		System.out.print("Fecha inicio (YYYY-MM-DD): ");
		LocalDate ini = LocalDate.parse(leer.nextLine());

		System.out.print("Fecha fin (YYYY-MM-DD): ");
		LocalDate fin = LocalDate.parse(leer.nextLine());

		Persona coord;

		if (sesion.getPerfil() == Perfiles.COORDINACION) {
			coord = servicioPersonas.findByNombre(sesion.getNombrePersona());
		} else {
			System.out.println("=== LISTA DE COORDINADORES ===");
			List<Persona> coords = servicioPersonas.findAllCoordinadores();

			if (coords.isEmpty()) {
				System.out.println("No hay coordinadores registrados.");
				return;
			}

			coords.forEach(p -> System.out.println(p.getId() + " - " + p.getNombre()));

			System.out.print("ID del coordinador: ");
			Integer idCoord = leer.nextInt();
			leer.nextLine();

			coord = servicioPersonas.findById(idCoord);

			if (coord == null || !servicioPersonas.esCoordinador(coord)) {
				System.out.println("Coordinador invalido.");
				return;
			}
		}

		try {
			// servicioEspectaculos.crearEspectaculo(nombre, ini, fin, coord);
			System.out.println("Espectaculo creado.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	// ============================================================
	// MODIFICAR ESPECTACULO
	// ============================================================

	/**
	 * Permite modificar un espectaculo existente: nombre, fechas, numeros o
	 * coordinador (solo para ADMIN).
	 */
	private void modificarEspectaculo(Scanner leer) {

		verEspectaculos();

		System.out.print("ID a modificar: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		Espectaculo esp = servicioEspectaculos.findById(id);

		if (esp == null) {
			System.out.println("No existe.");
			return;
		}

		int opcion = -1;

		while (opcion != 0) {

			System.out.println("\n=== MODIFICAR ESPECTACULO ===");
			System.out.println("1. Cambiar nombre");
			System.out.println("2. Cambiar fechas");
			System.out.println("3. Ver numeros");
			System.out.println("4. Anadir numero");
			System.out.println("5. Quitar numero");
			System.out.println("6. Ver informe completo");

			// Opcion exclusiva para ADMIN
			if (usuarioActual != null && usuarioActual.getCredenciales() != null
					&& usuarioActual.getCredenciales().getPerfil() == Perfiles.ADMIN) {

				System.out.println("7. Cambiar coordinador");
			}

			System.out.println("0. Volver");
			System.out.print("Opcion: ");

			opcion = leer.nextInt();
			leer.nextLine();

			switch (opcion) {

			case 1:
				System.out.print("Nuevo nombre: ");
				String nuevoNombre = leer.nextLine();
				// servicioEspectaculos.actualizarNombre(esp, nuevoNombre);
				System.out.println("Nombre actualizado.");
				break;

			case 2:
				System.out.print("Nueva fecha inicio (YYYY-MM-DD): ");
				LocalDate ini = LocalDate.parse(leer.nextLine());
				System.out.print("Nueva fecha fin (YYYY-MM-DD): ");
				LocalDate fin = LocalDate.parse(leer.nextLine());
				// servicioEspectaculos.actualizarFechas(esp, ini, fin);
				System.out.println("Fechas actualizadas.");
				break;

			case 3:
				servicioEspectaculos.verNumerosDeEspectaculo(esp.getId());
				break;

			case 4:
				crearNumero(leer, esp);
				break;

			case 5:
				borrarNumero(leer, esp);
				break;

			case 6:
				System.out.println(servicioEspectaculos.generarInformeEspectaculo(esp.getId()));
				break;

			case 7:
				if (usuarioActual == null || usuarioActual.getCredenciales() == null
						|| usuarioActual.getCredenciales().getPerfil() != Perfiles.ADMIN) {

					System.out.println("No tienes permiso para cambiar el coordinador.");
					break;
				}

				System.out.println("Coordinadores disponibles:");
				List<Persona> coords = servicioPersonas.findAllCoordinadores();
				for (Persona c : coords) {
					System.out.println(c.getId() + " - " + c.getNombre());
				}

				System.out.print("ID del nuevo coordinador: ");
				int idCoord = leer.nextInt();
				leer.nextLine();

				// servicioEspectaculos.actualizarCoordinador(esp, idCoord);
				System.out.println("Coordinador actualizado.");
				break;

			case 0:
				System.out.println("Volviendo...");
				break;

			default:
				System.out.println("Opcion invalida.");
			}
		}
	}

	// ============================================================
	// GESTION DE NUMEROS
	// ============================================================

	/**
	 * Menu para gestionar los numeros de un espectaculo: ver, crear, modificar o
	 * borrar.
	 */
	private void gestionarNumeros(Scanner leer) {

		verEspectaculos();

		System.out.print("ID del espectaculo: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		Espectaculo esp = servicioEspectaculos.findById(id);

		if (esp == null) {
			System.out.println("No existe.");
			return;
		}

		int op = -1;

		while (op != 0) {
			System.out.println("=== GESTION DE NUMEROS ===");
			System.out.println("1. Ver numeros");
			System.out.println("2. Crear numero");
			System.out.println("3. Modificar numero");
			System.out.println("4. Borrar numero");
			System.out.println("0. Volver");

			try {
				op = leer.nextInt();
				leer.nextLine();
			} catch (Exception e) {
				leer.nextLine();
				continue;
			}

			switch (op) {

			case 1:
				List<Numero> nums = servicioEspectaculos.obtenerNumeros(esp);

				if (nums == null || nums.size() == 0) {
					System.out.println("No hay numeros en este espectaculo.");
					break;
				}

				System.out.println("=== NUMEROS DEL ESPECTACULO ===");

				for (Numero n : nums) {
					System.out.println("ID: " + n.getId());
					System.out.println("Orden: " + n.getOrden());
					System.out.println("Nombre: " + n.getNombre());
					System.out.println("Duracion: " + n.getDuracion() + " minutos");

					List<Artista> artistas = servicioEspectaculos.obtenerArtistasDeNumero(n);

					if (artistas == null || artistas.size() == 0) {
						System.out.println("   Artistas: (ninguno)");
					} else {
						System.out.println("   Artistas:");
						for (Artista a : artistas) {
							Persona p = a.getPersona();
							System.out.print("     - " + p.getNombre() + " (" + p.getNacionalidad() + ")");
							if (a.getApodo() != null) {
								System.out.print(" | Apodo: " + a.getApodo());
							}
							System.out.println();
						}
					}

					System.out.println();
				}
				break;

			case 2:
				crearNumero(leer, esp);
				break;

			case 3:
				modificarNumero(leer, esp);
				break;

			case 4:
				borrarNumero(leer, esp);
				break;

			case 0:
				break;

			default:
				System.out.println("Opcion invalida");
			}
		}
	}

	/**
	 * Crea un numero dentro de un espectaculo.
	 */
	private void crearNumero(Scanner leer, Espectaculo esp) {
		System.out.print("Orden: ");
		int orden = leer.nextInt();
		leer.nextLine();

		System.out.print("Nombre: ");
		String nombre = leer.nextLine();

		System.out.print("Duracion: ");
		double dur = leer.nextDouble();
		leer.nextLine();

		// servicioEspectaculos.crearNumero(esp.getId(), orden, nombre, dur);

		System.out.println("Numero creado.");
	}

	/**
	 * Modifica los datos de un numero existente.
	 */
	private void modificarNumero(Scanner leer, Espectaculo esp) {
		List<Numero> lista = servicioEspectaculos.obtenerNumeros(esp);
		lista.forEach(System.out::println);

		System.out.print("ID del numero: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		Numero n = lista.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);

		if (n == null) {
			System.out.println("No existe.");
			return;
		}

		System.out.print("Nuevo orden: ");
		int orden = leer.nextInt();
		leer.nextLine();

		System.out.print("Nuevo nombre: ");
		String nombre = leer.nextLine();

		System.out.print("Nueva duracion: ");
		double dur = leer.nextDouble();
		leer.nextLine();

		// servicioEspectaculos.actualizarNumero(n, orden, nombre, dur);

		System.out.println("Numero actualizado.");
	}

	/**
	 * Borra un numero de un espectaculo.
	 */
	private void borrarNumero(Scanner leer, Espectaculo esp) {
		List<Numero> lista = servicioEspectaculos.obtenerNumeros(esp);
		lista.forEach(System.out::println);

		System.out.print("ID del numero: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		Numero n = lista.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);

		if (n == null) {
			System.out.println("No existe.");
			return;
		}

		// servicioEspectaculos.borrarNumero(n.getId());

		System.out.println("Numero borrado.");
	}

	// ============================================================
	// GESTION DE ARTISTAS EN NUMEROS
	// ============================================================

	/**
	 * Menu para anadir o quitar artistas de un numero concreto.
	 */
	private void gestionarArtistas(Scanner leer) {

		verEspectaculos();

		System.out.print("ID del espectaculo: ");
		Integer idEsp = leer.nextInt();
		leer.nextLine();

		Espectaculo esp = servicioEspectaculos.findById(idEsp);

		if (esp == null) {
			System.out.println("No existe.");
			return;
		}

		List<Numero> numeros = servicioEspectaculos.obtenerNumeros(esp);
		numeros.forEach(System.out::println);

		System.out.print("ID del numero: ");
		Integer idNum = leer.nextInt();
		leer.nextLine();

		Numero numero = numeros.stream().filter(n -> n.getId().equals(idNum)).findFirst().orElse(null);

		if (numero == null) {
			System.out.println("No existe.");
			return;
		}

		int op = -1;

		while (op != 0) {
			System.out.println("=== GESTION DE ARTISTAS EN NUMERO ===");
			System.out.println("1. Anadir artista");
			System.out.println("2. Quitar artista");
			System.out.println("0. Volver");

			try {
				op = leer.nextInt();
				leer.nextLine();
			} catch (Exception e) {
				leer.nextLine();
				continue;
			}

			switch (op) {
			case 1:
				anadirArtista(leer, numero);
				break;
			case 2:
				quitarArtista(leer, numero);
				break;
			case 0:
				break;
			default:
				System.out.println("Opcion invalida");
			}
		}
	}

	/**
	 * Anade un artista a un numero.
	 */
	private void anadirArtista(Scanner leer, Numero numero) {

		System.out.println("=== LISTA DE ARTISTAS DISPONIBLES ===");
		List<Artista> artistas = servicioArtistas.findAll();

		if (artistas.isEmpty()) {
			System.out.println("No hay artistas registrados.");
			return;
		}

		for (Artista a : artistas) {
			Persona p = a.getPersona();
			System.out.println(p.getId() + " - " + p.getNombre()
					+ (a.getApodo() != null ? " (" + a.getApodo() + ")" : "") + " | Especialidades: "
					+ String.join(", ", a.getEspecialidades().stream().map(Enum::name).toList()));
		}

		System.out.print("ID del artista a anadir: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		try {
			// servicioEspectaculos.anadirArtistaANumero(numero.getId(), id);
			System.out.println("Artista anadido.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Quita un artista de un numero.
	 */
	private void quitarArtista(Scanner leer, Numero numero) {

		System.out.println("=== ARTISTAS ASIGNADOS AL NUMERO ===");

		List<Artista> asignados = servicioEspectaculos.obtenerArtistasAsignados(numero.getId());

		if (asignados.isEmpty()) {
			System.out.println("No hay artistas asignados a este numero.");
			return;
		}

		for (Artista a : asignados) {
			Persona p = a.getPersona();
			System.out.println(
					p.getId() + " - " + p.getNombre() + (a.getApodo() != null ? " (Apodo: " + a.getApodo() + ")" : ""));
		}

		System.out.print("ID del artista a quitar: ");
		Integer id = leer.nextInt();
		leer.nextLine();

		try {
			// servicioEspectaculos.quitarArtistaDeNumero(numero.getId(), id);
			System.out.println("Artista eliminado del numero.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// ============================================================
	// REGISTRO DE PERSONA
	// ============================================================

	/**
	 * Permite registrar una nueva persona desde el menu ADMIN.
	 */
	private void registrarPersona(Scanner leer, Sesion sesion) {

		System.out.println("=== REGISTRO DE PERSONA ===");

		System.out.print("Nombre: ");
		String nombre = leer.nextLine();

		System.out.print("Email: ");
		String email = leer.nextLine();

		System.out.print("Nacionalidad (codigo pais): ");
		String nacionalidad = leer.nextLine();

		System.out.print("Usuario: ");
		String usuario = leer.nextLine();

		System.out.print("Contrasena: ");
		String contrasenia = leer.nextLine();

		System.out.println("Perfil:");
		System.out.println("1. ADMIN");
		System.out.println("2. COORDINACION");
		System.out.println("3. ARTISTA");
		System.out.print("Opcion: ");
		int opPerfil = leer.nextInt();
		leer.nextLine();

		Perfiles perfil;

		switch (opPerfil) {
		case 1 -> perfil = Perfiles.ADMIN;
		case 2 -> perfil = Perfiles.COORDINACION;
		case 3 -> perfil = Perfiles.ARTISTA;
		default -> {
			System.out.println("Perfil invalido.");
			return;
		}
		}

		boolean senior = false;
		LocalDate fechaSenior = null;
		String apodo = null;
		List<Especialidad> especialidades = null;

		if (perfil == Perfiles.ARTISTA) {
			System.out.print("Apodo: ");
			apodo = leer.nextLine();

			especialidades = elegirEspecialidades(leer);
		}

		try {
			servicioPersonas.registrarPersona(nombre, email, nacionalidad, usuario, contrasenia, perfil, senior,
					fechaSenior, apodo, especialidades, sesion.getNombrePersona());

			System.out.println("Persona registrada correctamente.");

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// ============================================================
	// MODIFICACION DE PERSONA
	// ============================================================

	/**
	 * Permite modificar los datos de una persona existente.
	 */

	/**
	 * Permite seleccionar varias especialidades introducidas por el usuario. Recibe
	 * una linea con especialidades separadas por comas, valida cada una y construye
	 * la lista final.
	 */
	private List<Especialidad> elegirEspecialidades(Scanner leer) {
		List<Especialidad> lista = new ArrayList<>();

		System.out.println(
				"Introduce especialidades separadas por coma (ACROBACIA, HUMOR, MAGIA, EQUILIBRISMO, MALABARISMO):");
		String linea = leer.nextLine();

		for (String esp : linea.split(",")) {
			esp = esp.trim().toUpperCase();
			if (!esp.isEmpty()) {
				try {
					lista.add(Especialidad.valueOf(esp));
				} catch (IllegalArgumentException e) {
					System.out.println("Especialidad no valida: " + esp);
				}
			}
		}

		return lista;
	}

	// ============================================================
	// CERRAR SESION
	// ============================================================

	/**
	 * Limpia los datos de la sesion activa.
	 */
	private void cerrarSesion(Sesion sesion) {
		sesion.setNombrePersona(null);
		sesion.setPerfil(null);
		System.out.println("Sesion cerrada.");
	}

	// ============================================================
	// MENUS VISUALES
	// ============================================================

	/**
	 * Menu mostrado a usuarios no autenticados.
	 */
	private void mostrarMenuInvitado() {
		System.out.println("=== INVITADO ===");
		System.out.println("1. Ver espectaculos");
		System.out.println("2. Iniciar sesion");
		System.out.println("0. Salir");
	}

	/**
	 * Menu mostrado a usuarios con perfil ARTISTA.
	 */
	private void mostrarMenuArtista() {
		System.out.println("=== ARTISTA ===");
		System.out.println("1. Ver ficha");
		System.out.println("2. Ver espectaculo");
		System.out.println("3. Ver espectaculo completo");
		System.out.println("0. Cerrar sesion");
	}

	/**
	 * Menu mostrado a usuarios con perfil COORDINACION.
	 */
	private void mostrarMenuCoordinacion() {
		System.out.println("=== COORDINACION ===");
		System.out.println("1. Crear espectaculo");
		System.out.println("2. Modificar espectaculo");
		System.out.println("3. Ver espectaculos");
		System.out.println("4. Gestionar numeros");
		System.out.println("5. Gestionar artistas");
		System.out.println("6. Ver espectaculo completo");
		System.out.println("0. Cerrar sesion");
	}

	/**
	 * Menu mostrado a usuarios con perfil ADMIN.
	 */
	private void mostrarMenuAdmin() {
		System.out.println("=== ADMIN ===");
		System.out.println("1. Registrar persona");
		System.out.println("2. Modificar persona");
		System.out.println("3. Crear espectaculo");
		System.out.println("4. Ver espectaculos");
		System.out.println("5. Modi2ficar espectaculos");
		System.out.println("6. Gestionar numeros");
		System.out.println("7. Gestionar artistas");
		System.out.println("8. Ver espectaculo completo");
		System.out.println("0. Cerrar sesion");
	}
}
