package dao;

import model.Plaga;
import model.Cultivo;
import model.ResultadoTecnico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de plagas en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class PlagaDAO extends GenericDAO {

    /**
     * Inserta una nueva plaga en la base de datos.
     */
    public boolean insertar(Plaga plaga) {
        String sql = "INSERT INTO plagas (id, nombre_comun, nombre_cientifico, descripcion) " +
                     "VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, plaga.getId());
            ps.setString(2, plaga.getNombreComun());
            ps.setString(3, plaga.getNombreCientifico());
            ps.setString(4, plaga.getDescriopcion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de una plaga existente.
     */
    public boolean actualizar(Plaga plaga) {
        String sql = "UPDATE plagas SET nombre_comun = ?, nombre_cientifico = ?, " +
                     "descripcion = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, plaga.getNombreComun());
            ps.setString(2, plaga.getNombreCientifico());
            ps.setString(3, plaga.getDescriopcion());
            ps.setString(4, plaga.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina una plaga de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM plagas WHERE id = ?";
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
     * Busca una plaga por su ID.
     */
    public Plaga buscarPorId(String id) {
        String sql = "SELECT * FROM plagas WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPlaga(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca plagas por nombre común.
     */
    public List<Plaga> buscarPorNombreComun(String nombreComun) {
        List<Plaga> lista = new ArrayList<>();
        String sql = "SELECT * FROM plagas WHERE nombre_comun LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + nombreComun + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPlaga(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Busca plagas por nombre científico.
     */
    public List<Plaga> buscarPorNombreCientifico(String nombreCientifico) {
        List<Plaga> lista = new ArrayList<>();
        String sql = "SELECT * FROM plagas WHERE nombre_cientifico LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + nombreCientifico + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPlaga(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista todas las plagas registradas.
     */
    public List<Plaga> listar() {
        List<Plaga> lista = new ArrayList<>();
        String sql = "SELECT * FROM plagas";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPlaga(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los cultivos afectados por una plaga específica.
     */
    public List<Cultivo> obtenerCultivosAfectados(String idPlaga) {
        List<Cultivo> cultivos = new ArrayList<>();
        String sql = "SELECT c.* FROM cultivos c " +
                     "INNER JOIN cultivo_plaga cp ON c.id = cp.id_cultivo " +
                     "WHERE cp.id_plaga = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPlaga);
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
     * Obtiene los resultados técnicos donde se detectó una plaga específica.
     */
    public List<ResultadoTecnico> obtenerResultadosTecnicos(String idPlaga) {
        List<ResultadoTecnico> resultados = new ArrayList<>();
        String sql = "SELECT rt.* FROM resultados_tecnicos rt " +
                     "INNER JOIN resultado_plaga rp ON rt.id = rp.id_resultado " +
                     "WHERE rp.id_plaga = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPlaga);
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
     * Obtiene estadísticas de incidencia de una plaga.
     */
    public int obtenerTotalDetecciones(String idPlaga) {
        String sql = "SELECT COUNT(*) as total FROM resultado_plaga WHERE id_plaga = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPlaga);
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
     * Construye un objeto Plaga a partir de un ResultSet.
     */
    private Plaga construirPlaga(ResultSet rs) throws SQLException {
        Plaga plaga = new Plaga();
        plaga.setId(rs.getString("id"));
        plaga.setNombreComun(rs.getString("nombre_comun"));
        plaga.setNombreCientifico(rs.getString("nombre_cientifico"));
        plaga.setDescripcion(rs.getString("descripcion"));
        return plaga;
    }
}
