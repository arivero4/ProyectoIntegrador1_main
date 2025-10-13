package dao;

import model.AsistenteTecnico;
import model.LugarProduccion;
import model.InspeccionFitosanitaria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de asistentes técnicos en la base de datos.
 * Extiende UsuarioDAO para heredar funcionalidad común de usuarios.
 */
public class AsistenteTecnicoDAO extends UsuarioDAO {

    /**
     * Inserta un nuevo asistente técnico en la base de datos.
     */
    public boolean insertar(AsistenteTecnico asistente) {
        PreparedStatement ps = null;
        try {
            // Primero insertar en la tabla base de usuarios
            if (!insertarUsuarioBase(asistente)) {
                return false;
            }

            // Luego insertar en la tabla específica de asistentes técnicos
            String sql = "INSERT INTO asistentes_tecnicos (id, numero_tarjeta_profesional) VALUES (?, ?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, asistente.getId());
            ps.setString(2, asistente.getNumeroTarjetaProfesional());
            
            boolean resultado = ps.executeUpdate() > 0;
            
            // Insertar permisos si existen
            if (resultado && asistente.getPermiso() != null && !asistente.getPermiso().isEmpty()) {
                insertarPermisos(asistente.getId(), asistente.getPermiso());
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
     * Actualiza los datos de un asistente técnico existente.
     */
    public boolean actualizar(AsistenteTecnico asistente) {
        PreparedStatement ps = null;
        try {
            // Actualizar datos base del usuario
            if (!actualizarUsuarioBase(asistente)) {
                return false;
            }
            
            // Actualizar datos específicos del asistente técnico
            String sql = "UPDATE asistentes_tecnicos SET numero_tarjeta_profesional = ? WHERE id = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, asistente.getNumeroTarjetaProfesional());
            ps.setString(2, asistente.getId());
            
            boolean resultado = ps.executeUpdate() > 0;
            
            // Actualizar permisos
            if (resultado && asistente.getPermiso() != null) {
                actualizarPermisos(asistente.getId(), asistente.getPermiso());
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
     * Elimina un asistente técnico de la base de datos.
     */
    public boolean eliminar(String id) {
        PreparedStatement ps = null;
        try {
            // Primero eliminar de la tabla específica
            String sql = "DELETE FROM asistentes_tecnicos WHERE id = ?";
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
     * Busca un asistente técnico por su ID.
     */
    public AsistenteTecnico buscarPorId(String id) {
        String sql = "SELECT u.*, a.numero_tarjeta_profesional FROM usuarios u " +
                     "INNER JOIN asistentes_tecnicos a ON u.id = a.id " +
                     "WHERE u.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirAsistenteTecnico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un asistente técnico por su número de identificación.
     */
    public AsistenteTecnico buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT u.*, a.numero_tarjeta_profesional FROM usuarios u " +
                     "INNER JOIN asistentes_tecnicos a ON u.id = a.id " +
                     "WHERE u.numero_identificacion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroIdentificacion);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirAsistenteTecnico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un asistente técnico por su número de tarjeta profesional.
     */
    public AsistenteTecnico buscarPorTarjetaProfesional(String numeroTarjeta) {
        String sql = "SELECT u.*, a.numero_tarjeta_profesional FROM usuarios u " +
                     "INNER JOIN asistentes_tecnicos a ON u.id = a.id " +
                     "WHERE a.numero_tarjeta_profesional = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, numeroTarjeta);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirAsistenteTecnico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los asistentes técnicos registrados.
     */
    public List<AsistenteTecnico> listar() {
        List<AsistenteTecnico> lista = new ArrayList<>();
        String sql = "SELECT u.*, a.numero_tarjeta_profesional FROM usuarios u " +
                     "INNER JOIN asistentes_tecnicos a ON u.id = a.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirAsistenteTecnico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los lugares de producción asociados a un asistente técnico.
     */
    public List<LugarProduccion> obtenerLugaresProduccion(String idAsistente) {
        List<LugarProduccion> lugares = new ArrayList<>();
        String sql = "SELECT * FROM lugares_produccion WHERE id_asistente_tecnico = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idAsistente);
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
     * Obtiene las inspecciones fitosanitarias realizadas por un asistente técnico.
     */
    public List<InspeccionFitosanitaria> obtenerInspecciones(String idAsistente) {
        List<InspeccionFitosanitaria> inspecciones = new ArrayList<>();
        String sql = "SELECT * FROM inspecciones_fitosanitarias WHERE id_asistente_tecnico = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idAsistente);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                InspeccionFitosanitaria inspeccion = new InspeccionFitosanitaria();
                inspeccion.setId(rs.getString("id"));
                inspeccion.setCodigoIca(rs.getString("codigo_ica"));
                inspeccion.setFechaInspeccion(rs.getString("fecha_inspeccion"));
                inspecciones.add(inspeccion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return inspecciones;
    }

    /**
     * Construye un objeto AsistenteTecnico a partir de un ResultSet.
     */
    private AsistenteTecnico construirAsistenteTecnico(ResultSet rs) throws SQLException {
        AsistenteTecnico asistente = new AsistenteTecnico();
        asistente.setId(rs.getString("id"));
        asistente.setRol(rs.getString("rol"));
        asistente.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        asistente.setNombre(rs.getString("nombre"));
        asistente.setTelefonoContacto(rs.getString("telefono_contacto"));
        asistente.setCorreoElectronico(rs.getString("correo_electronico"));
        asistente.setNumeroTarjetaProfesional(rs.getString("numero_tarjeta_profesional"));
        
        // Cargar permisos
        List<String> permisos = obtenerPermisos(rs.getString("id"));
        asistente.setPermiso(permisos);
        
        return asistente;
    }
}
