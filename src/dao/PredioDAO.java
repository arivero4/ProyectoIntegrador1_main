package dao;

import model.Predio;
import model.Propietario;
import model.Vereda;
import model.LugarProduccion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de predios en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class PredioDAO extends GenericDAO {

    /**
     * Inserta un nuevo predio en la base de datos.
     */
    public boolean insertar(Predio predio) {
    String sql = "INSERT INTO predios (id, codigo_ica, direccion, area, latitud, longitud, " +
             "id_propietario, id_vereda, cod_lugar_produccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, predio.getId());
            ps.setString(2, predio.getCodigoIca());
            ps.setString(3, predio.getDireccion());
            ps.setDouble(4, predio.getArea());
            ps.setDouble(5, predio.getLatitud());
            ps.setDouble(6, predio.getLongitud());
            ps.setString(7, predio.getPropietario() != null ? predio.getPropietario().getId() : null);
            ps.setString(8, predio.getIdVereda());
            ps.setString(9, predio.getCodLugarProduccion());
            System.out.println("[DEBUG] PredioDAO.insertar params: id=" + predio.getId() + ", codigo_ica=" + predio.getCodigoIca() + ", id_propietario=" + (predio.getPropietario()!=null?predio.getPropietario().getId():"null") + ", id_vereda=" + predio.getIdVereda() + ", cod_lugar_produccion=" + predio.getCodLugarProduccion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de un predio existente.
     */
    public boolean actualizar(Predio predio) {
    String sql = "UPDATE predios SET codigo_ica = ?, direccion = ?, area = ?, " +
             "latitud = ?, longitud = ?, id_propietario = ?, id_vereda = ?, cod_lugar_produccion = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, predio.getCodigoIca());
            ps.setString(2, predio.getDireccion());
            ps.setDouble(3, predio.getArea());
            ps.setDouble(4, predio.getLatitud());
            ps.setDouble(5, predio.getLongitud());
            ps.setString(6, predio.getPropietario() != null ? predio.getPropietario().getId() : null);
            ps.setString(7, predio.getIdVereda());
            ps.setString(8, predio.getCodLugarProduccion());
            ps.setString(9, predio.getId());
            System.out.println("[DEBUG] PredioDAO.actualizar params: id=" + predio.getId() + ", codigo_ica=" + predio.getCodigoIca());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina un predio de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM predios WHERE id = ?";
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
     * Busca un predio por su ID.
     */
    public Predio buscarPorId(String id) {
        String sql = "SELECT p.*, prop.nombre as nombre_propietario " +
                     "FROM predios p " +
                     "LEFT JOIN propietarios prop_table ON p.id_propietario = prop_table.id " +
                     "LEFT JOIN usuarios prop ON prop_table.id = prop.id " +
                     "WHERE p.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPredio(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Busca un predio por su código ICA.
     */
    public Predio buscarPorCodigoIca(String codigoIca) {
        String sql = "SELECT p.*, prop.nombre as nombre_propietario " +
                     "FROM predios p " +
                     "LEFT JOIN propietarios prop_table ON p.id_propietario = prop_table.id " +
                     "LEFT JOIN usuarios prop ON prop_table.id = prop.id " +
                     "WHERE p.codigo_ica = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoIca);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPredio(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todos los predios registrados.
     */
    public List<Predio> listar() {
        List<Predio> lista = new ArrayList<>();
        String sql = "SELECT p.*, prop.nombre as nombre_propietario " +
                     "FROM predios p " +
                     "LEFT JOIN propietarios prop_table ON p.id_propietario = prop_table.id " +
                     "LEFT JOIN usuarios prop ON prop_table.id = prop.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPredio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista los predios de un propietario específico.
     */
    public List<Predio> listarPorPropietario(String idPropietario) {
        List<Predio> lista = new ArrayList<>();
        String sql = "SELECT p.*, prop.nombre as nombre_propietario " +
                     "FROM predios p " +
                     "LEFT JOIN propietarios prop_table ON p.id_propietario = prop_table.id " +
                     "LEFT JOIN usuarios prop ON prop_table.id = prop.id " +
                     "WHERE p.id_propietario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPropietario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPredio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los lugares de producción asociados a un predio.
     */
    public List<LugarProduccion> obtenerLugaresProduccion(String idPredio) {
        List<LugarProduccion> lugares = new ArrayList<>();
        String sql = "SELECT * FROM lugares_produccion WHERE id_predio = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idPredio);
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
     * Construye un objeto Predio a partir de un ResultSet.
     */
    private Predio construirPredio(ResultSet rs) throws SQLException {
        Predio predio = new Predio();
        predio.setId(rs.getString("id"));
        predio.setCodigoIca(rs.getString("codigo_ica"));
        predio.setDireccion(rs.getString("direccion"));
        predio.setArea(rs.getDouble("area"));
        predio.setLatitud(rs.getDouble("latitud"));
        predio.setLongitud(rs.getDouble("longitud"));
        try {
            predio.setIdVereda(rs.getString("id_vereda"));
        } catch (SQLException ex) {
            // ignore if column missing
        }
        try {
            predio.setCodLugarProduccion(rs.getString("cod_lugar_produccion"));
        } catch (SQLException ex) {
            // ignore if column missing
        }
        
        // Construir propietario si existe
        if (rs.getString("id_propietario") != null) {
            Propietario propietario = new Propietario();
            propietario.setId(rs.getString("id_propietario"));
            propietario.setNombre(rs.getString("nombre_propietario"));
            predio.setPropietario(propietario);
        }
        
        return predio;
    }
}
