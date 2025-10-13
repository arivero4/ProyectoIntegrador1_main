package dao;

import model.Vereda;
import model.Municipio;
import model.Departamento;
import model.Predio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de veredas en la base de datos.
 * Extiende GenericDAO para heredar funcionalidad común.
 */
public class VeredaDAO extends GenericDAO {

    /**
     * Inserta una nueva vereda en la base de datos.
     */
    public boolean insertar(Vereda vereda) {
        String sql = "INSERT INTO vereda (id, nombre, id_municipio) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, vereda.getId());
            ps.setString(2, vereda.getNombre());
            ps.setString(3, vereda.getMunicipio() != null ? vereda.getMunicipio().getId() : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Actualiza los datos de una vereda existente.
     */
    public boolean actualizar(Vereda vereda) {
        String sql = "UPDATE vereda SET nombre = ?, id_municipio = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, vereda.getNombre());
            ps.setString(2, vereda.getMunicipio() != null ? vereda.getMunicipio().getId() : null);
            ps.setString(3, vereda.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    /**
     * Elimina una vereda de la base de datos.
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM vereda WHERE id = ?";
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
     * Busca una vereda por su ID.
     */
    public Vereda buscarPorId(String id) {
        String sql = "SELECT v.*, m.nombre as nombre_municipio, m.id_departamento, " +
                     "d.nombre as nombre_departamento " +
                     "FROM vereda v " +
                     "LEFT JOIN municipio m ON v.id_municipio = m.id " +
                     "LEFT JOIN departamento d ON m.id_departamento = d.id " +
                     "WHERE v.id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirVereda(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return null;
    }

    /**
     * Lista todas las veredas registradas.
     */
    public List<Vereda> listar() {
        List<Vereda> lista = new ArrayList<>();
        String sql = "SELECT v.*, m.nombre as nombre_municipio, m.id_departamento, " +
                     "d.nombre as nombre_departamento " +
                     "FROM vereda v " +
                     "LEFT JOIN municipio m ON v.id_municipio = m.id " +
                     "LEFT JOIN departamento d ON m.id_departamento = d.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirVereda(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Lista las veredas de un municipio específico.
     */
    public List<Vereda> listarPorMunicipio(String idMunicipio) {
        List<Vereda> lista = new ArrayList<>();
        String sql = "SELECT v.*, m.nombre as nombre_municipio, m.id_departamento, " +
                     "d.nombre as nombre_departamento " +
                     "FROM vereda v " +
                     "LEFT JOIN municipio m ON v.id_municipio = m.id " +
                     "LEFT JOIN departamento d ON m.id_departamento = d.id " +
                     "WHERE v.id_municipio = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idMunicipio);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirVereda(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return lista;
    }

    /**
     * Obtiene los predios asociados a una vereda.
     */
    public List<Predio> obtenerPredios(String idVereda) {
        List<Predio> predios = new ArrayList<>();
        String sql = "SELECT * FROM predios WHERE id_vereda = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, idVereda);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Predio predio = new Predio();
                predio.setId(rs.getString("id"));
                predio.setCodigoIca(rs.getString("codigo_ica"));
                predio.setDireccion(rs.getString("direccion"));
                predio.setArea(rs.getDouble("area"));
                predio.setLatitud(rs.getDouble("latitud"));
                predio.setLongitud(rs.getDouble("longitud"));
                predios.add(predio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(ps, rs);
        }
        return predios;
    }

    /**
     * Construye un objeto Vereda a partir de un ResultSet.
     */
    private Vereda construirVereda(ResultSet rs) throws SQLException {
        Vereda vereda = new Vereda();
        vereda.setId(rs.getString("id"));
        vereda.setNombre(rs.getString("nombre"));
        
        // Construir municipio si existe
        if (rs.getString("id_municipio") != null) {
            Municipio municipio = new Municipio();
            municipio.setId(rs.getString("id_municipio"));
            municipio.setNombre(rs.getString("nombre_municipio"));
            
            // Construir departamento si existe
            if (rs.getString("id_departamento") != null) {
                Departamento departamento = new Departamento();
                departamento.setId(rs.getString("id_departamento"));
                departamento.setNombre(rs.getString("nombre_departamento"));
                municipio.setDepartamento(departamento);
            }
            
            vereda.setMunicipio(municipio);
        }
        
        return vereda;
    }
}
