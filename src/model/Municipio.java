package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una subdivisión administrativa de un departamento. Cada municipio pertenece a un departamento y puede contener varias veredas. Su función es estructurar la información territorial relacionada con las inspecciones.
 */
public class Municipio {

	/**
	 * Identificador único del municipio dentro del sistema.
	 */
	private String id;

	/**
	 * Nombre oficial del municipio.
	 */
	private String nombre;

	private Departamento departamento;

	private List<Vereda> veredas;

	/**
	 * Constructor de la clase. Crea una instancia vacía de un municipio.
	 */
	public Municipio() {
		this.id = "";
		this.nombre = "";
		this.departamento = null;
		this.veredas = new ArrayList<>();
	}

	/**
	 * Constructor con todos los parámetros usados en DAOs.
	 */
	public Municipio(String id, String nombre, Departamento departamento, java.util.List<Vereda> veredas) {
		this.id = id;
		this.nombre = nombre;
		this.departamento = departamento;
		this.veredas = veredas != null ? veredas : new ArrayList<>();
	}

	/**
	 * Asigna el identificador del municipio
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador del municipio.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Establece el nombre del municipio.
	 */
	public void setNombre(String pNombre) {
		this.nombre = pNombre;
	}

	/**
	 * Retorna el nombre del municipio.
	 */
	public String getNombre() {
		return this.nombre;
	}

	public void setDepartamento(Departamento pDepartamento) {
		this.departamento = pDepartamento;
	}

	public Departamento getDepartamento() {
		return this.departamento;
	}

	public void agregarVereda(Vereda pVereda) {
		if (pVereda != null && !this.veredas.contains(pVereda)) {
			this.veredas.add(pVereda);
		}
	}

	public Vereda getVeredas() {
		return this.veredas.isEmpty() ? null : this.veredas.get(0);
	}

}
