package dao;

import model.Propietario;
import model.Predio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de propietarios en la base de datos.
 * Extiende UsuarioDAO para heredar funcionalidad común de usuarios.
 */
public class PropietarioDAO extends UsuarioDAO {

    /**
     * Inserta un nuevo propietario en la base de datos.
     */
    public boolean insertar(Propietario propietario) {
        PreparedStatement ps = null;
        try {
            // 1. Insertar en la tabla usuarios
            System.out.println("[DEBUG] insertar: intentando insertar usuario base. id=" + propietario.getId() + ", numeroIdentificacion=" + propietario.getNumeroIdentificacion());
            boolean usuarioInsertado = insertarUsuarioBase(propietario);
            if (!usuarioInsertado) {
                System.out.println("[DEBUG] insertar: fallo al insertar en usuarios (insertarUsuarioBase devolvió false)");
                return false;
            }

            // 2. Insertar en la tabla propietario
            String sql = "INSERT INTO propietario (id, nombre, rol, direccion, correo_electronico) VALUES (?, ?, ?, ?, ?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, propietario.getId());
            ps.setString(2, propietario.getNombre());
            ps.setString(3, propietario.getRol());
            ps.setString(4, propietario.getDireccion());
            ps.setString(5, propietario.getCorreoElectronico());
            System.out.println("[DEBUG] insertar: ejecutando SQL de propietario: " + sql);
            System.out.println("[DEBUG] insertar: params -> id=" + propietario.getId() + ", nombre=" + propietario.getNombre() + ", rol=" + propietario.getRol() + ", direccion=" + propietario.getDireccion() + ", correo=" + propietario.getCorreoElectronico());
            int updated = ps.executeUpdate();
            System.out.println("[DEBUG] insertar: executeUpdate returned " + updated);
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("[DEBUG] insertar: SQLException durante insercion");
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un propietario existente.
     */
    public boolean actualizar(Propietario propietario) {
        try {
            // Actualizar datos base del usuario
            boolean resultado = actualizarUsuarioBase(propietario);
            
            // Actualizar permisos
            if (resultado && propietario.getPermiso() != null) {
                actualizarPermisos(propietario.getId(), propietario.getPermiso());
            }
            
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un propietario de la base de datos.
     */
    public boolean eliminar(String id) {
        PreparedStatement ps = null;
        try {
            // Primero eliminar de la tabla específica
            String sql = "DELETE FROM propietario WHERE id = ?";
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
     * Busca un propietario por su ID.
     */
    public Propietario buscarPorId(String id) {
        String sql = "SELECT u.*, p.direccion FROM usuarios u " +
                     "INNER JOIN propietario p ON u.id = p.id " +
                     "WHERE u.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPropietario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un propietario por su número de identificación.
     */
    public Propietario buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT u.*, p.direccion FROM usuarios u " +
                     "INNER JOIN propietario p ON u.id = p.id " +
                     "WHERE u.numero_identificacion = ?";
        System.out.println("[DEBUG] buscarPorNumeroIdentificacion: SQL=" + sql + ", param=" + numeroIdentificacion);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroIdentificacion);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPropietario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los propietarios registrados.
     */
    public List<Propietario> listar() {
        List<Propietario> lista = new ArrayList<>();
        String sql = "SELECT u.*, p.direccion FROM usuarios u " +
                     "INNER JOIN propietario p ON u.id = p.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPropietario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los predios asociados a un propietario.
     */
    public List<Predio> obtenerPredios(String idPropietario) {
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT * FROM predios WHERE id_propietario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPropietario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Predio predio = new Predio();
                predio.setId(rs.getString("id"));
                predio.setCodigoIca(rs.getString("codigo_ica"));
                predio.setDireccion(rs.getString("direccion"));
                predio.setArea(rs.getDouble("area"));
                predio.setLatitud(rs.getDouble("latitud"));
                predio.setLongitud(rs.getDouble("longitud"));
                predios.add(predio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return predios;
    }

    /**
     * Construye un objeto Propietario a partir de un ResultSet.
     */
    private Propietario construirPropietario(ResultSet rs) throws SQLException {
        Propietario propietario = new Propietario();
        // En la tabla usuarios, 'id' es la clave primaria; 'numero_identificacion' es el documento
        propietario.setId(rs.getString("id"));
        propietario.setRol(rs.getString("rol"));
        propietario.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        propietario.setNombre(rs.getString("nombre"));
        propietario.setTelefonoContacto(rs.getString("telefono_contacto"));
        propietario.setCorreoElectronico(rs.getString("correo_electronico"));
        // direccion proviene de la tabla propietario (joined as p.direccion)
        try {
            String dir = rs.getString("direccion");
            propietario.setDireccion(dir);
        } catch (SQLException ex) {
            // En caso de que la columna no exista en el ResultSet, no interrumpimos
        }
        // Debug: imprimir los valores cargados
        System.out.println("[DEBUG] construirPropietario: id=" + propietario.getId() + ", numero_identificacion=" + propietario.getNumeroIdentificacion() + ", nombre=" + propietario.getNombre() + ", direccion=" + propietario.getDireccion());
        
        // Cargar permisos
        List<String> permisos = obtenerPermisos(rs.getString("id"));
        propietario.setPermiso(permisos);
        
        return propietario;
    }
}
