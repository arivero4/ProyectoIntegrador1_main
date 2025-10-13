package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la unidad territorial más pequeña dentro de un municipio. Cada vereda pertenece a un municipio y sirve para ubicar con mayor precisión los predios agrícolas inspeccionados.
 */
public class Vereda {

	/**
	 * Identificador único de la vereda dentro del sistema.
	 */
	private String id;

	/**
	 * Nombre de la vereda según la nomenclatura local.
	 */
	private String nombre;

	private Municipio municipio;

	private List<Predio> predios;

	/**
	 * Constructor de la clase. Inicializa una vereda sin valores definidos.
	 */
	public Vereda() {
		this.id = "";
		this.nombre = "";
		this.municipio = null;
		this.predios = new ArrayList<>();
	}

	/**
	 * Constructor con todos los parámetros usados en DAOs.
	 */
	public Vereda(String id, String nombre, Municipio municipio, java.util.List<Predio> predios) {
		this.id = id;
		this.nombre = nombre;
		this.municipio = municipio;
		this.predios = predios != null ? predios : new ArrayList<>();
	}

	/**
	 * Asigna el identificador único de la vereda.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador de la vereda.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Establece el nombre de la vereda.
	 */
	public void setNombre(String pNombre) {
		this.nombre = pNombre;
	}

	/**
	 * Retorna el nombre de la vereda.
	 */
	public String getNombre() {
		return this.nombre;
	}

	public void setMunicipio(Municipio pMunicipio) {
		this.municipio = pMunicipio;
	}

	public Municipio getMunicipio() {
		return this.municipio;
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
