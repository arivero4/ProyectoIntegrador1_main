
package negocio.predios;

import dao.PredioDAO;
import model.Predio;
import java.util.List;

/**
 * Gestor de lógica de negocio para predios. Usa PredioDAO para persistencia en BD.
 */
public class GestorPredios {
	private PredioDAO predioDAO;

	public GestorPredios() {
		this.predioDAO = new PredioDAO();
	}

	public boolean registrarPredio(Predio predio) {
		if (predio == null || predio.getId() == null || predio.getId().isEmpty()) {
			return false;
		}
		try {
			System.out.println("[DEBUG] GestorPredios.registrarPredio id=" + predio.getId());
			return predioDAO.insertar(predio);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Predio consultarPredio(String id) {
		if (id == null || id.trim().isEmpty()) return null;
		return predioDAO.buscarPorId(id);
	}

	public boolean actualizarPredio(Predio predio) {
		if (predio == null || predio.getId() == null || predio.getId().isEmpty()) return false;
		try {
			return predioDAO.actualizar(predio);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Predio> listarPredios() {
		return predioDAO.listar();
	}
}
