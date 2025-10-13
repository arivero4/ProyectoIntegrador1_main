package negocio.usuarios;

import dao.PropietarioDAO;
import model.Propietario;
import java.util.List;
import java.util.ArrayList;

/**
 * Gestor de lógica de negocio para la entidad Propietario.
 * Maneja las operaciones CRUD y validaciones de negocio relacionadas con propietarios.
 */
public class GestorPropietarios {
    private PropietarioDAO propietarioDAO;
    
    /**
     * Constructor que inicializa el DAO de propietarios.
     */
    public GestorPropietarios() {
        this.propietarioDAO = new PropietarioDAO();
    }
    
    /**
     * Registra un nuevo propietario en el sistema.
     * Aplica validaciones de negocio antes de persistir.
     * 
     * @param propietario El propietario a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarPropietario(Propietario propietario) {
        // Validación 1: El propietario no puede ser nulo
        if (propietario == null) {
            System.err.println("Error: El propietario no puede ser nulo");
            return false;
        }
        
        // Validación 2: Número de identificación obligatorio
        if (propietario.getNumeroIdentificacion() == null || 
            propietario.getNumeroIdentificacion().trim().isEmpty()) {
            System.err.println("Error: El número de identificación es obligatorio");
            return false;
        }
        
        // Validación 3: Nombre obligatorio
        if (propietario.getNombre() == null || 
            propietario.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre es obligatorio");
            return false;
        }
        
        // Validación 4: Verificar unicidad del número de identificación
        Propietario existente = propietarioDAO.buscarPorNumeroIdentificacion(propietario.getNumeroIdentificacion());
        if (existente != null) {
            System.err.println("Error: Ya existe un propietario con el número de identificación: " + 
                             propietario.getNumeroIdentificacion());
            return false;
        }
        
        // Validación 5: Validar formato de email si está presente
        if (propietario.getCorreoElectronico() != null && !propietario.getCorreoElectronico().trim().isEmpty()) {
            if (!validarFormatoEmail(propietario.getCorreoElectronico())) {
                System.err.println("Error: El formato del email no es válido");
                return false;
            }
        }
        
        // Validación 6: Validar teléfono si está presente
        if (propietario.getTelefonoContacto() != null && !propietario.getTelefonoContacto().trim().isEmpty()) {
            if (!validarFormatoTelefono(propietario.getTelefonoContacto())) {
                System.err.println("Error: El formato del teléfono no es válido");
                return false;
            }
        }
        
        // Si todas las validaciones pasan, proceder con el registro
        return propietarioDAO.insertar(propietario);
    }
    
    /**
     * Consulta un propietario por su número de identificación.
     * 
     * @param numeroIdentificacion El número de identificación del propietario
     * @return El propietario encontrado o null si no existe
     */
    public Propietario consultarPropietario(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            System.err.println("Error: El número de identificación no puede estar vacío");
            return null;
        }
        return propietarioDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
    }
    
    /**
     * Actualiza los datos de un propietario existente.
     * 
     * @param propietario El propietario con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarPropietario(Propietario propietario) {
        // Validación 1: El propietario no puede ser nulo
        if (propietario == null) {
            System.err.println("Error: El propietario no puede ser nulo");
            return false;
        }
        
        // Validación 2: Verificar que el propietario existe
        Propietario existente = propietarioDAO.buscarPorNumeroIdentificacion(propietario.getNumeroIdentificacion());
        if (existente == null) {
            System.err.println("Error: No existe un propietario con el número de identificación: " + 
                             propietario.getNumeroIdentificacion());
            return false;
        }
        
        // Validación 3: Nombre obligatorio
        if (propietario.getNombre() == null || 
            propietario.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre es obligatorio");
            return false;
        }
        
        // Validación 4: Validar formato de email si está presente
        if (propietario.getCorreoElectronico() != null && !propietario.getCorreoElectronico().trim().isEmpty()) {
            if (!validarFormatoEmail(propietario.getCorreoElectronico())) {
                System.err.println("Error: El formato del email no es válido");
                return false;
            }
        }
        
        // Validación 5: Validar teléfono si está presente
        if (propietario.getTelefonoContacto() != null && !propietario.getTelefonoContacto().trim().isEmpty()) {
            if (!validarFormatoTelefono(propietario.getTelefonoContacto())) {
                System.err.println("Error: El formato del teléfono no es válido");
                return false;
            }
        }
        
        return propietarioDAO.actualizar(propietario);
    }
    
    /**
     * Elimina un propietario del sistema.
     * 
     * @param numeroIdentificacion El número de identificación del propietario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarPropietario(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            System.err.println("Error: El número de identificación no puede estar vacío");
            return false;
        }
        
        // Verificar que el propietario existe
        Propietario existente = propietarioDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
        if (existente == null) {
            System.err.println("Error: No existe un propietario con el número de identificación: " + 
                             numeroIdentificacion);
            return false;
        }
        
        // TODO: Verificar que no tenga predios asociados antes de eliminar
        
        return propietarioDAO.eliminar(numeroIdentificacion);
    }
    
    /**
     * Lista todos los propietarios registrados en el sistema.
     * 
     * @return Lista de propietarios
     */
    public List<Propietario> listarPropietarios() {
        return propietarioDAO.listar();
    }
    
    /**
     * Busca propietarios por nombre (búsqueda parcial).
     * 
     * @param nombre El nombre o parte del nombre a buscar
     * @return Lista de propietarios que coinciden con el criterio
     */
    public List<Propietario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return listarPropietarios();
        }
        // Filtrar localmente por nombre
        List<Propietario> todos = listarPropietarios();
        List<Propietario> resultado = new ArrayList<>();
        String nombreBusqueda = nombre.toLowerCase();
        
        for (Propietario p : todos) {
            if (p.getNombre() != null && p.getNombre().toLowerCase().contains(nombreBusqueda)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene los predios asociados a un propietario.
     * 
     * @param idPropietario El ID del propietario
     * @return Lista de predios del propietario
     */
    public List<model.Predio> obtenerPrediosPropietario(String idPropietario) {
        if (idPropietario == null || idPropietario.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return propietarioDAO.obtenerPredios(idPropietario);
    }
    
    /**
     * Valida el formato de un email.
     * 
     * @param email El email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean validarFormatoEmail(String email) {
        // Expresión regular simple para validar email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
    
    /**
     * Valida el formato de un teléfono.
     * 
     * @param telefono El teléfono a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean validarFormatoTelefono(String telefono) {
        // Validar que solo contenga números, espacios, guiones y paréntesis
        // y que tenga entre 7 y 15 dígitos
        String regex = "^[0-9\\s\\-\\(\\)]{7,15}$";
        return telefono.matches(regex);
    }
}
