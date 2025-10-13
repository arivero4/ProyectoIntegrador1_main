package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa la información cuantitativa y cualitativa recolectada durante una inspección fitosanitaria.
 * Su propósito es registrar los resultados obtenidos de la observación directa de los cultivos, incluyendo la cantidad de plantas evaluadas, las afectadas por plagas o enfermedades y las observaciones técnicas realizadas por el inspector.
 */
public class ResultadoTecnico {
	private String inspeccionId;
	private String descripcion;
	private String fecha;

	/**
	 * Identificador único del resultado técnico, utilizado para su trazabilidad dentro del sistema.
	 */
	private String id;

	/**
	 * Número total de plantas inspeccionadas durante la visita técnica.
	 */
	private int totalPlantasEvaluadas;

	/**
	 * Cantidad de plantas encontradas con síntomas de plagas o enfermedades. Este valor es crucial para determinar el nivel de infestación.
	 */
	private int plantasAfectadas;

	/**
	 * Texto libre con las observaciones técnicas realizadas por el inspector, incluyendo condiciones del cultivo, presencia de vectores o factores ambientales relevantes.
	 */
	private String observaciones;

	private List<Cultivo> cultivos;

	private List<Plaga> plagas;

	private InspeccionFitosanitaria inspeccionFitosanitaria;

	private InformeFitosanitario informeFitosanitario;

	/**
	 * Constructor que inicializa un nuevo registro de resultado técnico vacío.
	 */
	public ResultadoTecnico() {
		this.id = "";
		this.inspeccionId = "";
		this.descripcion = "";
		this.fecha = "";
		this.totalPlantasEvaluadas = 0;
		this.plantasAfectadas = 0;
		this.observaciones = "";
		this.cultivos = new ArrayList<>();
		this.plagas = new ArrayList<>();
		this.inspeccionFitosanitaria = null;
		this.informeFitosanitario = null;
	}
	public void setInspeccionId(String inspeccionId) {
		this.inspeccionId = inspeccionId;
	}
	public String getInspeccionId() {
		return this.inspeccionId;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcion() {
		return this.descripcion;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFecha() {
		return this.fecha;
	}

	/**
	 * Asigna el identificador único al resultado técnico.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Retorna el identificador actual del resultado.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Registra el número total de plantas observadas.
	 */
	public void setTotalPlantasEvaluadas(int pTotalPlantasEvaluadas) {
		this.totalPlantasEvaluadas = pTotalPlantasEvaluadas;
	}

	/**
	 * Devuelve la cantidad total de plantas evaluadas.
	 */
	public int getTotalPlantasEvaluadas() {
		return this.totalPlantasEvaluadas;
	}

	/**
	 * Define cuántas plantas presentaron afectaciones visibles.
	 */
	public void setPlantasAfectadas(Int pPlantasAfectadas) {
		if (pPlantasAfectadas != null) {
			this.plantasAfectadas = pPlantasAfectadas.getValor();
		}
	}

	/**
	 * Retorna el número de plantas afectadas registradas.
	 */
	public int getPlantasAfectadas() {
		return this.plantasAfectadas;
	}

	/**
	 * Permite ingresar comentarios y detalles sobre las condiciones del cultivo.
	 */
	public void setObservaciones(String pObservaciones) {
		this.observaciones = pObservaciones;
	}

	/**
	 * Devuelve las observaciones realizadas durante la inspección.
	 */
	public String getObservaciones() {
		return this.observaciones;
	}

	public void agregarCultivo(Cultivo pCultivo) {
		if (pCultivo != null && !this.cultivos.contains(pCultivo)) {
			this.cultivos.add(pCultivo);
		}
	}

	public Cultivo getCultivo() {
		return this.cultivos.isEmpty() ? null : this.cultivos.get(0);
	}

	public void agregarPlaga(Plaga pPlaga) {
		if (pPlaga != null && !this.plagas.contains(pPlaga)) {
			this.plagas.add(pPlaga);
		}
	}

	public Plaga getPlaga() {
		return this.plagas.isEmpty() ? null : this.plagas.get(0);
	}

	public void setInspeccionFitosanitaria(InspeccionFitosanitaria pInspeccionFitosanitaria) {
		this.inspeccionFitosanitaria = pInspeccionFitosanitaria;
	}

	public InspeccionFitosanitaria getInspeccionFitosanitaria() {
		return this.inspeccionFitosanitaria;
	}

	public void setInformeFitosanitario(InformeFitosanitario pInformeFitosanitario) {
		this.informeFitosanitario = pInformeFitosanitario;
	}

	public InformeFitosanitario getInformeFitosanitario() {
		return this.informeFitosanitario;
	}

}
