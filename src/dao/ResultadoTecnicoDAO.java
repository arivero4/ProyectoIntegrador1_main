package dao;

import model.ResultadoTecnico;
import model.Cultivo;
import model.Plaga;
import model.InspeccionFitosanitaria;
import model.InformeFitosanitario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de resultados técnicos en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class ResultadoTecnicoDAO extends GenericDAO {

    /**
     * Inserta un nuevo resultado técnico en la base de datos.
     */
    public boolean insertar(ResultadoTecnico resultado) {
        String sql = "INSERT INTO resultados_tecnicos (id, total_plantas_evaluadas, plantas_afectadas, " +
                     "observaciones, id_inspeccion, id_informe) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, resultado.getId());
            ps.setInt(2, resultado.getTotalPlantasEvaluadas());
            ps.setInt(3, resultado.getPlantasAfectadas());
            ps.setString(4, resultado.getObservaciones());
            ps.setString(5, resultado.getInspeccionFitosanitaria() != null ? 
                         resultado.getInspeccionFitosanitaria().getId() : null);
            ps.setString(6, resultado.getInformeFitosanitario() != null ? 
                         resultado.getInformeFitosanitario().getId() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un resultado técnico existente.
     */
    public boolean actualizar(ResultadoTecnico resultado) {
        String sql = "UPDATE resultados_tecnicos SET total_plantas_evaluadas = ?, plantas_afectadas = ?, " +
                     "observaciones = ?, id_inspeccion = ?, id_informe = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, resultado.getTotalPlantasEvaluadas());
            ps.setInt(2, resultado.getPlantasAfectadas());
            ps.setString(3, resultado.getObservaciones());
            ps.setString(4, resultado.getInspeccionFitosanitaria() != null ? 
                         resultado.getInspeccionFitosanitaria().getId() : null);
            ps.setString(5, resultado.getInformeFitosanitario() != null ? 
                         resultado.getInformeFitosanitario().getId() : null);
            ps.setString(6, resultado.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un resultado técnico de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM resultados_tecnicos WHERE id = ?";
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
     * Busca un resultado técnico por su ID.
     */
    public ResultadoTecnico buscarPorId(String id) {
        String sql = "SELECT * FROM resultados_tecnicos WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirResultadoTecnico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los resultados técnicos registrados.
     */
    public List<ResultadoTecnico> listar() {
        List<ResultadoTecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM resultados_tecnicos";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirResultadoTecnico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los resultados técnicos de una inspección específica.
     */
    public List<ResultadoTecnico> listarPorInspeccion(String idInspeccion) {
        List<ResultadoTecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM resultados_tecnicos WHERE id_inspeccion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInspeccion);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirResultadoTecnico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los resultados técnicos de un informe específico.
     */
    public List<ResultadoTecnico> listarPorInforme(String idInforme) {
        List<ResultadoTecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM resultados_tecnicos WHERE id_informe = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idInforme);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirResultadoTecnico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los cultivos evaluados en un resultado técnico.
     */
    public List<Cultivo> obtenerCultivos(String idResultado) {
        List<Cultivo> cultivos = new ArrayList<>();
        String sql = "SELECT c.* FROM cultivos c " +
                     "INNER JOIN resultado_cultivo rc ON c.id = rc.id_cultivo " +
                     "WHERE rc.id_resultado = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idResultado);
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
     * Asocia un cultivo a un resultado técnico.
     */
    public boolean asociarCultivo(String idResultado, String idCultivo) {
        String sql = "INSERT INTO resultado_cultivo (id_resultado, id_cultivo) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idResultado);
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
     * Obtiene las plagas detectadas en un resultado técnico.
     */
    public List<Plaga> obtenerPlagas(String idResultado) {
        List<Plaga> plagas = new ArrayList<>();
        String sql = "SELECT p.* FROM plagas p " +
                     "INNER JOIN resultado_plaga rp ON p.id = rp.id_plaga " +
                     "WHERE rp.id_resultado = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idResultado);
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
     * Asocia una plaga a un resultado técnico.
     */
    public boolean asociarPlaga(String idResultado, String idPlaga) {
        String sql = "INSERT INTO resultado_plaga (id_resultado, id_plaga) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idResultado);
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
     * Calcula el porcentaje de afectación de un resultado técnico.
     */
    public double calcularPorcentajeAfectacion(String idResultado) {
        ResultadoTecnico resultado = buscarPorId(idResultado);
        if (resultado != null && resultado.getTotalPlantasEvaluadas() > 0) {
            return (resultado.getPlantasAfectadas() * 100.0) / resultado.getTotalPlantasEvaluadas();
        }
        return 0.0;
    }

    /**
     * Construye un objeto ResultadoTecnico a partir de un ResultSet.
     */
    private ResultadoTecnico construirResultadoTecnico(ResultSet rs) throws SQLException {
        ResultadoTecnico resultado = new ResultadoTecnico();
        resultado.setId(rs.getString("id"));
        resultado.setTotalPlantasEvaluadas(rs.getInt("total_plantas_evaluadas"));
        resultado.setObservaciones(rs.getString("observaciones"));
        
        // Construir inspección si existe
        if (rs.getString("id_inspeccion") != null) {
            InspeccionFitosanitaria inspeccion = new InspeccionFitosanitaria();
            inspeccion.setId(rs.getString("id_inspeccion"));
            resultado.setInspeccionFitosanitaria(inspeccion);
        }
        
        // Construir informe si existe
        if (rs.getString("id_informe") != null) {
            InformeFitosanitario informe = new InformeFitosanitario();
            informe.setId(rs.getString("id_informe"));
            resultado.setInformeFitosanitario(informe);
        }
        
        return resultado;
    }
}
