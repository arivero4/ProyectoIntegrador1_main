package model;

import java.util.ArrayList;
import java.util.List;

/**
 * epresenta un sitio geográfico o administrativo donde se desarrollan actividades agrícolas. Está compuesta por uno o varios lotes, y cada lugar tiene un código único que permite su identificación dentro del sistema.
 */
public class LugarProduccion {

	/**
	 * Identificador único del lugar de producción, utilizado para referenciarlo en las relaciones con productores o lotes.
	 */
	private String id;

	/**
	 * Código interno o institucional que identifica el lugar de producción según normativas locales o registros oficiales (por ejemplo, un código ICA).
	 */
	private String codigoIca;

	private Predio predio;

	private Productor productor;

	private AsistenteTecnico asistenteTecnico;

	private List<Lote> lotes;

	/**
	 * onstructor que inicializa un objeto vacío de tipo LugarProduccion.
	 */
	public LugarProduccion() {
		this.id = "";
		this.codigoIca = "";
		this.predio = null;
		this.productor = null;
		this.asistenteTecnico = null;
		this.lotes = new ArrayList<>();
	}

	/**
	 * Asigna el identificador único del lugar de producción.
	 */
	public void setId(String pId) {
		this.id = pId;
	}

	/**
	 * Devuelve el identificador actual del lugar.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Establece el código LCA (código local del área). Este código es clave en los registros y trazabilidad de la producción.
	 */
	public void setCodigoIca(String pCodigoIca) {
		this.codigoIca = pCodigoIca;
	}

	/**
	 * Retorna el código LCA asignado, útil para validaciones o reportes de registro.
	 */
	public String getCodigoIca() {
		return this.codigoIca;
	}

	public void setPredio(Predio pPredio) {
		this.predio = pPredio;
	}

	public Predio getPredio() {
		return this.predio;
	}

	public void setProductor(Productor pProductor) {
		this.productor = pProductor;
	}

	public Productor getProductor() {
		return this.productor;
	}

	public void setAsistenteTecnico(AsistenteTecnico pAsistenteTecnico) {
		this.asistenteTecnico = pAsistenteTecnico;
	}

	public AsistenteTecnico getAsistenteTecnico() {
		return this.asistenteTecnico;
	}

	public void agregarLote(Lote pLote) {
		if (pLote != null && !this.lotes.contains(pLote)) {
			this.lotes.add(pLote);
		}
	}

	public Lote setLote() {
		return this.lotes.isEmpty() ? null : this.lotes.get(0);
	}

}
