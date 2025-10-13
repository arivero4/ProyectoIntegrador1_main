package negocio.usuarios;

import dao.ProductorDAO;
import model.Productor;
import model.LugarProduccion;
import java.util.List;
import java.util.ArrayList;

/**
 * Gestor de lógica de negocio para la entidad Productor.
 * Maneja las operaciones CRUD y validaciones de negocio relacionadas con productores.
 */
public class GestorProductores {
    private ProductorDAO productorDAO;
    
    /**
     * Constructor que inicializa el DAO de productores.
     */
    public GestorProductores() {
        this.productorDAO = new ProductorDAO();
    }
    
    /**
     * Registra un nuevo productor en el sistema.
     * Aplica validaciones de negocio antes de persistir.
     * 
     * @param productor El productor a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarProductor(Productor productor) {
        // Validación 1: El productor no puede ser nulo
        if (productor == null) {
            System.err.println("Error: El productor no puede ser nulo");
            return false;
        }
        
        // Validación 2: Número de identificación obligatorio
        if (productor.getNumeroIdentificacion() == null || 
            productor.getNumeroIdentificacion().trim().isEmpty()) {
            System.err.println("Error: El número de identificación es obligatorio");
            return false;
        }
        
        // Validación 3: Nombre obligatorio
        if (productor.getNombre() == null || 
            productor.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre es obligatorio");
            return false;
        }
        
        // Validación 4: Verificar unicidad del número de identificación
        Productor existente = productorDAO.buscarPorNumeroIdentificacion(productor.getNumeroIdentificacion());
        if (existente != null) {
            System.err.println("Error: Ya existe un productor con el número de identificación: " + 
                             productor.getNumeroIdentificacion());
            return false;
        }
        
        // Validación 5: Validar formato de email si está presente
        if (productor.getCorreoElectronico() != null && !productor.getCorreoElectronico().trim().isEmpty()) {
            if (!validarFormatoEmail(productor.getCorreoElectronico())) {
                System.err.println("Error: El formato del email no es válido");
                return false;
            }
        }
        
        // Validación 6: Validar teléfono si está presente
        if (productor.getTelefonoContacto() != null && !productor.getTelefonoContacto().trim().isEmpty()) {
            if (!validarFormatoTelefono(productor.getTelefonoContacto())) {
                System.err.println("Error: El formato del teléfono no es válido");
                return false;
            }
        }
        
        // Si todas las validaciones pasan, proceder con el registro
        return productorDAO.insertar(productor);
    }
    
    /**
     * Consulta un productor por su número de identificación.
     * 
     * @param numeroIdentificacion El número de identificación del productor
     * @return El productor encontrado o null si no existe
     */
    public Productor consultarProductor(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            System.err.println("Error: El número de identificación no puede estar vacío");
            return null;
        }
        return productorDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
    }
    
    /**
     * Busca un productor por su ID.
     * 
     * @param id El ID del productor
     * @return El productor encontrado o null si no existe
     */
    public Productor buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.err.println("Error: El ID no puede estar vacío");
            return null;
        }
        return productorDAO.buscarPorId(id);
    }
    
    /**
     * Actualiza los datos de un productor existente.
     * 
     * @param productor El productor con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarProductor(Productor productor) {
        // Validación 1: El productor no puede ser nulo
        if (productor == null) {
            System.err.println("Error: El productor no puede ser nulo");
            return false;
        }
        
        // Validación 2: Verificar que el productor existe
        Productor existente = productorDAO.buscarPorNumeroIdentificacion(productor.getNumeroIdentificacion());
        if (existente == null) {
            System.err.println("Error: No existe un productor con el número de identificación: " + 
                             productor.getNumeroIdentificacion());
            return false;
        }
        
        // Validación 3: Nombre obligatorio
        if (productor.getNombre() == null || 
            productor.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre es obligatorio");
            return false;
        }
        
        // Validación 4: Validar formato de email si está presente
        if (productor.getCorreoElectronico() != null && !productor.getCorreoElectronico().trim().isEmpty()) {
            if (!validarFormatoEmail(productor.getCorreoElectronico())) {
                System.err.println("Error: El formato del email no es válido");
                return false;
            }
        }
        
        // Validación 5: Validar teléfono si está presente
        if (productor.getTelefonoContacto() != null && !productor.getTelefonoContacto().trim().isEmpty()) {
            if (!validarFormatoTelefono(productor.getTelefonoContacto())) {
                System.err.println("Error: El formato del teléfono no es válido");
                return false;
            }
        }
        
        return productorDAO.actualizar(productor);
    }
    
    /**
     * Elimina un productor del sistema.
     * 
     * @param numeroIdentificacion El número de identificación del productor a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarProductor(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            System.err.println("Error: El número de identificación no puede estar vacío");
            return false;
        }
        
        // Verificar que el productor existe
        Productor existente = productorDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
        if (existente == null) {
            System.err.println("Error: No existe un productor con el número de identificación: " + 
                             numeroIdentificacion);
            return false;
        }
        
        // TODO: Verificar que no tenga lugares de producción asociados antes de eliminar
        
        return productorDAO.eliminar(existente.getId());
    }
    
    /**
     * Lista todos los productores registrados en el sistema.
     * 
     * @return Lista de productores
     */
    public List<Productor> listarProductores() {
        return productorDAO.listar();
    }
    
    /**
     * Busca productores por nombre (búsqueda parcial).
     * 
     * @param nombre El nombre o parte del nombre a buscar
     * @return Lista de productores que coinciden con el criterio
     */
    public List<Productor> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return listarProductores();
        }
        // Filtrar localmente por nombre
        List<Productor> todos = listarProductores();
        List<Productor> resultado = new ArrayList<>();
        String nombreBusqueda = nombre.toLowerCase();
        
        for (Productor p : todos) {
            if (p.getNombre() != null && p.getNombre().toLowerCase().contains(nombreBusqueda)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene los lugares de producción asociados a un productor.
     * 
     * @param idProductor El ID del productor
     * @return Lista de lugares de producción del productor
     */
    public List<LugarProduccion> obtenerLugaresProduccion(String idProductor) {
        if (idProductor == null || idProductor.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return productorDAO.obtenerLugaresProduccion(idProductor);
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
