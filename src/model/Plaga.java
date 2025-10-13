package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa los organismos o factores bióticos que pueden afectar negativamente los cultivos.
 * Permite registrar información técnica sobre cada plaga identificada durante las inspecciones fitosanitarias, contribuyendo al monitoreo y control sanitario del sistema agrícola.
 */
public class Plaga {

	/**
	 * Identificador único de la plaga dentro del sistema.
	 */
	private String id;

	/**
	 * Nombre común con el que se conoce la plaga (por ejemplo, "Gusano cogollero", "Pulgón verde").
	 */
	private String nombreComun;

	/**
	 * Denominación científica de la plaga, utilizada en reportes técnicos y estudios biológicos (por ejemplo, Spodoptera frugiperda).
	 */
	private String nombreCientifico;

	/**
	 * Detalles descriptivos de la plaga, como su morfología, ciclo biológico, síntomas en el cultivo o métodos de control.
	 */
	private String descripcion;

	private Cultivo cultivo;

	private List<ResultadoTecnico> resultadosTecnicos;

	/**
	 * Constructor por defecto. Crea una instancia vacía de la plaga.
	 */
	public Plaga() {
		this.id = "";
		this.nombreComun = "";
		this.nombreCientifico = "";
		this.descripcion = "";
		this.cultivo = null;
		this.resultadosTecnicos = new ArrayList<>();
	}

	/**
	 * Establece el identificador único de la plaga.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador actual.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Registra o modifica el nombre común de la plaga.
	 */
	public void setNombreComun(String pNombreComun) {
		this.nombreComun = pNombreComun;
	}

	/**
	 * Retorna el nombre común actual.
	 */
	public String getNombreComun() {
		return this.nombreComun;
	}

	/**
	 * Asigna o actualiza el nombre científico de la plaga.
	 */
	public void setNombreCientifico(String pNombreCientifico) {
		this.nombreCientifico = pNombreCientifico;
	}

	/**
	 * Devuelve el nombre científico registrado.
	 */
	public String getNombreCientifico() {
		return this.nombreCientifico;
	}

	/**
	 * Permite escribir o actualizar la descripción detallada de la plaga.
	 */
	public void setDescripcion(String pDescripcion) {
		this.descripcion = pDescripcion;
	}

	/**
	 * Devuelve la descripción almacenada.
	 */
	public String getDescriopcion() {
		return this.descripcion;
	}

	public void setCultivo(Cultivo pCultivo) {
		this.cultivo = pCultivo;
	}

	public Cultivo getCultivo() {
		return this.cultivo;
	}

	public void agregarResultadoTecnico(ResultadoTecnico pResultadoTecnico) {
		if (pResultadoTecnico != null && !this.resultadosTecnicos.contains(pResultadoTecnico)) {
			this.resultadosTecnicos.add(pResultadoTecnico);
		}
	}

	public ResultadoTecnico getResultadoTecnico() {
		return this.resultadosTecnicos.isEmpty() ? null : this.resultadosTecnicos.get(0);
	}

}
