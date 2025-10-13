package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa a las personas que interactúan con el sistema (inspectores, técnicos, administradores o supervisores).
 * Cada usuario tiene un rol y una serie de permisos que determinan las acciones que puede ejecutar dentro del sistema de inspecciones fitosanitarias.
 */
public abstract class Usuario {

	/**
	 * Identificador único del usuario dentro del sistema. Permite diferenciarlo de otros usuarios y es usado en los registros de auditoría.
	 */
	private String id;

	/**
	 * Define el rol asignado al usuario (por ejemplo: Inspector, Administrador, Técnico, Propietario). Este valor determina su nivel de acceso y permisos.
	 */
	private String rol;

	/**
	 * Lista que contiene los permisos específicos otorgados al usuario según su rol. Facilita la gestión de seguridad en el sistema.
	 */
	private List<String> permisos;

	/**
	 * Documento de identidad del usuario, utilizado para validación y registro oficial.
	 */
	private String numeroIdentificacion;

	/**
	 * Nombre completo del usuario registrado.
	 */
	private String nombre;

	/**
	 * Número de teléfono o celular asociado al usuario, utilizado para comunicación directa.
	 */
	private String telefonoContacto;

	/**
	 * Dirección de correo electrónico usada para notificaciones o acceso al sistema.
	 */
	private String correoElectronico;

	/**
	 * Constructor de la clase. Inicializa un nuevo usuario con valores predeterminados o vacíos.
	 */
	public Usuario() {
		this.id = "";
		this.rol = "";
		this.permisos = new ArrayList<>();
		this.numeroIdentificacion = "";
		this.nombre = "";
		this.telefonoContacto = "";
		this.correoElectronico = "";
	}

	/**
	 * Constructor con todos los parámetros usados por Propietario.
	 */
	public Usuario(String idUsuario, String rol, java.util.List<String> permisos, String tipoIdentificacion, String numeroIdentificacion, String nombresCompletos, String telefonoContacto, String correoElectronico, String codigoICAPredio, java.util.List<String> lugaresProduccion) {
		this.id = idUsuario;
		this.rol = rol;
		this.permisos = permisos != null ? permisos : new ArrayList<>();
		this.numeroIdentificacion = numeroIdentificacion;
		this.nombre = nombresCompletos;
		this.telefonoContacto = telefonoContacto;
		this.correoElectronico = correoElectronico;
		// tipoIdentificacion, codigoICAPredio y lugaresProduccion pueden almacenarse en subclases si es necesario
	}

	/**
	 * Establece el identificador único del usuario.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador del usuario.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Asigna el rol del usuario.
	 */
	public void setRol(String pRol) {
		this.rol = pRol;
	}

	/**
	 * Devuelve el rol del usuario.
	 */
	public String getRol() {
		return this.rol;
	}

	/**
	 * Asigna la lista de permisos que el usuario puede ejercer.
	 */
	public void setPermiso(List<String> pPermiso) {
		this.permisos = pPermiso != null ? pPermiso : new ArrayList<>();
	}

	/**
	 * Devuelve la lista actual de permisos asociados al usuario.
	 */
	public List<String> getPermiso() {
		return this.permisos;
	}

	/**
	 * Asigna el número de identificación oficial del usuario.
	 */
	public void setNumeroIdentificacion(String pNumeroIdentificacion) {
		this.numeroIdentificacion = pNumeroIdentificacion;
	}

	/**
	 * Retorna el número de identificación del usuario.
	 */
	public String getNumeroIdentificacion() {
		return this.numeroIdentificacion;
	}

	/**
	 * Define el nombre completo del usuario.
	 */
	public void setNombre(String pNombre) {
		this.nombre = pNombre;
	}

	/**
	 * Devuelve el nombre actual del usuario.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Asigna el número de teléfono o celular del usuario.
	 */
	public void setTelefonoContacto(String pTelefonoContacto) {
		this.telefonoContacto = pTelefonoContacto;
	}

	/**
	 * Retorna el número de contacto registrado del usuario.
	 */
	public String getTelefonoContacto() {
		return this.telefonoContacto;
	}

	/**
	 * Asigna el correo electrónico del usuario.
	 */
	public void setCorreoElectronico(String pCorreoElectronico) {
		this.correoElectronico = pCorreoElectronico;
	}

	/**
	 * Devuelve el correo electrónico asociado al usuario.
	 */
	public String getCorreoElectronico() {
		return this.correoElectronico;
	}

}
