/**
 * Servicio encargado de gestionar todas las operaciones relacionadas con personas.
 *
 * Incluye:
 * - Validaciones de email, usuario, contrasenia y nacionalidad
 * - Registro completo de personas con su perfil correspondiente
 * - Gestion de credenciales, artistas y coordinadores
 * - Modificacion de datos personales y de acceso
 * - Eliminacion segura de una persona y sus entidades asociadas
 * - Carga de paises desde un archivo XML para validar nacionalidades
 *
 * Este servicio forma parte de la capa de negocio y es utilizado en los casos
 * de uso CU2 (login), CU3A/B/C (gestion de personas y credenciales) y CU6
 * (informacion de artistas).
 *
 * Autor: Michael
 * @version 1.0
 */
package com.luisdbb.tarea3AD2024base.services;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.luisdbb.tarea3AD2024base.modelo.*;
import com.luisdbb.tarea3AD2024base.repositorios.*;

import jakarta.transaction.Transactional;

/**
 * Servicio encargado de gestionar todas las operaciones relacionadas con
 * personas.
 *
 * Incluye validaciones básicas (email, usuario, contraseña), validación de
 * nacionalidades mediante el archivo paises.xml, registro completo de personas
 * con su perfil correspondiente (ADMIN, COORDINACION o ARTISTA), y
 * actualización de datos personales y credenciales.
 *
 * Este servicio participa en los casos de uso: - CU3A: Registro de personas -
 * CU3B: Asignación de credenciales - CU3C: Gestión de datos personales
 *
 * Forma parte de la capa de servicios dentro del modelo de aplicación.
 *
 * @author MichaelQP
 * @version 1.1
 * @since 2026
 */
@Service
public class ServicioPersonas {
	@Autowired
	private ServicioLogOperaciones servicioLog;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private CredencialesRepository credencialesRepository;

	@Autowired
	private ArtistaRepository artistaRepository;

	@Autowired
	private CoordinacionRepository coordinacionRepository;

	// ============================================================
	// VALIDACIONES BÁSICAS
	// ============================================================

	/**
	 * Valida el formato de un email utilizando una expresión regular sencilla.
	 *
	 * @param email email a validar
	 * @return true si el formato es válido, false en caso contrario
	 */
	public boolean emailValido(String email) {
		return email != null && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	}

	/**
	 * Valida el nombre de usuario según las reglas del sistema. Debe contener solo
	 * letras minúsculas y tener al menos 3 caracteres.
	 *
	 * @param usuario nombre de usuario
	 * @return true si es válido, false en caso contrario
	 */
	public boolean usuarioValido(String usuario) {
		return usuario != null && usuario.matches("^[a-z]{3,}$");
	}

	/**
	 * Valida la contraseña según las reglas del sistema. Debe tener al menos 3
	 * caracteres y no contener espacios.
	 *
	 * @param pass contraseña a validar
	 * @return true si es válida, false en caso contrario
	 */
	public boolean contraseniaValida(String pass) {
		return pass != null && pass.length() >= 3 && !pass.contains(" ");
	}

	// ============================================================
	// VALIDACIÓN DE PAÍSES DESDE paises.xml
	// ============================================================

	private static final List<String> IDS_PAISES = new ArrayList<>();
	private static boolean paisesCargados = false;

	/**
	 * Carga los códigos de país desde el archivo paises.xml si aún no se han
	 * cargado. Este archivo se encuentra en la carpeta resources del proyecto.
	 */
	private void cargarPaisesSiHaceFalta() {
		if (paisesCargados)
			return;

		IDS_PAISES.clear();

		try (InputStream is = getClass().getClassLoader().getResourceAsStream("paises.xml")) {

			if (is == null) {
				System.out.println("No se encontró paises.xml en resources.");
				return;
			}

			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(is);

			NodeList idsNodes = doc.getElementsByTagName("id");

			for (int i = 0; i < idsNodes.getLength(); i++) {
				String id = idsNodes.item(i).getTextContent();
				if (id != null && !id.trim().isEmpty()) {
					IDS_PAISES.add(id.trim().toUpperCase());
				}
			}

			paisesCargados = true;

		} catch (Exception e) {
			System.out.println("Error cargando paises desde XML: " + e.getMessage());
		}
	}

