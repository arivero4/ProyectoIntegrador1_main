package dao;

import model.Cultivo;
import model.Lote;
import model.Plaga;
import model.ResultadoTecnico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de cultivos en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class CultivoDAO extends GenericDAO {

    /**
     * Inserta un nuevo cultivo en la base de datos.
     */
    public boolean insertar(Cultivo cultivo) {
        String sql = "INSERT INTO cultivos (id, nombre_variedad, nombre_cultivo, especie_vegetal, descripcion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cultivo.getId());
            ps.setString(2, cultivo.getNombreVariedad());
            ps.setString(3, cultivo.getNombreCultivo());
            ps.setString(4, cultivo.getEspecieVegetal());
            ps.setString(5, cultivo.getDescripcion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un cultivo existente.
     */
    public boolean actualizar(Cultivo cultivo) {
        String sql = "UPDATE cultivos SET nombre_variedad = ?, nombre_cultivo = ?, " +
                     "especie_vegetal = ?, descripcion = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cultivo.getNombreVariedad());
            ps.setString(2, cultivo.getNombreCultivo());
            ps.setString(3, cultivo.getEspecieVegetal());
            ps.setString(4, cultivo.getDescripcion());
            ps.setString(5, cultivo.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un cultivo de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM cultivos WHERE id = ?";
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
     * Busca un cultivo por su ID.
     */
    public Cultivo buscarPorId(String id) {
        String sql = "SELECT * FROM cultivos WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirCultivo(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca cultivos por nombre.
     */
    public List<Cultivo> buscarPorNombre(String nombre) {
        List<Cultivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM cultivos WHERE nombre_cultivo LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirCultivo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista todos los cultivos registrados.
     */
    public List<Cultivo> listar() {
        List<Cultivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM cultivos";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirCultivo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los lotes donde se cultiva un cultivo específico.
     */
    public List<Lote> obtenerLotes(String idCultivo) {
        List<Lote> lotes = new ArrayList<>();
        String sql = "SELECT l.* FROM lotes l " +
                     "INNER JOIN lote_cultivo lc ON l.id = lc.id_lote " +
                     "WHERE lc.id_cultivo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idCultivo);
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
     * Obtiene las plagas asociadas a un cultivo.
     */
    public List<Plaga> obtenerPlagas(String idCultivo) {
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT p.* FROM plagas p " +
                     "INNER JOIN cultivo_plaga cp ON p.id = cp.id_plaga " +
                     "WHERE cp.id_cultivo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idCultivo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Plaga plaga = new Plaga();
                plaga.setId(rs.getString("id"));
                plaga.setNombreComun(rs.getString("nombre_comun"));
                plaga.setNombreCientifico(rs.getString("nombre_cientifico"));
                plaga.setDescripcion(rs.getString("descripcion"));
                plagas.add(plaga);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return plagas;
    }

    /**
     * Asocia una plaga a un cultivo.
     */
    public boolean asociarPlaga(String idCultivo, String idPlaga) {
        String sql = "INSERT INTO cultivo_plaga (id_cultivo, id_plaga) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idCultivo);
            ps.setString(2, idPlaga);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Desasocia una plaga de un cultivo.
     */
    public boolean desasociarPlaga(String idCultivo, String idPlaga) {
        String sql = "DELETE FROM cultivo_plaga WHERE id_cultivo = ? AND id_plaga = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idCultivo);
            ps.setString(2, idPlaga);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Obtiene los resultados técnicos asociados a un cultivo.
     */
    public List<ResultadoTecnico> obtenerResultadosTecnicos(String idCultivo) {
        List<ResultadoTecnico> resultados = new ArrayList<>();
        String sql = "SELECT rt.* FROM resultados_tecnicos rt " +
                     "INNER JOIN resultado_cultivo rc ON rt.id = rc.id_resultado " +
                     "WHERE rc.id_cultivo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idCultivo);
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
     * Construye un objeto Cultivo a partir de un ResultSet.
     */
    private Cultivo construirCultivo(ResultSet rs) throws SQLException {
        Cultivo cultivo = new Cultivo();
        cultivo.setId(rs.getString("id"));
        cultivo.setNombreVariedad(rs.getString("nombre_variedad"));
        cultivo.setNombreCultivo(rs.getString("nombre_cultivo"));
        cultivo.setEspecieVegetal(rs.getString("especie_vegetal"));
        cultivo.setDescripcion(rs.getString("descripcion"));
        return cultivo;
    }
}
