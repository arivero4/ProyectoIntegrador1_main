package model;

import java.util.ArrayList;
import java.util.List;

/**
 * representa la información relacionada con una plantación agrícola específica que se desarrolla dentro de un lote.
 * Incluye detalles sobre la especie vegetal, la variedad sembrada y una descripción general del cultivo. Esta clase es clave para llevar un control de los cultivos activos, sus características botánicas y su relación con los lotes o inspecciones fitosanitarias.
 */
public class Cultivo {

	/**
	 * Identificador único del cultivo dentro del sistema. Permite distinguir entre diferentes cultivos y mantener trazabilidad.
	 */
	private String id;

	/**
	 * Nombre de la variedad cultivada (por ejemplo, "Maíz Amarillo" o "Café Caturra"). Ayuda a diferenciar tipos de plantas dentro de la misma especie.
	 */
	private String nombreVariedad;

	/**
	 * Nombre común del cultivo, como "Maíz", "Café", "Arroz", etc.
	 */
	private String nombreCultivo;

	/**
	 * Nombre científico o botánico de la especie vegetal (por ejemplo, Zea mays o Coffea arabica). Este atributo es importante para clasificaciones técnicas o científicas.
	 */
	private String especieVegetal;

	/**
	 * Texto que describe las características generales del cultivo, tales como el ciclo productivo, condiciones ideales de siembra, clima o tipo de suelo.
	 */
	private String descripcion;

	private List<Lote> lotes;

	private Plaga plaga;

	private List<ResultadoTecnico> resultadosTecnicos;

	/**
	 * Constructor por defecto que inicializa un cultivo vacío, permitiendo asignar valores posteriormente.
	 */
	public Cultivo() {
		this.id = "";
		this.nombreVariedad = "";
		this.nombreCultivo = "";
		this.especieVegetal = "";
		this.descripcion = "";
		this.lotes = new ArrayList<>();
		this.plaga = null;
		this.resultadosTecnicos = new ArrayList<>();
	}

	/**
	 * Asigna el identificador único al cultivo, garantizando su individualidad dentro del sistema.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador actual del cultivo, útil en búsquedas y relaciones con inspecciones.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Permite registrar o modificar el nombre de la variedad del cultivo.
	 */
	public void setNombreVariedad(String pNombreVariedad) {
		this.nombreVariedad = pNombreVariedad;
	}

	/**
	 * Retorna el nombre actual de la variedad sembrada.
	 */
	public String getNombreVariedad() {
		return this.nombreVariedad;
	}

	/**
	 * signa el nombre común del cultivo, facilitando su identificación en los reportes o registros.
	 */
	public void setNombreCultivo(String pNombreCultivo) {
		this.nombreCultivo = pNombreCultivo;
	}

	/**
	 * Devuelve el nombre del cultivo.
	 */
	public String getNombreCultivo() {
		return this.nombreCultivo;
	}

	/**
	 * Permite registrar el nombre científico de la especie vegetal, garantizando precisión técnica.
	 */
	public void setEspecieVegetal(String pEspecieVegetal) {
		this.especieVegetal = pEspecieVegetal;
	}

	/**
	 * Devuelve el nombre científico de la especie asociada.
	 */
	public String getEspecieVegetal() {
		return this.especieVegetal;
	}

	/**
	 * Define o actualiza la descripción general del cultivo.
	 */
	public void setDescripcion(String pDescripcion) {
		this.descripcion = pDescripcion;
	}

	/**
	 * Retorna la descripción actual registrada del cultivo.
	 */
	public String getDescripcion() {
		return this.descripcion;
	}

	public void agregarLote(Lote pLote) {
		if (pLote != null && !this.lotes.contains(pLote)) {
			this.lotes.add(pLote);
		}
	}

	public Lote getLote() {
		return this.lotes.isEmpty() ? null : this.lotes.get(0);
	}

	public void setPlaga(Plaga pPlaga) {
		this.plaga = pPlaga;
	}

	public Plaga getPlaga() {
		return this.plaga;
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
