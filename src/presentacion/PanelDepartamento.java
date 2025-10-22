package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import logica.ControladorSistema;

public class PanelDepartamento extends JPanel {

    private JTextField txtNombre;
    private JButton btnGuardar, btnActualizar, btnBuscar, btnEliminar;
    private ControladorSistema controlador;

    private final String URL = "jdbc:oracle:thin:@192.168.2.100:1521:XE";
    private final String USUARIO = "ica2";
    private final String CLAVE = "ica2";

    public PanelDepartamento(ControladorSistema controlador) {
        this.controlador = controlador;
        setLayout(new GridLayout(3, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Departamentos"));

        add(new JLabel("Nombre del Departamento:"));
        txtNombre = new JTextField();
        add(txtNombre);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnBuscar = new JButton("Buscar");
        btnEliminar = new JButton("Eliminar");

        add(btnGuardar);
        add(btnActualizar);
        add(btnBuscar);
        add(btnEliminar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnBuscar.addActionListener(e -> buscar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    private Connection conectar() throws SQLException {
        try { Class.forName("oracle.jdbc.driver.OracleDriver"); }
        catch (ClassNotFoundException e) { JOptionPane.showMessageDialog(this, "Driver Oracle no encontrado."); }
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    private boolean existeDepartamento(String nombre) throws SQLException {
        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM DEPARTAMENTO WHERE LOWER(nombre)=LOWER(?)");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre del departamento."); return; }

        try (Connection conn = conectar()) {
            if (existeDepartamento(nombre)) {
                JOptionPane.showMessageDialog(this, "Ya existe un departamento con ese nombre.");
                return;
            }

            PreparedStatement psMax = conn.prepareStatement("SELECT NVL(MAX(id),0)+1 FROM DEPARTAMENTO");
            ResultSet rs = psMax.executeQuery(); rs.next();
            int id = rs.getInt(1);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO DEPARTAMENTO VALUES(?,?)");
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Departamento guardado correctamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE DEPARTAMENTO SET nombre=? WHERE LOWER(nombre)=LOWER(?)");
            ps.setString(1, nombre);
            ps.setString(2, nombre);
            int filas = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, filas > 0 ? "Departamento actualizado." : "No encontrado.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void buscar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM DEPARTAMENTO WHERE LOWER(nombre)=LOWER(?)");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                JOptionPane.showMessageDialog(this, "Departamento encontrado.");
            } else {
                JOptionPane.showMessageDialog(this, "No encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            // Verificar dependencias
            PreparedStatement psDep = conn.prepareStatement("SELECT COUNT(*) FROM MUNICIPIO WHERE id_departamento IN (SELECT id FROM DEPARTAMENTO WHERE LOWER(nombre)=LOWER(?))");
            psDep.setString(1, nombre);
            ResultSet rsDep = psDep.executeQuery(); rsDep.next();
            if (rsDep.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar: existen municipios asociados.");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("DELETE FROM DEPARTAMENTO WHERE LOWER(nombre)=LOWER(?)");
            ps.setString(1, nombre);
            int filas = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, filas > 0 ? "Departamento eliminado." : "No encontrado.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
