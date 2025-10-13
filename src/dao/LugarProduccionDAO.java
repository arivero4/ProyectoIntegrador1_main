package dao;

import model.LugarProduccion;
import model.Predio;
import model.Productor;
import model.AsistenteTecnico;
import model.Lote;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de lugares de producción en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class LugarProduccionDAO extends GenericDAO {

    /**
     * Inserta un nuevo lugar de producción en la base de datos.
     */
    public boolean insertar(LugarProduccion lugar) {
        String sql = "INSERT INTO lugares_produccion (id, codigo_ica, id_predio, id_productor, " +
                     "id_asistente_tecnico) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, lugar.getId());
            ps.setString(2, lugar.getCodigoIca());
            ps.setString(3, lugar.getPredio() != null ? lugar.getPredio().getId() : null);
            ps.setString(4, lugar.getProductor() != null ? lugar.getProductor().getId() : null);
            ps.setString(5, lugar.getAsistenteTecnico() != null ? lugar.getAsistenteTecnico().getId() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un lugar de producción existente.
     */
    public boolean actualizar(LugarProduccion lugar) {
        String sql = "UPDATE lugares_produccion SET codigo_ica = ?, id_predio = ?, " +
                     "id_productor = ?, id_asistente_tecnico = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, lugar.getCodigoIca());
            ps.setString(2, lugar.getPredio() != null ? lugar.getPredio().getId() : null);
            ps.setString(3, lugar.getProductor() != null ? lugar.getProductor().getId() : null);
            ps.setString(4, lugar.getAsistenteTecnico() != null ? lugar.getAsistenteTecnico().getId() : null);
            ps.setString(5, lugar.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un lugar de producción de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM lugares_produccion WHERE id = ?";
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
     * Busca un lugar de producción por su ID.
     */
    public LugarProduccion buscarPorId(String id) {
        String sql = "SELECT lp.*, pr.nombre as nombre_productor, at.nombre as nombre_asistente " +
                     "FROM lugares_produccion lp " +
                     "LEFT JOIN productores prod_table ON lp.id_productor = prod_table.id " +
                     "LEFT JOIN usuarios pr ON prod_table.id = pr.id " +
                     "LEFT JOIN asistentes_tecnicos at_table ON lp.id_asistente_tecnico = at_table.id " +
                     "LEFT JOIN usuarios at ON at_table.id = at.id " +
                     "WHERE lp.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirLugarProduccion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un lugar de producción por su código ICA.
     */
    public LugarProduccion buscarPorCodigoIca(String codigoIca) {
        String sql = "SELECT lp.*, pr.nombre as nombre_productor, at.nombre as nombre_asistente " +
                     "FROM lugares_produccion lp " +
                     "LEFT JOIN productores prod_table ON lp.id_productor = prod_table.id " +
                     "LEFT JOIN usuarios pr ON prod_table.id = pr.id " +
                     "LEFT JOIN asistentes_tecnicos at_table ON lp.id_asistente_tecnico = at_table.id " +
                     "LEFT JOIN usuarios at ON at_table.id = at.id " +
                     "WHERE lp.codigo_ica = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoIca);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirLugarProduccion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los lugares de producción registrados.
     */
    public List<LugarProduccion> listar() {
        List<LugarProduccion> lista = new ArrayList<>();
        String sql = "SELECT lp.*, pr.nombre as nombre_productor, at.nombre as nombre_asistente " +
                     "FROM lugares_produccion lp " +
                     "LEFT JOIN productores prod_table ON lp.id_productor = prod_table.id " +
                     "LEFT JOIN usuarios pr ON prod_table.id = pr.id " +
                     "LEFT JOIN asistentes_tecnicos at_table ON lp.id_asistente_tecnico = at_table.id " +
                     "LEFT JOIN usuarios at ON at_table.id = at.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirLugarProduccion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los lugares de producción de un predio específico.
     */
    public List<LugarProduccion> listarPorPredio(String idPredio) {
        List<LugarProduccion> lista = new ArrayList<>();
        String sql = "SELECT lp.*, pr.nombre as nombre_productor, at.nombre as nombre_asistente " +
                     "FROM lugares_produccion lp " +
                     "LEFT JOIN productores prod_table ON lp.id_productor = prod_table.id " +
                     "LEFT JOIN usuarios pr ON prod_table.id = pr.id " +
                     "LEFT JOIN asistentes_tecnicos at_table ON lp.id_asistente_tecnico = at_table.id " +
                     "LEFT JOIN usuarios at ON at_table.id = at.id " +
                     "WHERE lp.id_predio = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPredio);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirLugarProduccion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los lugares de producción de un productor específico.
     */
    public List<LugarProduccion> listarPorProductor(String idProductor) {
        List<LugarProduccion> lista = new ArrayList<>();
        String sql = "SELECT lp.*, pr.nombre as nombre_productor, at.nombre as nombre_asistente " +
                     "FROM lugares_produccion lp " +
                     "LEFT JOIN productores prod_table ON lp.id_productor = prod_table.id " +
                     "LEFT JOIN usuarios pr ON prod_table.id = pr.id " +
                     "LEFT JOIN asistentes_tecnicos at_table ON lp.id_asistente_tecnico = at_table.id " +
                     "LEFT JOIN usuarios at ON at_table.id = at.id " +
                     "WHERE lp.id_productor = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idProductor);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirLugarProduccion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los lotes asociados a un lugar de producción.
     */
    public List<Lote> obtenerLotes(String idLugarProduccion) {
        List<Lote> lotes = new ArrayList<>();
        String sql = "SELECT * FROM lotes WHERE id_lugar_produccion = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idLugarProduccion);
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
     * Construye un objeto LugarProduccion a partir de un ResultSet.
     */
    private LugarProduccion construirLugarProduccion(ResultSet rs) throws SQLException {
        LugarProduccion lugar = new LugarProduccion();
        lugar.setId(rs.getString("id"));
        lugar.setCodigoIca(rs.getString("codigo_ica"));
        
        // Construir productor si existe
        if (rs.getString("id_productor") != null) {
            Productor productor = new Productor();
            productor.setId(rs.getString("id_productor"));
            productor.setNombre(rs.getString("nombre_productor"));
            lugar.setProductor(productor);
        }
        
        // Construir asistente técnico si existe
        if (rs.getString("id_asistente_tecnico") != null) {
            AsistenteTecnico asistente = new AsistenteTecnico();
            asistente.setId(rs.getString("id_asistente_tecnico"));
            asistente.setNombre(rs.getString("nombre_asistente"));
            lugar.setAsistenteTecnico(asistente);
        }
        
        return lugar;
    }
}
