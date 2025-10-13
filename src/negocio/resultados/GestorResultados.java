
package negocio.resultados;

import model.ResultadoTecnico;
import java.util.ArrayList;
import java.util.List;

public class GestorResultados {
	private List<ResultadoTecnico> resultados;

	public GestorResultados() {
		this.resultados = new ArrayList<>();
	}

	public boolean registrarResultado(ResultadoTecnico resultado) {
		if (resultado == null || resultado.getId() == null || resultado.getId().isEmpty()) {
			return false;
		}
		for (ResultadoTecnico r : resultados) {
			if (r.getId().equals(resultado.getId())) {
				return false;
			}
		}
		resultados.add(resultado);
		return true;
	}

	public ResultadoTecnico consultarResultado(String id) {
		for (ResultadoTecnico r : resultados) {
			if (r.getId().equals(id)) {
				return r;
			}
		}
		return null;
	}

	public boolean actualizarResultado(ResultadoTecnico resultado) {
		if (resultado == null || resultado.getId() == null || resultado.getId().isEmpty()) {
			return false;
		}
		for (int i = 0; i < resultados.size(); i++) {
			if (resultados.get(i).getId().equals(resultado.getId())) {
				resultados.set(i, resultado);
				return true;
			}
		}
		return false;
	}

	public List<ResultadoTecnico> listarResultados() {
		return new ArrayList<>(resultados);
	}
}