	/**
	 * Valida si un código de país existe en el archivo paises.xml.
	 *
	 * @param idPais código de país
	 * @return true si el país está registrado, false en caso contrario
	 */
	public boolean nacionalidadValida(String idPais) {
		cargarPaisesSiHaceFalta();
		if (idPais == null)
			return false;
		return IDS_PAISES.contains(idPais.trim().toUpperCase());
	}

	// ============================================================
	// REGISTRO DE PERSONA
	// ============================================================

	/**
	 * Registra una persona completa en el sistema, incluyendo: - Persona -
	 * Credenciales - Coordinación o Artista según el perfil asignado
	 *
	 * Realiza todas las validaciones necesarias antes de persistir los datos.
	 *
	 * @param nombre         nombre completo
	 * @param email          email único
	 * @param nacionalidad   código de país válido
	 * @param usuario        nombre de usuario
	 * @param contrasenia    contraseña
	 * @param perfil         perfil asignado (ADMIN, COORDINACION, ARTISTA)
	 * @param senior         estado de senioridad (solo para coordinadores)
	 * @param fechaSenior    fecha de obtención de senioridad
	 * @param apodo          apodo del artista
	 * @param especialidades lista de especialidades del artista
	 * @return persona registrada
	 */
	@Transactional
	public Persona registrarPersona(String nombre, String email, String nacionalidad, String usuario,
			String contrasenia, Perfiles perfil, boolean senior, LocalDate fechaSenior, String apodo,
			List<Especialidad> especialidades, String usuarioActual) {

		if (!emailValido(email))
			throw new IllegalArgumentException("Email inválido");

		if (!nacionalidadValida(nacionalidad))
			throw new IllegalArgumentException("Nacionalidad inválida según paises.xml");

		if (personaRepository.existsByEmail(email))
			throw new IllegalArgumentException("Ya existe una persona con ese email");

		if (!usuarioValido(usuario))
			throw new IllegalArgumentException("Usuario inválido");

		if (credencialesRepository.existsByNombreUsuario(usuario))
			throw new IllegalArgumentException("Ya existe ese nombre de usuario");

		if (!contraseniaValida(contrasenia))
			throw new IllegalArgumentException("Contraseña inválida");

		Persona persona = new Persona(nombre, email, nacionalidad);

		Credenciales credenciales = new Credenciales(persona, usuario, contrasenia, perfil);
		persona.setCredenciales(credenciales);

		persona = personaRepository.save(persona);

		if (perfil == Perfiles.COORDINACION) {
			Coordinacion c = new Coordinacion(persona, senior, fechaSenior);
			coordinacionRepository.save(c);
		}

		if (perfil == Perfiles.ARTISTA) {
			Artista a = new Artista(persona, apodo, especialidades != null ? especialidades : new ArrayList<>());
			artistaRepository.save(a);
		}

		// ===== EL LOG YE ESTO =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.NUEVO, "Insertada Persona id " + persona.getId());

		return persona;
	}

	// ============================================================
	// MODIFICACIÓN DE PERSONA
	// ============================================================

	/**
	 * Actualiza los datos básicos de una persona.
	 *
	 * @param persona persona a modificar
	 * @return persona actualizada
	 */
	public Persona actualizarPersona(Persona persona, String usuarioActual) {

		Persona guardada = personaRepository.save(persona);

		// ===== NUEVO LO DEL LOG WAZAAAA =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.ACTUALIZACION,
				"Actualizada Persona id " + guardada.getId());

