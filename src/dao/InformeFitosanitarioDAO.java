package dao;

import model.InformeFitosanitario;
import model.ResultadoTecnico;
import model.Alerta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de informes fitosanitarios en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class InformeFitosanitarioDAO extends GenericDAO {

    /**
     * Inserta un nuevo informe fitosanitario en la base de datos.
     */
    public boolean insertar(InformeFitosanitario informe) {
        String sql = "INSERT INTO informes_fitosanitarios (id, codigo_ica, total_plantas_evaluadas, " +
                     "nivel_incidencia, observaciones, fecha_informe) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, informe.getId());
            ps.setString(2, informe.getCodigoIca());
            ps.setInt(3, informe.getTotalPlantasEvaluadas());
            ps.setDouble(4, informe.getNivelIncidencia());
            ps.setString(5, informe.getObservaciones());
            ps.setString(6, informe.getFechaInforme());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un informe fitosanitario existente.
     */
    public boolean actualizar(InformeFitosanitario informe) {
        String sql = "UPDATE informes_fitosanitarios SET codigo_ica = ?, total_plantas_evaluadas = ?, " +
                     "nivel_incidencia = ?, observaciones = ?, fecha_informe = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, informe.getCodigoIca());
            ps.setInt(2, informe.getTotalPlantasEvaluadas());
            ps.setDouble(3, informe.getNivelIncidencia());
            ps.setString(4, informe.getObservaciones());
            ps.setString(5, informe.getFechaInforme());
            ps.setString(6, informe.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un informe fitosanitario de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM informes_fitosanitarios WHERE id = ?";
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
     * Busca un informe fitosanitario por su ID.
     */
    public InformeFitosanitario buscarPorId(String id) {
        String sql = "SELECT * FROM informes_fitosanitarios WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirInforme(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un informe por su código ICA.
     */
    public InformeFitosanitario buscarPorCodigoIca(String codigoIca) {
        String sql = "SELECT * FROM informes_fitosanitarios WHERE codigo_ica = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoIca);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirInforme(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los informes fitosanitarios registrados.
     */
    public List<InformeFitosanitario> listar() {
        List<InformeFitosanitario> lista = new ArrayList<>();
        String sql = "SELECT * FROM informes_fitosanitarios ORDER BY fecha_informe DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirInforme(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los informes generados en un rango de fechas.
     */
    public List<InformeFitosanitario> listarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<InformeFitosanitario> lista = new ArrayList<>();
        String sql = "SELECT * FROM informes_fitosanitarios " +
                     "WHERE fecha_informe BETWEEN ? AND ? " +
                     "ORDER BY fecha_informe DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirInforme(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los informes con nivel de incidencia superior a un umbral.
     */
    public List<InformeFitosanitario> listarPorNivelIncidencia(double nivelMinimo) {
        List<InformeFitosanitario> lista = new ArrayList<>();
        String sql = "SELECT * FROM informes_fitosanitarios " +
                     "WHERE nivel_incidencia >= ? " +
                     "ORDER BY nivel_incidencia DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setDouble(1, nivelMinimo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirInforme(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los resultados técnicos asociados a un informe.
     */
    public List<ResultadoTecnico> obtenerResultadosTecnicos(String idInforme) {
        List<ResultadoTecnico> resultados = new ArrayList<>();
        String sql = "SELECT * FROM resultados_tecnicos WHERE id_informe = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInforme);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ResultadoTecnico resultado = new ResultadoTecnico();
                resultado.setId(rs.getString("id"));
                resultado.setTotalPlantasEvaluadas(rs.getInt("total_plantas_evaluadas"));
                resultado.setObservaciones(rs.getString("observaciones"));
                resultados.add(resultado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return resultados;
    }

    /**
     * Obtiene las alertas generadas a partir de un informe.
     */
    public List<Alerta> obtenerAlertas(String idInforme) {
        List<Alerta> alertas = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE id_informe = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInforme);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Alerta alerta = new Alerta();
                alerta.setId(rs.getString("id"));
                alerta.setNivelRiesgo(rs.getString("nivel_riesgo"));
                alertas.add(alerta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return alertas;
    }

    /**
     * Calcula estadísticas agregadas de informes.
     */
    public double obtenerPromedioIncidencia() {
        String sql = "SELECT AVG(nivel_incidencia) as promedio FROM informes_fitosanitarios";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("promedio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return 0.0;
    }

    /**
     * Obtiene el total de plantas evaluadas en todos los informes.
     */
    public int obtenerTotalPlantasEvaluadas() {
        String sql = "SELECT SUM(total_plantas_evaluadas) as total FROM informes_fitosanitarios";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
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
     * Construye un objeto InformeFitosanitario a partir de un ResultSet.
     */
    private InformeFitosanitario construirInforme(ResultSet rs) throws SQLException {
        InformeFitosanitario informe = new InformeFitosanitario();
        informe.setId(rs.getString("id"));
        informe.setCodigoIca(rs.getString("codigo_ica"));
        informe.setToltalPlantasEvaluadas(rs.getInt("total_plantas_evaluadas"));
        informe.setNivelIncidencia(rs.getDouble("nivel_incidencia"));
        informe.setObservaciones(rs.getString("observaciones"));
        informe.setFechaInforme(rs.getString("fecha_informe"));
        return informe;
    }
}
