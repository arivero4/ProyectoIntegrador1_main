package dao;

import model.InspeccionFitosanitaria;
import model.Lote;
import model.ResultadoTecnico;
import model.AsistenteTecnico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de inspecciones fitosanitarias en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class InspeccionFitosanitariaDAO extends GenericDAO {

    /**
     * Inserta una nueva inspección fitosanitaria en la base de datos.
     */
    public boolean insertar(InspeccionFitosanitaria inspeccion) {
        String sql = "INSERT INTO inspecciones_fitosanitarias (id, codigo_ica, fecha_inspeccion, " +
                     "id_asistente_tecnico) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, inspeccion.getId());
            ps.setString(2, inspeccion.getCodigoIca());
            ps.setString(3, inspeccion.getFechaInspeccion());
            ps.setString(4, inspeccion.getAsistenteTecnico() != null ? 
                         inspeccion.getAsistenteTecnico().getId() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de una inspección fitosanitaria existente.
     */
    public boolean actualizar(InspeccionFitosanitaria inspeccion) {
        String sql = "UPDATE inspecciones_fitosanitarias SET codigo_ica = ?, fecha_inspeccion = ?, " +
                     "id_asistente_tecnico = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, inspeccion.getCodigoIca());
            ps.setString(2, inspeccion.getFechaInspeccion());
            ps.setString(3, inspeccion.getAsistenteTecnico() != null ? 
                         inspeccion.getAsistenteTecnico().getId() : null);
            ps.setString(4, inspeccion.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina una inspección fitosanitaria de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM inspecciones_fitosanitarias WHERE id = ?";
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
     * Busca una inspección fitosanitaria por su ID.
     */
    public InspeccionFitosanitaria buscarPorId(String id) {
        String sql = "SELECT i.*, u.nombre as nombre_asistente " +
                     "FROM inspecciones_fitosanitarias i " +
                     "LEFT JOIN asistentes_tecnicos at ON i.id_asistente_tecnico = at.id " +
                     "LEFT JOIN usuarios u ON at.id = u.id " +
                     "WHERE i.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirInspeccion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca una inspección por su código ICA.
     */
    public InspeccionFitosanitaria buscarPorCodigoIca(String codigoIca) {
        String sql = "SELECT i.*, u.nombre as nombre_asistente " +
                     "FROM inspecciones_fitosanitarias i " +
                     "LEFT JOIN asistentes_tecnicos at ON i.id_asistente_tecnico = at.id " +
                     "LEFT JOIN usuarios u ON at.id = u.id " +
                     "WHERE i.codigo_ica = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoIca);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirInspeccion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todas las inspecciones fitosanitarias registradas.
     */
    public List<InspeccionFitosanitaria> listar() {
        List<InspeccionFitosanitaria> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre as nombre_asistente " +
                     "FROM inspecciones_fitosanitarias i " +
                     "LEFT JOIN asistentes_tecnicos at ON i.id_asistente_tecnico = at.id " +
                     "LEFT JOIN usuarios u ON at.id = u.id " +
                     "ORDER BY i.fecha_inspeccion DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirInspeccion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista las inspecciones realizadas por un asistente técnico específico.
     */
    public List<InspeccionFitosanitaria> listarPorAsistente(String idAsistente) {
        List<InspeccionFitosanitaria> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre as nombre_asistente " +
                     "FROM inspecciones_fitosanitarias i " +
                     "LEFT JOIN asistentes_tecnicos at ON i.id_asistente_tecnico = at.id " +
                     "LEFT JOIN usuarios u ON at.id = u.id " +
                     "WHERE i.id_asistente_tecnico = ? " +
                     "ORDER BY i.fecha_inspeccion DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idAsistente);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirInspeccion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista las inspecciones realizadas en un rango de fechas.
     */
    public List<InspeccionFitosanitaria> listarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<InspeccionFitosanitaria> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre as nombre_asistente " +
                     "FROM inspecciones_fitosanitarias i " +
                     "LEFT JOIN asistentes_tecnicos at ON i.id_asistente_tecnico = at.id " +
                     "LEFT JOIN usuarios u ON at.id = u.id " +
                     "WHERE i.fecha_inspeccion BETWEEN ? AND ? " +
                     "ORDER BY i.fecha_inspeccion DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirInspeccion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los lotes inspeccionados en una inspección.
     */
    public List<Lote> obtenerLotes(String idInspeccion) {
        List<Lote> lotes = new ArrayList<>();
        String sql = "SELECT l.* FROM lotes l " +
                     "INNER JOIN inspeccion_lote il ON l.id = il.id_lote " +
                     "WHERE il.id_inspeccion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInspeccion);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getString("id"));
                lote.setDescripcion(rs.getString("descripcion"));
                lote.setExtension(rs.getDouble("extension"));
                lotes.add(lote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lotes;
    }

    /**
     * Asocia un lote a una inspección.
     */
    public boolean asociarLote(String idInspeccion, String idLote) {
        String sql = "INSERT INTO inspeccion_lote (id_inspeccion, id_lote) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInspeccion);
            ps.setString(2, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Obtiene los resultados técnicos de una inspección.
     */
    public List<ResultadoTecnico> obtenerResultadosTecnicos(String idInspeccion) {
        List<ResultadoTecnico> resultados = new ArrayList<>();
        String sql = "SELECT * FROM resultados_tecnicos WHERE id_inspeccion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInspeccion);
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
     * Construye un objeto InspeccionFitosanitaria a partir de un ResultSet.
     */
    private InspeccionFitosanitaria construirInspeccion(ResultSet rs) throws SQLException {
        InspeccionFitosanitaria inspeccion = new InspeccionFitosanitaria();
        inspeccion.setId(rs.getString("id"));
        inspeccion.setCodigoIca(rs.getString("codigo_ica"));
        inspeccion.setFechaInspeccion(rs.getString("fecha_inspeccion"));
        
        // Construir asistente técnico si existe
        if (rs.getString("id_asistente_tecnico") != null) {
            AsistenteTecnico asistente = new AsistenteTecnico();
            asistente.setId(rs.getString("id_asistente_tecnico"));
            asistente.setNombre(rs.getString("nombre_asistente"));
            inspeccion.setAsistenteTecnico(asistente);
        }
        
        return inspeccion;
    }
}
