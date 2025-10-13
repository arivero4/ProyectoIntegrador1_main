package model;

/**
 * representa las notificaciones o advertencias generadas automáticamente a partir de los resultados de un informe fitosanitario.
 * Se utiliza para informar a los productores, técnicos o autoridades competentes sobre la presencia de condiciones críticas que requieren atención inmediata, como altos niveles de incidencia o brotes de plagas peligrosas.
 */
public class Alerta {

	/**
	 * Identificador único de la alerta dentro del sistema.
	 */
	private String id;

	/**
	 * Indica la gravedad o categoría de la alerta (por ejemplo, "Bajo", "Medio", "Alto"). Este valor puede definirse según los umbrales establecidos por la autoridad fitosanitaria.
	 */
	private String nivelRiesgo;

	/**
	 * Informe fitosanitario asociado a esta alerta.
	 */
	private InformeFitosanitario informeFitosanitario;

	/**
	 * Constructor que inicializa una nueva alerta vacía.
	 */
	public Alerta() {
		this.id = "";
		this.nivelRiesgo = "";
		this.informeFitosanitario = null;
	}

	/**
	 * Asigna el identificador único a la alerta.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador de la alerta actual.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Define el nivel de riesgo asociado a la alerta. Este valor se determina en función del análisis del informe técnico.
	 */
	public void setNivelRiesgo(String pNivelRiesgo) {
		this.nivelRiesgo = pNivelRiesgo;
	}

	/**
	 * Retorna el nivel de riesgo registrado.
	 */
	public String getNivelRiesgo() {
		return this.nivelRiesgo;
	}

	public InformeFitosanitario getInformeFitosanitario() {
		return this.informeFitosanitario;
	}

	public void agregarInformeFitosanitario(InformeFitosanitario pInformeFitosanitario) {
		this.informeFitosanitario = pInformeFitosanitario;
	}

}
