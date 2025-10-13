package dao;

import model.Departamento;
import model.Municipio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO extends GenericDAO {

    public void insertar(Departamento dpto) {
        String sql = "INSERT INTO departamento (id, nombre) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dpto.getId());
            ps.setString(2, dpto.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Departamento dpto) {
        String sql = "UPDATE departamento SET nombre = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dpto.getNombre());
            ps.setString(2, dpto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(String id) {
        String sql = "DELETE FROM departamento WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Departamento buscarPorId(String id) {
        String sql = "SELECT id, nombre FROM departamento WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Cargar municipios del departamento
                    List<Municipio> municipios = cargarMunicipiosPorDepartamento(id);
                    
                    return new Departamento(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        municipios
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Departamento> listar() {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM departamento";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Crear departamento sin cargar municipios para evitar carga excesiva
                lista.add(new Departamento(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Departamento> listarConMunicipios() {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM departamento";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String departamentoId = rs.getString("id");
                
                // Cargar municipios del departamento
                List<Municipio> municipios = cargarMunicipiosPorDepartamento(departamentoId);
                
                lista.add(new Departamento(
                    departamentoId,
                    rs.getString("nombre"),
                    municipios
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private List<Municipio> cargarMunicipiosPorDepartamento(String idDepartamento) {
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT id, nombre FROM municipio WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idDepartamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    municipios.add(new Municipio(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        null,  // No cargar el departamento para evitar referencia circular
                        null   // No cargar veredas aquí para evitar carga excesiva
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipios;
    }
}
