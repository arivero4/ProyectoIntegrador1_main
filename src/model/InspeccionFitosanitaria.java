package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa las evaluaciones técnicas realizadas sobre los cultivos o lotes con el fin de identificar la presencia de plagas, enfermedades o condiciones que afecten la salud vegetal.
 */
public class InspeccionFitosanitaria {
	private String predioId;
	private String tecnico;
	private String observaciones;

	/**
	 * Identificador único de la inspección fitosanitaria.
	 */
	private String id;

	/**
	 * Código del lugar o área de producción donde se realiza la inspección. Este código permite asociar la inspección a un registro oficial o local.
	 */
	private String codigoIca;

	/**
	 * echa en la que se llevó a cabo la inspección, generalmente almacenada en formato estándar (por ejemplo, "2025-10-11").
	 */
	private String fechaInspeccion;

	private List<Lote> lotes;

	private List<ResultadoTecnico> resultadosTecnicos;

	private AsistenteTecnico asistenteTecnico;

	/**
	 * Constructor que crea una nueva inspección sin valores iniciales, lista para ser completada.
	 */
	public InspeccionFitosanitaria() {
		this.id = "";
		this.codigoIca = "";
		this.fechaInspeccion = "";
		this.predioId = "";
		this.tecnico = "";
		this.observaciones = "";
		this.lotes = new ArrayList<>();
		this.resultadosTecnicos = new ArrayList<>();
		this.asistenteTecnico = null;
	}
	public void setFecha(String fecha) {
		this.fechaInspeccion = fecha;
	}
	public String getFecha() {
		return this.fechaInspeccion;
	}
	public void setPredioId(String predioId) {
		this.predioId = predioId;
	}
	public String getPredioId() {
		return this.predioId;
	}
	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}
	public String getTecnico() {
		return this.tecnico;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getObservaciones() {
		return this.observaciones;
	}

	/**
	 * Asigna un identificador único a la inspección.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador actual de la inspección.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Establece el código LCA correspondiente al lugar de producción inspeccionado.
	 */
	public void setCodigoIca(String pCodigoIca) {
		this.codigoIca = pCodigoIca;
	}

	/**
	 * Retorna el código LCA de la inspección.
	 */
	public String getCodigoIca() {
		return this.codigoIca;
	}

	/**
	 * Registra o actualiza la fecha en que se realizó la inspección, garantizando la trazabilidad temporal de los datos.
	 */
	public void setFechaInspeccion(String pFechaInspeccion) {
		this.fechaInspeccion = pFechaInspeccion;
	}

	public String getFechaInspeccion() {
		return this.fechaInspeccion;
	}

	public void agregarLote(Lote pLote) {
		if (pLote != null && !this.lotes.contains(pLote)) {
			this.lotes.add(pLote);
		}
	}

	public Lote getLote() {
		return this.lotes.isEmpty() ? null : this.lotes.get(0);
	}

	public void agregarResultadoTecnico(ResultadoTecnico pResultadoTecnico) {
		if (pResultadoTecnico != null && !this.resultadosTecnicos.contains(pResultadoTecnico)) {
			this.resultadosTecnicos.add(pResultadoTecnico);
		}
	}

	public ResultadoTecnico getResultadoTecnico() {
		return this.resultadosTecnicos.isEmpty() ? null : this.resultadosTecnicos.get(0);
	}

	public void setAsistenteTecnico(AsistenteTecnico pAsistenteTecnico) {
		this.asistenteTecnico = pAsistenteTecnico;
	}

	public AsistenteTecnico getAsistenteTecnico() {
		return this.asistenteTecnico;
	}

}
