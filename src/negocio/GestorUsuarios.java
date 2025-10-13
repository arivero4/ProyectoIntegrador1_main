package negocio;

import dao.PropietarioDAO;
import model.Propietario;
import java.util.List;

public class GestorUsuarios {
    private PropietarioDAO propietarioDAO;
    
    public GestorUsuarios() {
        this.propietarioDAO = new PropietarioDAO();
    }
    
    public boolean registrarPropietario(Propietario propietario) {
        // Validaciones de negocio
        if (propietario == null) {
            return false;
        }
        
        if (propietario.getNumeroIdentificacion() == null || 
            propietario.getNumeroIdentificacion().trim().isEmpty()) {
            return false;
        }
        
        if (propietario.getNombresCompletos() == null || 
            propietario.getNombresCompletos().trim().isEmpty()) {
            return false;
        }
        
        // Verificar si ya existe
        Propietario existente = propietarioDAO.consultar(propietario.getNumeroIdentificacion());
        if (existente != null) {
            return false; // Ya existe un propietario con esa identificación
        }
        
        return propietarioDAO.insertar(propietario);
    }
    
    public Propietario consultarPropietario(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            return null;
        }
        return propietarioDAO.consultar(numeroIdentificacion);
    }
    
    public boolean actualizarPropietario(Propietario propietario) {
        if (propietario == null) {
            return false;
        }
        
        // Verificar que existe
        Propietario existente = propietarioDAO.consultar(propietario.getNumeroIdentificacion());
        if (existente == null) {
            return false;
        }
        
        return propietarioDAO.actualizar(propietario);
    }
    
    public List<Propietario> listarPropietarios() {
        return propietarioDAO.listarTodos();
    }
}
