package model;

import java.util.ArrayList;
import java.util.List;

/**
 * modela la información básica de la persona natural o jurídica dueña del predio. Permite establecer la relación legal entre los terrenos agrícolas y sus respectivos titulares.
 */
public class Propietario extends Usuario {
	// Dirección del propietario
	private String direccion;

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDireccion() {
		return this.direccion;
	}
	public String getTipoIdentificacion() {
		// Si tienes un atributo específico, retorna ese valor. Si no, usa el método de Usuario.
		return super.getRol(); // Cambia esto si tienes un atributo tipoIdentificacion
	}

	public String getNombresCompletos() {
		return super.getNombre();
	}

	public String getCodigoICAPredio() {
		// Si tienes un atributo específico, retorna ese valor. Si no, retorna vacío o el id.
		return this.id;
	}

	/**
	 * Identificador único del propietario dentro del sistema. Se utiliza como clave primaria para establecer relaciones con los predios.
	 */
	private String id;

	private List<Predio> predios;

	/**
	 * Constructor de la clase. Inicializa un objeto Propietario vacío.
	 */
	public Propietario() {
		super();
		this.id = "";
		this.predios = new ArrayList<>();
	}

	/**
	 * Constructor con todos los parámetros usados en la interfaz y DAOs.
	 */
	public Propietario(String idUsuario, String rol, java.util.List<String> permisos, String tipoIdentificacion, String numeroIdentificacion, String nombresCompletos, String telefonoContacto, String correoElectronico, String codigoICAPredio, java.util.List<String> lugaresProduccion) {
		super(idUsuario, rol, permisos, tipoIdentificacion, numeroIdentificacion, nombresCompletos, telefonoContacto, correoElectronico, codigoICAPredio, lugaresProduccion);
		this.id = idUsuario;
		this.predios = new ArrayList<>();
	}

	/**
	 * Asigna el identificador único al propietario.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Retorna el identificador actual del propietario.
	 */
	public String getId() {
		return this.id;
	}

	public void agregarPredio(Predio pPredio) {
		if (pPredio != null && !this.predios.contains(pPredio)) {
			this.predios.add(pPredio);
		}
	}

	public Predio getPredios() {
		return this.predios.isEmpty() ? null : this.predios.get(0);
	}

}
