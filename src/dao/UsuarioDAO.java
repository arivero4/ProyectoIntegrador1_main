package dao;

import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO base para la gestión de usuarios en la base de datos.
 * Proporciona operaciones CRUD comunes para todos los tipos de usuario.
 */
public abstract class UsuarioDAO extends GenericDAO {

    /**
     * Inserta los datos comunes de un usuario en la tabla usuarios.
     * Este método debe ser llamado por las clases hijas antes de insertar datos específicos.
     */
    protected boolean insertarUsuarioBase(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, rol, numero_identificacion, nombre, telefono_contacto, correo_electronico) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getId());
            ps.setString(2, usuario.getRol());
            ps.setString(3, usuario.getNumeroIdentificacion());
            ps.setString(4, usuario.getNombre());
            ps.setString(5, usuario.getTelefonoContacto());
            ps.setString(6, usuario.getCorreoElectronico());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos comunes de un usuario.
     */
    protected boolean actualizarUsuarioBase(Usuario usuario) {
        String sql = "UPDATE usuarios SET rol = ?, nombre = ?, telefono_contacto = ?, " +
                     "correo_electronico = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getRol());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getTelefonoContacto());
            ps.setString(4, usuario.getCorreoElectronico());
            ps.setString(5, usuario.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un usuario de la tabla base.
     */
    protected boolean eliminarUsuarioBase(String id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Busca un usuario por su número de identificación.
     */
    protected ResultSet buscarUsuarioBasePorIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT * FROM usuarios WHERE numero_identificacion = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroIdentificacion);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un usuario por su ID.
     */
    protected ResultSet buscarUsuarioBasePorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inserta permisos de un usuario en la tabla de permisos.
     */
    protected void insertarPermisos(String idUsuario, List<String> permisos) {
        String sql = "INSERT INTO permisos_usuario (id_usuario, permiso) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            for (String permiso : permisos) {
                ps.setString(1, idUsuario);
                ps.setString(2, permiso);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Obtiene los permisos de un usuario.
     */
    protected List<String> obtenerPermisos(String idUsuario) {
        List<String> permisos = new ArrayList<>();
        String sql = "SELECT permiso FROM permisos_usuario WHERE id_usuario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                permisos.add(rs.getString("permiso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return permisos;
    }

    /**
     * Actualiza los permisos de un usuario.
     */
    protected void actualizarPermisos(String idUsuario, List<String> permisos) {
        // Primero eliminar permisos existentes
        eliminarPermisos(idUsuario);
        // Luego insertar los nuevos
        insertarPermisos(idUsuario, permisos);
    }

    /**
     * Elimina todos los permisos de un usuario.
     */
    protected void eliminarPermisos(String idUsuario) {
        String sql = "DELETE FROM permisos_usuario WHERE id_usuario = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, null);
        }
    }
}
