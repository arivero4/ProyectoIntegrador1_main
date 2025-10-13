package dao;

import model.Municipio;
import model.Departamento;
import model.Vereda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MunicipioDAO extends GenericDAO {

    public void insertar(Municipio m) {
        String sql = "INSERT INTO municipio (id, nombre, id_departamento) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, m.getId());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getDepartamento().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Municipio m) {
        String sql = "UPDATE municipio SET nombre = ?, id_departamento = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getDepartamento().getId());
            ps.setString(3, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(String id) {
        String sql = "DELETE FROM municipio WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Municipio buscarPorId(String id) {
        String sql = "SELECT m.id, m.nombre, m.id_departamento, d.nombre as nombre_departamento " +
                     "FROM municipio m " +
                     "LEFT JOIN departamento d ON m.id_departamento = d.id " +
                     "WHERE m.id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Crear departamento si existe
                    Departamento departamento = null;
                    if (rs.getString("id_departamento") != null) {
                        departamento = new Departamento(
                            rs.getString("id_departamento"),
                            rs.getString("nombre_departamento"),
                            null
                        );
                    }
                    
                    // Cargar veredas del municipio
                    List<Vereda> veredas = cargarVeredasPorMunicipio(id);
                    
                    return new Municipio(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        departamento,
                        veredas
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Municipio> listar() {
        List<Municipio> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.nombre, m.id_departamento, d.nombre as nombre_departamento " +
                     "FROM municipio m " +
                     "LEFT JOIN departamento d ON m.id_departamento = d.id";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Crear departamento si existe
                Departamento departamento = null;
                if (rs.getString("id_departamento") != null) {
                    departamento = new Departamento(
                        rs.getString("id_departamento"),
                        rs.getString("nombre_departamento"),
                        null
                    );
                }
                
                // Cargar veredas del municipio
                String municipioId = rs.getString("id");
                List<Vereda> veredas = cargarVeredasPorMunicipio(municipioId);
                
                lista.add(new Municipio(
                    municipioId,
                    rs.getString("nombre"),
                    departamento,
                    veredas
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Municipio> listarPorDepartamento(String idDepartamento) {
        List<Municipio> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.nombre, m.id_departamento, d.nombre as nombre_departamento " +
                     "FROM municipio m " +
                     "LEFT JOIN departamento d ON m.id_departamento = d.id " +
                     "WHERE m.id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idDepartamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear departamento
                    Departamento departamento = new Departamento(
                        rs.getString("id_departamento"),
                        rs.getString("nombre_departamento"),
                        null
                    );
                    
                    // Cargar veredas del municipio
                    String municipioId = rs.getString("id");
                    List<Vereda> veredas = cargarVeredasPorMunicipio(municipioId);
                    
                    lista.add(new Municipio(
                        municipioId,
                        rs.getString("nombre"),
                        departamento,
                        veredas
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private List<Vereda> cargarVeredasPorMunicipio(String idMunicipio) {
        List<Vereda> veredas = new ArrayList<>();
        String sql = "SELECT id, nombre FROM vereda WHERE id_municipio = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idMunicipio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    veredas.add(new Vereda(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        null,  // No cargar el municipio para evitar referencia circular
                        null   // No cargar predios aquí para evitar carga excesiva
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veredas;
    }
}
