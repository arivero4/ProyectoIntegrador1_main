package model;

import java.util.ArrayList;
import java.util.List;

/**
 * consolida los resultados de las inspecciones fitosanitarias y resume el estado sanitario de los cultivos inspeccionados.
 * Este informe es elaborado a partir de los resultados técnicos obtenidos en campo y contiene información clave como la fecha del informe, los niveles de incidencia detectados y observaciones generales del estado fitosanitario.
 */
public class InformeFitosanitario {

	/**
	 * Identificador único del informe dentro del sistema.
	 */
	private String id;

	/**
	 * Código del lugar de producción o área inspeccionada, usado para relacionar el informe con un sitio específico.
	 */
	private String codigoIca;

	/**
	 * Número total de plantas revisadas según los resultados consolidados de campo.
	 */
	private int totalPlantasEvaluadas;

	/**
	 * Porcentaje de afectación calculado como el número de plantas afectadas dividido entre el total de plantas evaluadas. Este indicador permite medir la gravedad del problema fitosanitario.
	 */
	private double nivelIncidencia;

	/**
	 * Resumen general con observaciones del inspector o analista sobre el estado del cultivo o hallazgos relevantes.
	 */
	private String observaciones;

	/**
	 * Fecha de elaboración del informe, generalmente en formato "AAAA-MM-DD".
	 */
	private String fechaInforme;

	private List<ResultadoTecnico> resultadosTecnicos;

	private List<Alerta> alertas;

	/**
	 * Constructor que crea un nuevo informe sin datos iniciales.
	 */
	public InformeFitosanitario() {
		this.id = "";
		this.codigoIca = "";
		this.totalPlantasEvaluadas = 0;
		this.nivelIncidencia = 0.0;
		this.observaciones = "";
		this.fechaInforme = "";
		this.resultadosTecnicos = new ArrayList<>();
		this.alertas = new ArrayList<>();
	}

	/**
	 * Asigna el identificador del informe.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador del informe.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Registra el código del lugar donde se realizó la inspección.
	 */
	public void setCodigoIca(String pCodigoIca) {
		this.codigoIca = pCodigoIca;
	}

	/**
	 * Retorna el código LCA asociado al informe.
	 */
	public String getCodigoIca() {
		return this.codigoIca;
	}

	/**
	 * Define la cantidad total de plantas evaluadas en el informe.
	 */
	public void setToltalPlantasEvaluadas(int pTotalPlantasEvaluadas) {
		this.totalPlantasEvaluadas = pTotalPlantasEvaluadas;
	}

	/**
	 * Devuelve el número total de plantas evaluadas.
	 */
	public int getTotalPlantasEvaluadas() {
		return this.totalPlantasEvaluadas;
	}

	/**
	 * Registra el porcentaje o nivel de incidencia detectado.
	 */
	public void setNivelIncidencia(double pNivelIncidencia) {
		this.nivelIncidencia = pNivelIncidencia;
	}

	/**
	 * Devuelve el valor actual del nivel de incidencia.
	 */
	public double getNivelIncidencia() {
		return this.nivelIncidencia;
	}

	/**
	 * Permite escribir comentarios o conclusiones generales del informe.
	 */
	public void setObservaciones(String pObservaciones) {
		this.observaciones = pObservaciones;
	}

	/**
	 * Retorna las observaciones escritas.
	 */
	public String getObservaciones() {
		return this.observaciones;
	}

	/**
	 * Establece la fecha oficial de elaboración del informe.
	 */
	public void setFechaInforme(String pFechaInforme) {
		this.fechaInforme = pFechaInforme;
	}

	/**
	 * Devuelve la fecha del informe.
	 */
	public String getFechaInforme() {
		return this.fechaInforme;
	}

	public void agregarResultadoTecnico(ResultadoTecnico pResultadoTecnico) {
		if (pResultadoTecnico != null && !this.resultadosTecnicos.contains(pResultadoTecnico)) {
			this.resultadosTecnicos.add(pResultadoTecnico);
		}
	}

	public ResultadoTecnico getResultadoTecnico() {
		return this.resultadosTecnicos.isEmpty() ? null : this.resultadosTecnicos.get(0);
	}

	public void agregarAlerta(Alerta pAlerta) {
		if (pAlerta != null && !this.alertas.contains(pAlerta)) {
			this.alertas.add(pAlerta);
		}
	}

	public Alerta getAlerta() {
		return this.alertas.isEmpty() ? null : this.alertas.get(0);
	}

}
