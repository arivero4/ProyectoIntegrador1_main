package dao;

import model.Alerta;
import model.InformeFitosanitario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de alertas fitosanitarias en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class AlertaDAO extends GenericDAO {

    /**
     * Inserta una nueva alerta en la base de datos.
     */
    public boolean insertar(Alerta alerta) {
        String sql = "INSERT INTO alertas (id, nivel_riesgo, id_informe) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, alerta.getId());
            ps.setString(2, alerta.getNivelRiesgo());
            ps.setString(3, alerta.getInformeFitosanitario() != null ? 
                         alerta.getInformeFitosanitario().getId() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de una alerta existente.
     */
    public boolean actualizar(Alerta alerta) {
        String sql = "UPDATE alertas SET nivel_riesgo = ?, id_informe = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, alerta.getNivelRiesgo());
            ps.setString(2, alerta.getInformeFitosanitario() != null ? 
                         alerta.getInformeFitosanitario().getId() : null);
            ps.setString(3, alerta.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina una alerta de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM alertas WHERE id = ?";
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
     * Busca una alerta por su ID.
     */
    public Alerta buscarPorId(String id) {
        String sql = "SELECT a.*, i.codigo_ica, i.nivel_incidencia, i.fecha_informe " +
                     "FROM alertas a " +
                     "LEFT JOIN informes_fitosanitarios i ON a.id_informe = i.id " +
                     "WHERE a.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirAlerta(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todas las alertas registradas.
     */
    public List<Alerta> listar() {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT a.*, i.codigo_ica, i.nivel_incidencia, i.fecha_informe " +
                     "FROM alertas a " +
                     "LEFT JOIN informes_fitosanitarios i ON a.id_informe = i.id " +
                     "ORDER BY i.fecha_informe DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirAlerta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista las alertas por nivel de riesgo.
     */
    public List<Alerta> listarPorNivelRiesgo(String nivelRiesgo) {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT a.*, i.codigo_ica, i.nivel_incidencia, i.fecha_informe " +
                     "FROM alertas a " +
                     "LEFT JOIN informes_fitosanitarios i ON a.id_informe = i.id " +
                     "WHERE a.nivel_riesgo = ? " +
                     "ORDER BY i.fecha_informe DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nivelRiesgo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirAlerta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista las alertas de un informe específico.
     */
    public List<Alerta> listarPorInforme(String idInforme) {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT a.*, i.codigo_ica, i.nivel_incidencia, i.fecha_informe " +
                     "FROM alertas a " +
                     "LEFT JOIN informes_fitosanitarios i ON a.id_informe = i.id " +
                     "WHERE a.id_informe = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInforme);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirAlerta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista las alertas activas (de alto riesgo).
     */
    public List<Alerta> listarAlertasActivas() {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT a.*, i.codigo_ica, i.nivel_incidencia, i.fecha_informe " +
                     "FROM alertas a " +
                     "LEFT JOIN informes_fitosanitarios i ON a.id_informe = i.id " +
                     "WHERE a.nivel_riesgo IN ('Alto', 'Crítico') " +
                     "ORDER BY i.fecha_informe DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirAlerta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Cuenta el total de alertas por nivel de riesgo.
     */
    public int contarPorNivelRiesgo(String nivelRiesgo) {
        String sql = "SELECT COUNT(*) as total FROM alertas WHERE nivel_riesgo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nivelRiesgo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return 0;
    }

    /**
     * Obtiene estadísticas de alertas por nivel de riesgo.
     */
    public List<String> obtenerEstadisticasPorNivel() {
        List<String> estadisticas = new ArrayList<>();
        String sql = "SELECT nivel_riesgo, COUNT(*) as total FROM alertas " +
                     "GROUP BY nivel_riesgo ORDER BY total DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                String estadistica = rs.getString("nivel_riesgo") + ": " + rs.getInt("total");
                estadisticas.add(estadistica);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return estadisticas;
    }

    /**
     * Construye un objeto Alerta a partir de un ResultSet.
     */
    private Alerta construirAlerta(ResultSet rs) throws SQLException {
        Alerta alerta = new Alerta();
        alerta.setId(rs.getString("id"));
        alerta.setNivelRiesgo(rs.getString("nivel_riesgo"));
        
        // Construir informe fitosanitario si existe
        if (rs.getString("id_informe") != null) {
            InformeFitosanitario informe = new InformeFitosanitario();
            informe.setId(rs.getString("id_informe"));
            informe.setCodigoIca(rs.getString("codigo_ica"));
            informe.setNivelIncidencia(rs.getDouble("nivel_incidencia"));
            informe.setFechaInforme(rs.getString("fecha_informe"));
            alerta.agregarInformeFitosanitario(informe);
        }
        
        return alerta;
    }
}
