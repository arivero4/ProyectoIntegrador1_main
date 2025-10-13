package negocio.usuarios;

import model.Propietario;
import java.util.ArrayList;
import java.util.List;

import dao.PropietarioDAO;

public class GestorUsuarios {
    private PropietarioDAO propietarioDAO;

    public GestorUsuarios() {
        this.propietarioDAO = new PropietarioDAO();
    }

    public boolean registrarPropietario(Propietario propietario) {
        if (propietario == null || propietario.getId() == null || propietario.getId().isEmpty()) {
            return false;
        }
        return propietarioDAO.insertar(propietario);
    }

    public Propietario consultarPropietario(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.isEmpty()) {
            return null;
        }
        return propietarioDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
    }

    public boolean actualizarPropietario(Propietario propietario) {
        if (propietario == null || propietario.getId() == null || propietario.getId().isEmpty()) {
            return false;
        }
        return propietarioDAO.actualizar(propietario);
    }

    public List<Propietario> listarPropietarios() {
        return propietarioDAO.listar();
    }
}
