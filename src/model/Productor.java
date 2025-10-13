package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa a la persona o entidad responsable de las actividades agrícolas dentro de un lugar de producción. Su rol es esencial para la administración, ya que está vinculado directamente con los lotes y las producciones que gestiona.
 */
public class Productor extends Usuario {

	/**
	 * Identificador único del productor. Este valor permite relacionarlo con sus lugares de producción y mantener una trazabilidad clara en el sistema.
	 */
	private String id;

	private List<LugarProduccion> lugaresProduccion;

	/**
	 * Constructor por defecto que permite crear un objeto productor vacío para luego asignarle datos mediante los métodos setter.
	 */
	public Productor() {
		super();
		this.id = "";
		this.lugaresProduccion = new ArrayList<>();
	}

	/**
	 * Establece el identificador único del productor, asegurando su diferenciación dentro del sistema.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador actual del productor, usado en búsquedas o vínculos con otros elementos (por ejemplo, lugares de producción).
	 */
	public String getId() {
		return this.id;
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
