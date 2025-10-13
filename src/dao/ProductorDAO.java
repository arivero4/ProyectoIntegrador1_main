package dao;

import model.Productor;
import model.LugarProduccion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de productores en la base de datos.
 * Extiende UsuarioDAO para heredar funcionalidad común de usuarios.
 */
public class ProductorDAO extends UsuarioDAO {

    /**
     * Inserta un nuevo productor en la base de datos.
     */
    public boolean insertar(Productor productor) {
        PreparedStatement ps = null;
        try {
            // Primero insertar en la tabla base de usuarios
            if (!insertarUsuarioBase(productor)) {
                return false;
            }

            // Luego insertar en la tabla específica de productores
            String sql = "INSERT INTO productores (id) VALUES (?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, productor.getId());
            
            boolean resultado = ps.executeUpdate() > 0;
            
            // Insertar permisos si existen
            if (resultado && productor.getPermiso() != null && !productor.getPermiso().isEmpty()) {
                insertarPermisos(productor.getId(), productor.getPermiso());
            }
            
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un productor existente.
     */
    public boolean actualizar(Productor productor) {
        try {
            // Actualizar datos base del usuario
            boolean resultado = actualizarUsuarioBase(productor);
            
            // Actualizar permisos
            if (resultado && productor.getPermiso() != null) {
                actualizarPermisos(productor.getId(), productor.getPermiso());
            }
            
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un productor de la base de datos.
     */
    public boolean eliminar(String id) {
        PreparedStatement ps = null;
        try {
            // Primero eliminar de la tabla específica
            String sql = "DELETE FROM productores WHERE id = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
            
            // Eliminar permisos
            eliminarPermisos(id);
            
            // Luego eliminar de la tabla base
            return eliminarUsuarioBase(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Busca un productor por su ID.
     */
    public Productor buscarPorId(String id) {
        String sql = "SELECT u.* FROM usuarios u " +
                     "INNER JOIN productores p ON u.id = p.id " +
                     "WHERE u.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirProductor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un productor por su número de identificación.
     */
    public Productor buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT u.* FROM usuarios u " +
                     "INNER JOIN productores p ON u.id = p.id " +
                     "WHERE u.numero_identificacion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroIdentificacion);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirProductor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los productores registrados.
     */
    public List<Productor> listar() {
        List<Productor> lista = new ArrayList<>();
        String sql = "SELECT u.* FROM usuarios u " +
                     "INNER JOIN productores p ON u.id = p.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirProductor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los lugares de producción asociados a un productor.
     */
    public List<LugarProduccion> obtenerLugaresProduccion(String idProductor) {
        List<LugarProduccion> lugares = new ArrayList<>();
        String sql = "SELECT * FROM lugares_produccion WHERE id_productor = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idProductor);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                LugarProduccion lugar = new LugarProduccion();
                lugar.setId(rs.getString("id"));
                lugar.setCodigoIca(rs.getString("codigo_ica"));
                lugares.add(lugar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lugares;
    }

    /**
     * Construye un objeto Productor a partir de un ResultSet.
     */
    private Productor construirProductor(ResultSet rs) throws SQLException {
        Productor productor = new Productor();
        productor.setId(rs.getString("id"));
        productor.setRol(rs.getString("rol"));
        productor.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        productor.setNombre(rs.getString("nombre"));
        productor.setTelefonoContacto(rs.getString("telefono_contacto"));
        productor.setCorreoElectronico(rs.getString("correo_electronico"));
        
        // Cargar permisos
        List<String> permisos = obtenerPermisos(rs.getString("id"));
        productor.setPermiso(permisos);
        
        return productor;
    }
}
