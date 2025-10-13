package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la división territorial principal donde se agrupan los municipios. Contiene información básica de identificación y nombre del departamento. Es utilizada como referencia geográfica dentro del sistema de inspecciones fitosanitarias.
 */
public class Departamento {

	/**
	 * Identificador único del departamento, utilizado para diferenciarlo de otros departamentos dentro del sistema.
	 */
	private String id;

	/**
	 * Nombre oficial del departamento según la división política administrativa.
	 */
	private String nombre;

	private List<Municipio> municipios;

	/**
	 * Constructor de la clase. Inicializa un objeto de tipo Departamento sin valores predefinidos.
	 */
	public Departamento() {
		this.id = "";
		this.nombre = "";
		this.municipios = new ArrayList<>();
	}

	/**
	 * Constructor con todos los parámetros usados en DAOs.
	 */
	public Departamento(String id, String nombre, java.util.List<Municipio> municipios) {
		this.id = id;
		this.nombre = nombre;
		this.municipios = municipios != null ? municipios : new ArrayList<>();
	}

	/**
	 * Asigna el identificador único del departamento.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Establece el nombre del departamento.
	 */
	public void setNombre(String pNombre) {
		this.nombre = pNombre;
	}

	/**
	 * Devuelve el identificador del departamento.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Retorna el nombre del departamento registrado.
	 */
	public String getNombre() {
		return this.nombre;
	}

	public void agregarMunicipio(Municipio pMunicipio) {
		if (pMunicipio != null && !this.municipios.contains(pMunicipio)) {
			this.municipios.add(pMunicipio);
		}
	}

	public Municipio getMunicipios() {
		return this.municipios.isEmpty() ? null : this.municipios.get(0);
	}

}