		return guardada;
	}

	/**
	 * Actualiza el perfil de una persona modificando sus credenciales.
	 *
	 * @param persona     persona asociada
	 * @param nuevoPerfil nuevo perfil asignado
	 */
	public void actualizarPerfil(Persona persona, Perfiles nuevoPerfil, String usuarioActual) {

	    Credenciales cred = credencialesRepository.findByIdPersona(persona.getId());
	    cred.setPerfil(nuevoPerfil);
	    credencialesRepository.save(cred);

	    // ===== LOG =====
	    servicioLog.registrarOperacion(
	            usuarioActual,
	            TipoOperacion.ACTUALIZACION,
	            "Actualizado perfil Persona id " + persona.getId() + " a " + nuevoPerfil
	    );
	}


	/**
	 * Actualiza el nombre de usuario de una persona.
	 *
	 * @param persona      persona asociada
	 * @param nuevoUsuario nuevo nombre de usuario
	 */
	public void actualizarUsuario(Persona persona, String nuevoUsuario, String usuarioActual) {

		if (!usuarioValido(nuevoUsuario))
			throw new IllegalArgumentException("Usuario inválido");

		Credenciales cred = credencialesRepository.findByIdPersona(persona.getId());
		cred.setNombreUsuario(nuevoUsuario);
		credencialesRepository.save(cred);

		// ===== LOG =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.ACTUALIZACION,
				"Actualizado nombreUsuario Persona id " + persona.getId());
	}

	/**
	 * Actualiza el email de una persona.
	 *
	 * Valida el formato del email antes de aplicarlo.
	 *
	 * @param persona    persona asociada
	 * @param nuevoEmail nuevo email
	 * @return persona actualizada
	 */
	public Persona actualizarEmail(Persona persona, String nuevoEmail, String usuarioActual) {

		if (!emailValido(nuevoEmail))
			throw new IllegalArgumentException("Email invalido");

		persona.setEmail(nuevoEmail);
		Persona guardada = personaRepository.save(persona);

		// ===== LOG =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.ACTUALIZACION,
				"Actualizado email Persona id " + guardada.getId());

		return guardada;
	}

	/**
	 * Actualiza la nacionalidad de una persona.
	 *
	 * Valida el código de país utilizando los datos cargados desde paises.xml.
	 *
	 * @param persona  persona asociada
	 * @param nuevaNac nuevo código de país
	 * @return persona actualizada
	 */
	public Persona actualizarNacionalidad(Persona persona, String nuevaNac, String usuarioActual) {

		if (!nacionalidadValida(nuevaNac))
			throw new IllegalArgumentException("Nacionalidad invalida segun paises.xml");

		persona.setNacionalidad(nuevaNac);
		Persona guardada = personaRepository.save(persona);

		// ===== LOG =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.ACTUALIZACION,
				"Actualizada nacionalidad Persona id " + guardada.getId());

		return guardada;
	}

	/**
	 * Actualiza la contraseña de una persona.
	 *
	 * @param persona   persona asociada
	 * @param nuevaPass nueva contraseña
	 */
	public void actualizarContrasenia(Persona persona, String nuevaPass, String usuarioActual) {

		if (!contraseniaValida(nuevaPass))
			throw new IllegalArgumentException("Contrasenia invalida");

		Credenciales cred = credencialesRepository.findByIdPersona(persona.getId());
		cred.setContrasenia(nuevaPass);
		credencialesRepository.save(cred);

		// ===== LOG =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.ACTUALIZACION,
				"Actualizada contraseña Persona id " + persona.getId());
	}

	// ============================================================
	// BÚSQUEDAS
	// ============================================================

	/**
	 * Busca una persona por su identificador.
	 *
	 * @param id identificador de la persona
	 * @return persona encontrada o null si no existe
	 */
	public Persona findById(Integer id) {
		return personaRepository.findById(id).orElse(null);
	}

	/**
	 * Busca una persona por su nombre exacto.
	 *
	 * @param nombre nombre completo
	 * @return persona encontrada o null si no existe
	 */
	public Persona findByNombre(String nombre) {
		return personaRepository.findByNombre(nombre);
	}

	/**
	 * Obtiene todas las personas registradas.
	 *
	 * @return lista completa de personas
	 */
	public List<Persona> findAll() {
		return personaRepository.findAll();
	}

	/**
	 * Obtiene las credenciales asociadas a una persona.
	 *
	 * @param persona persona asociada
	 * @return credenciales correspondientes
	 */
	public Credenciales getCredenciales(Persona persona) {
		return credencialesRepository.findByIdPersona(persona.getId());
	}

	/**
	 * Comprueba si existe una persona con un id dado.
	 *
	 * @param id identificador de la persona
	 * @return true si existe, false en caso contrario
	 */
	public boolean exists(Integer id) {
		return personaRepository.existsById(id);
	}

	/**
	 * Indica si una persona es coordinador.
	 *
	 * @param persona persona a comprobar
	 * @return true si pertenece a coordinación
	 */
	public boolean esCoordinador(Persona persona) {
		return coordinacionRepository.existsByIdPersona(persona.getId());
	}

	/**
	 * Indica si una persona es artista.
	 *
	 * @param persona persona a comprobar
	 * @return true si es artista
	 */
	public boolean esArtista(Persona persona) {
		return artistaRepository.existsByIdPersona(persona.getId());
	}

	/**
	 * Obtiene todas las personas que son coordinadores.
	 *
	 * @return lista de personas coordinadoras
	 */
	public List<Persona> findAllCoordinadores() {
		return coordinacionRepository.findAll().stream().map(Coordinacion::getPersona).toList();
	}

	/**
	 * Obtiene todas las personas que son artistas.
	 *
	 * @return lista de personas artistas
	 */
	public List<Persona> findAllArtistas() {
		return personaRepository.findAll().stream().filter(this::esArtista).toList();
	}

	/**
	 * Obtiene el artista asociado a una persona.
	 *
	 * @param p persona asociada
	 * @return artista correspondiente o null si no existe
	 */
	public Artista findArtistaByPersona(Persona p) {
		return artistaRepository.findById(p.getId()).orElse(null);
	}

	// ============================================================
	// ELIMINAR PERSONA (COMPLETO Y SEGURO)
	// ============================================================

	/**
	 * Elimina una persona y todas sus entidades asociadas: - Credenciales - Artista
	 * - Coordinación - Persona
	 *
	 * Se ejecuta dentro de una transacción para garantizar consistencia.
	 *
	 * @param idPersona identificador de la persona a eliminar
	 */
	@Transactional
	public void delete(Integer idPersona, String usuarioActual) {

		Credenciales cred = credencialesRepository.findByIdPersona(idPersona);
		if (cred != null)
			credencialesRepository.delete(cred);

		Artista artista = artistaRepository.findByIdPersona(idPersona);
		if (artista != null)
			artistaRepository.delete(artista);

		Coordinacion coord = coordinacionRepository.findByIdPersona(idPersona);
		if (coord != null)
			coordinacionRepository.delete(coord);

		personaRepository.deleteById(idPersona);

		// ===== LOG =====
		servicioLog.registrarOperacion(usuarioActual, TipoOperacion.BORRADO, "Borrada Persona id " + idPersona);
	}

	// ============================================================
	// COORDINADORES
	// ============================================================

	/**
	 * Devuelve todas las entidades Coordinacion completas. Útil para acceder a
	 * datos como senioridad o fechaSenior.
	 *
	 * @return lista de entidades Coordinacion
	 */
	public List<Coordinacion> findAllCoordinaciones() {
		List<Coordinacion> lista = coordinacionRepository.findAll();
		lista.forEach(c -> c.getPersona().getNombre());
		return lista;
	}

	/**
	 * Busca una persona por su email.
	 *
	 * @param email email de la persona
	 * @return persona encontrada o null si no existe
	 */
	public Persona findByEmail(String email) {
		return personaRepository.findByEmail(email);
	}

	/**
	 * Exporta la ficha completa de un artista a un archivo XML.
	 *
	 * Incluye: - Datos personales - Apodo - Especialidades
	 *
	 * @param artista artista a exportar
	 * @param destino archivo XML de destino
	 */
	public void exportarFichaArtistaXML(Artista artista, File destino) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element root = doc.createElement("artista");
			doc.appendChild(root);

			Element nombre = doc.createElement("nombre");
			nombre.setTextContent(artista.getPersona().getNombre());
			root.appendChild(nombre);

			Element email = doc.createElement("email");
			email.setTextContent(artista.getPersona().getEmail());
			root.appendChild(email);

			Element nacionalidad = doc.createElement("nacionalidad");
			nacionalidad.setTextContent(artista.getPersona().getNacionalidad());
			root.appendChild(nacionalidad);

			Element apodo = doc.createElement("apodo");
			apodo.setTextContent(artista.getApodo());
			root.appendChild(apodo);

			Element espList = doc.createElement("especialidades");
			root.appendChild(espList);

			for (Especialidad e : artista.getEspecialidades()) {
				Element esp = doc.createElement("especialidad");
				esp.setTextContent(e.name());
				espList.appendChild(esp);
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(destino);

			transformer.transform(source, result);

		} catch (Exception e) {
			throw new RuntimeException("Error exportando ficha XML: " + e.getMessage(), e);
		}
	}
}
