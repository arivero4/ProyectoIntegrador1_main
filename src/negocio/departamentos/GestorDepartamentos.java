package negocio.departamentos;

import dao.DepartamentoDAO;
import model.Departamento;
import java.util.List;

public class GestorDepartamentos {
    private DepartamentoDAO dao;

    public GestorDepartamentos() {
        dao = new DepartamentoDAO();
    }

    public void registrarDepartamento(Departamento d) {
        if (d.getId() == null || d.getNombre() == null) {
            throw new IllegalArgumentException("El departamento debe tener un ID y nombre.");
        }
        dao.insertar(d);
    }

    public List<Departamento> listarDepartamentos() {
        return dao.listar();
    }
}
