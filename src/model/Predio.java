package model;

import java.util.ArrayList;
import java.util.List;

/**
 * a clase Predio representa la unidad física de producción agrícola dentro del sistema de inspecciones fitosanitarias. Cada predio corresponde a un terreno o finca donde se desarrollan cultivos, se realizan inspecciones y se registran condiciones ambientales o fitosanitarias.
 */
public class Predio {

	/**
	 * Identificador único del predio dentro del sistema. Permite distinguirlo de otros registros y es usado como clave principal en las relaciones con otras clases.
	 */
	private String id;

	/**
	 * Código alfanumérico que identifica oficialmente el predio ante las entidades catastrales. Es fundamental para la gestión territorial y la verificación de la propiedad.
	 */
	private String codigoIca;

	/**
	 * Ubicación física o dirección específica del predio. Puede incluir nombres de veredas, coordenadas o vías de acceso.
	 */
	private String direccion;

	/**
	 * Tamaño total del predio en hectáreas o metros cuadrados. Permite calcular la extensión destinada a los cultivos y controlar las áreas inspeccionadas.
	 */
	private double area;

	/**
	 * Coordenada geográfica que indica la posición norte-sur del predio en un mapa. Es usada para la localización mediante sistemas GIS.
	 */
	private double latitud;

	/**
	 * Coordenada geográfica que indica la posición este-oeste del predio. Junto con la latitud, permite ubicar el predio de manera precisa.
	 */
	private double longitud;

	private Propietario propietario;

	private List<LugarProduccion> lugaresProduccion;
    
	/**
	 * Identificador de la vereda donde se ubica el predio (FK a tabla vereda)
	 */
	private String idVereda;
	/** Código del lugar de producción asociado al predio (opcional) */
	private String codLugarProduccion;

	/**
	 * 
	 * Constructor de la clase. Inicializa un nuevo objeto Predio con valores vacíos o predeterminados.
	 */
	public Predio() {
		this.id = "";
		this.codigoIca = "";
		this.direccion = "";
		this.area = 0.0;
		this.latitud = 0.0;
		this.longitud = 0.0;
		this.propietario = null;
		this.lugaresProduccion = new ArrayList<>();
		this.idVereda = "";
		this.codLugarProduccion = "";
	}

	/**
	 * Retorna el identificador único del predio.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Asigna el valor del identificador único del predio.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Establece el código catastral del predio.
	 */
	public void setCodigoIca(String pCodigoIca) {
		this.codigoIca = pCodigoIca;
	}

	/**
	 * Devuelve el código catastral asociado al predio.
	 */
	public String getCodigoIca() {
		return this.codigoIca;
	}

	/**
	 * Permite modificar la dirección registrada del predio.
	 */
	public void setDireccion(String pDireccion) {
		this.direccion = pDireccion;
	}

	/**
	 * Retorna la dirección o ubicación textual del predio.
	 */
	public String getDireccion() {
		return this.direccion;
	}

	/**
	 * Establece o actualiza el valor del área del predio.
	 */
	public void setArea(double pArea) {
		this.area = pArea;
	}

	/**
	 * Devuelve el área total del predio.
	 */
	public double getArea() {
		return this.area;
	}

	/**
	 * Define la coordenada de latitud geográfica.
	 */
	public void setLatitud(double pLatitud) {
		this.latitud = pLatitud;
	}

	/**
	 * Devuelve la coordenada de latitud correspondiente al predio.
	 */
	public double getLatitud() {
		return this.latitud;
	}

	/**
	 * Asigna el valor de longitud geográfica del predio.
	 */
	public void setLongitud(double pLongitud) {
		this.longitud = pLongitud;
	}

	/**
	 * Retorna la coordenada de longitud del predio.
	 */
	public double getLongitud() {
		return this.longitud;
	}

	public void setPropietario(Propietario pPropietario) {
		this.propietario = pPropietario;
	}

	public Propietario getPropietario() {
		return this.propietario;
	}

	public void setIdVereda(String idVereda) {
		this.idVereda = idVereda;
	}

	public String getIdVereda() {
		return this.idVereda;
	}

	public void setCodLugarProduccion(String codLugarProduccion) {
		this.codLugarProduccion = codLugarProduccion;
	}

	public String getCodLugarProduccion() {
		return this.codLugarProduccion;
	}

	public void agregarLugarProduccion(LugarProduccion pLugarProduccion) {
		if (pLugarProduccion != null && !this.lugaresProduccion.contains(pLugarProduccion)) {
			this.lugaresProduccion.add(pLugarProduccion);
		}
	}

	public LugarProduccion getLugaresProduccion() {
		return this.lugaresProduccion.isEmpty() ? null : this.lugaresProduccion.get(0);
	}

}
