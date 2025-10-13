package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa una unidad específica dentro de un lugar de producción agrícola. Cada lote tiene un identificador único, una descripción que detalla sus características o uso, y un valor numérico que indica su extensión o tamaño.
 */
public class Lote {

	/**
	 * Identificador único del lote dentro del sistema. Se utiliza para distinguirlo de otros lotes, facilitando su búsqueda y gestión.
	 */
	private String id;

	/**
	 * Texto descriptivo que detalla las características del lote, como el tipo de terreno, el uso del suelo o información adicional relevante.
	 */
	private String descripcion;

	/**
	 * Valor numérico que representa el área total del lote (por ejemplo, en hectáreas o metros cuadrados). Es importante para cálculos de producción o distribución de cultivos.
	 */
	private double extension;

	private LugarProduccion lugarProduccion;

	private InspeccionFitosanitaria inspeccionFitosanitaria;

	private List<Cultivo> cultivos;

	/**
	 * Constructor de la clase. Inicializa un objeto vacío, permitiendo asignar los valores posteriormente mediante los métodos setter.
	 */
	public Lote() {
		this.id = "";
		this.descripcion = "";
		this.extension = 0.0;
		this.lugarProduccion = null;
		this.inspeccionFitosanitaria = null;
		this.cultivos = new ArrayList<>();
	}

	/**
	 * signa el valor del identificador del lote. Se utiliza para establecer un código único y mantener la integridad de los datos.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Retorna el identificador actual del lote, útil para búsquedas, reportes o relaciones con otros objetos del sistema.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Define o modifica la descripción del lote, permitiendo registrar detalles o actualizaciones sobre su estado o uso.
	 */
	public void setDescripcion(String pDescripcion) {
		this.descripcion = pDescripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * Permite establecer o actualizar la extensión del lote, asegurando que los datos de tamaño sean precisos.
	 */
	public void setExtension(double pExtension) {
		this.extension = pExtension;
	}

	/**
	 * Devuelve la extensión actual del lote. Es útil en procesos donde se calculan rendimientos, costos o distribución de recursos.
	 */
	public double getExtension() {
		return this.extension;
	}

	public void setLugarProduccion(LugarProduccion pLugarProduccion) {
		this.lugarProduccion = pLugarProduccion;
	}

	public LugarProduccion getLugarProduccion() {
		return this.lugarProduccion;
	}

	public void setInspeccionFitosanitaria(InspeccionFitosanitaria pInspeccionFitosanitaria) {
		this.inspeccionFitosanitaria = pInspeccionFitosanitaria;
	}

	public InspeccionFitosanitaria getInspeccionFitosanitaria() {
		return this.inspeccionFitosanitaria;
	}

	public void agregarCultivo(Cultivo pCultivo) {
		if (pCultivo != null && !this.cultivos.contains(pCultivo)) {
			this.cultivos.add(pCultivo);
		}
	}

	public Cultivo getCultivo() {
		return this.cultivos.isEmpty() ? null : this.cultivos.get(0);
	}

}
