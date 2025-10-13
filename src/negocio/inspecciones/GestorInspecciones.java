
package negocio.inspecciones;

import model.InspeccionFitosanitaria;
import java.util.ArrayList;
import java.util.List;

public class GestorInspecciones {
	private List<InspeccionFitosanitaria> inspecciones;

	public GestorInspecciones() {
		this.inspecciones = new ArrayList<>();
	}

	public boolean registrarInspeccion(InspeccionFitosanitaria inspeccion) {
		if (inspeccion == null || inspeccion.getId() == null || inspeccion.getId().isEmpty()) {
			return false;
		}
		for (InspeccionFitosanitaria i : inspecciones) {
			if (i.getId().equals(inspeccion.getId())) {
				return false;
			}
		}
		inspecciones.add(inspeccion);
		return true;
	}

	public InspeccionFitosanitaria consultarInspeccion(String id) {
		for (InspeccionFitosanitaria i : inspecciones) {
			if (i.getId().equals(id)) {
				return i;
			}
		}
		return null;
	}

	public boolean actualizarInspeccion(InspeccionFitosanitaria inspeccion) {
		if (inspeccion == null || inspeccion.getId() == null || inspeccion.getId().isEmpty()) {
			return false;
		}
		for (int i = 0; i < inspecciones.size(); i++) {
			if (inspecciones.get(i).getId().equals(inspeccion.getId())) {
				inspecciones.set(i, inspeccion);
				return true;
			}
		}
		return false;
	}

	public List<InspeccionFitosanitaria> listarInspecciones() {
		return new ArrayList<>(inspecciones);
	}
}
