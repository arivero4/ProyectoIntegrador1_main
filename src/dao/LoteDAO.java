package dao;

import model.Lote;
import model.LugarProduccion;
import model.Cultivo;
import model.InspeccionFitosanitaria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de lotes en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class LoteDAO extends GenericDAO {

    /**
     * Inserta un nuevo lote en la base de datos.
     */
    public boolean insertar(Lote lote) {
        String sql = "INSERT INTO lotes (id, descripcion, extension, id_lugar_produccion) " +
                     "VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, lote.getId());
            ps.setString(2, lote.getDescripcion());
            ps.setDouble(3, lote.getExtension());
            ps.setString(4, lote.getLugarProduccion() != null ? lote.getLugarProduccion().getId() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un lote existente.
     */
    public boolean actualizar(Lote lote) {
        String sql = "UPDATE lotes SET descripcion = ?, extension = ?, id_lugar_produccion = ? " +
                     "WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, lote.getDescripcion());
            ps.setDouble(2, lote.getExtension());
            ps.setString(3, lote.getLugarProduccion() != null ? lote.getLugarProduccion().getId() : null);
            ps.setString(4, lote.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un lote de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM lotes WHERE id = ?";
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
     * Busca un lote por su ID.
     */
    public Lote buscarPorId(String id) {
        String sql = "SELECT l.*, lp.codigo_ica as codigo_ica_lugar " +
                     "FROM lotes l " +
                     "LEFT JOIN lugares_produccion lp ON l.id_lugar_produccion = lp.id " +
                     "WHERE l.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirLote(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los lotes registrados.
     */
    public List<Lote> listar() {
        List<Lote> lista = new ArrayList<>();
        String sql = "SELECT l.*, lp.codigo_ica as codigo_ica_lugar " +
                     "FROM lotes l " +
                     "LEFT JOIN lugares_produccion lp ON l.id_lugar_produccion = lp.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirLote(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los lotes de un lugar de producción específico.
     */
    public List<Lote> listarPorLugarProduccion(String idLugarProduccion) {
        List<Lote> lista = new ArrayList<>();
        String sql = "SELECT l.*, lp.codigo_ica as codigo_ica_lugar " +
                     "FROM lotes l " +
                     "LEFT JOIN lugares_produccion lp ON l.id_lugar_produccion = lp.id " +
                     "WHERE l.id_lugar_produccion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idLugarProduccion);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirLote(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los cultivos asociados a un lote.
     */
    public List<Cultivo> obtenerCultivos(String idLote) {
        List<Cultivo> cultivos = new ArrayList<>();
        String sql = "SELECT c.* FROM cultivos c " +
                     "INNER JOIN lote_cultivo lc ON c.id = lc.id_cultivo " +
                     "WHERE lc.id_lote = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idLote);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Cultivo cultivo = new Cultivo();
                cultivo.setId(rs.getString("id"));
                cultivo.setNombreVariedad(rs.getString("nombre_variedad"));
                cultivo.setNombreCultivo(rs.getString("nombre_cultivo"));
                cultivo.setEspecieVegetal(rs.getString("especie_vegetal"));
                cultivo.setDescripcion(rs.getString("descripcion"));
                cultivos.add(cultivo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return cultivos;
    }

    /**
     * Asocia un cultivo a un lote.
     */
    public boolean asociarCultivo(String idLote, String idCultivo) {
        String sql = "INSERT INTO lote_cultivo (id_lote, id_cultivo) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idLote);
            ps.setString(2, idCultivo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Desasocia un cultivo de un lote.
     */
    public boolean desasociarCultivo(String idLote, String idCultivo) {
        String sql = "DELETE FROM lote_cultivo WHERE id_lote = ? AND id_cultivo = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idLote);
            ps.setString(2, idCultivo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Obtiene la inspección fitosanitaria asociada a un lote.
     */
    public InspeccionFitosanitaria obtenerInspeccion(String idLote) {
        String sql = "SELECT if.* FROM inspecciones_fitosanitarias if " +
                     "INNER JOIN inspeccion_lote il ON if.id = il.id_inspeccion " +
                     "WHERE il.id_lote = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idLote);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                InspeccionFitosanitaria inspeccion = new InspeccionFitosanitaria();
                inspeccion.setId(rs.getString("id"));
                inspeccion.setCodigoIca(rs.getString("codigo_ica"));
                inspeccion.setFechaInspeccion(rs.getString("fecha_inspeccion"));
                return inspeccion;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Construye un objeto Lote a partir de un ResultSet.
     */
    private Lote construirLote(ResultSet rs) throws SQLException {
        Lote lote = new Lote();
        lote.setId(rs.getString("id"));
        lote.setDescripcion(rs.getString("descripcion"));
        lote.setExtension(rs.getDouble("extension"));
        
        // Construir lugar de producción si existe
        if (rs.getString("id_lugar_produccion") != null) {
            LugarProduccion lugar = new LugarProduccion();
            lugar.setId(rs.getString("id_lugar_produccion"));
            lugar.setCodigoIca(rs.getString("codigo_ica_lugar"));
            lote.setLugarProduccion(lugar);
        }
        
        return lote;
    }
}
