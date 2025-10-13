package model;

import java.util.ArrayList;
import java.util.List;

public class AsistenteTecnico extends Usuario {

	private String id;

	private String numeroTarjetaProfesional;

	private List<LugarProduccion> lugaresProduccion;

	private List<InspeccionFitosanitaria> inspeccionesFitosanitarias;

	public AsistenteTecnico() {
		super();
		this.id = "";
		this.numeroTarjetaProfesional = "";
		this.lugaresProduccion = new ArrayList<>();
		this.inspeccionesFitosanitarias = new ArrayList<>();
	}

	public void setId(String pId) {
		this.id = pId;
	}

	public String getId() {
		return this.id;
	}

	public void setNumeroTarjetaProfesional(String pNumeroTarjetaProfesional) {
		this.numeroTarjetaProfesional = pNumeroTarjetaProfesional;
	}

	public String getNumeroTarjetaProfesional() {
		return this.numeroTarjetaProfesional;
	}

	public void agregarLugarProduccion(LugarProduccion pLugarProduccion) {
		if (pLugarProduccion != null && !this.lugaresProduccion.contains(pLugarProduccion)) {
			this.lugaresProduccion.add(pLugarProduccion);
		}
	}

	public LugarProduccion getLugaresProduccion() {
		return this.lugaresProduccion.isEmpty() ? null : this.lugaresProduccion.get(0);
	}

	public void agregarInspeccionFitosanitaria(InspeccionFitosanitaria pInspeccionFitosanitaria) {
		if (pInspeccionFitosanitaria != null && !this.inspeccionesFitosanitarias.contains(pInspeccionFitosanitaria)) {
			this.inspeccionesFitosanitarias.add(pInspeccionFitosanitaria);
		}
	}

	public InspeccionFitosanitaria getInspeccionFitosanitaria() {
		return this.inspeccionesFitosanitarias.isEmpty() ? null : this.inspeccionesFitosanitarias.get(0);
	}

}
