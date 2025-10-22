package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import logica.ControladorSistema;

public class PanelMunicipio extends JPanel {

    private JTextField txtNombre;
    private JComboBox<String> cbDepartamento;
    private JButton btnGuardar, btnActualizar, btnBuscar, btnEliminar;
    private ControladorSistema controlador;

    private final String URL = "jdbc:oracle:thin:@192.168.2.100:1521:XE";
    private final String USUARIO = "ica2";
    private final String CLAVE = "ica2";

    public PanelMunicipio(ControladorSistema controlador) {
        this.controlador = controlador;
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Municipios"));

        add(new JLabel("Nombre del Municipio:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Departamento:"));
        cbDepartamento = new JComboBox<>();
        add(cbDepartamento);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnBuscar = new JButton("Buscar");
        btnEliminar = new JButton("Eliminar");

        add(btnGuardar);
        add(btnActualizar);
        add(btnBuscar);
        add(btnEliminar);

        cargarDepartamentos();

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

    private void cargarDepartamentos() {
        cbDepartamento.removeAllItems();
        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id, nombre FROM DEPARTAMENTO ORDER BY nombre");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                if (id != null && nombre != null) {
                    cbDepartamento.addItem(id + " - " + nombre);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando departamentos: " + ex.getMessage());
        }
        // Si no hay departamentos, mostrar mensaje
        if (cbDepartamento.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay departamentos registrados. Por favor, registre primero un departamento.");
        }
    }

    private boolean existeMunicipio(String nombre) throws SQLException {
        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM MUNICIPIO WHERE LOWER(nombre)=LOWER(?)");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }
        if (cbDepartamento.getItemCount() == 0) { JOptionPane.showMessageDialog(this, "Debe registrar al menos un departamento antes."); return; }

        if (cbDepartamento.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un departamento.");
            return;
        }
        
        String idDepartamento = cbDepartamento.getSelectedItem().toString().split(" - ")[0];

        try (Connection conn = conectar()) {
            if (existeMunicipio(nombre)) {
                JOptionPane.showMessageDialog(this, "Ya existe un municipio con ese nombre.");
                return;
            }

            PreparedStatement psMax = conn.prepareStatement("SELECT NVL(MAX(id),0)+1 FROM MUNICIPIO");
            ResultSet rs = psMax.executeQuery(); rs.next();
            int id = rs.getInt(1);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO MUNICIPIO VALUES(?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, idDepartamento);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Municipio guardado correctamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            PreparedStatement psDep = conn.prepareStatement("SELECT COUNT(*) FROM VEREDA WHERE id_municipio IN (SELECT id FROM MUNICIPIO WHERE LOWER(nombre)=LOWER(?))");
            psDep.setString(1, nombre);
            ResultSet rsDep = psDep.executeQuery(); rsDep.next();
            if (rsDep.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar: existen veredas asociadas.");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("DELETE FROM MUNICIPIO WHERE LOWER(nombre)=LOWER(?)");
            ps.setString(1, nombre);
            int filas = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, filas > 0 ? "Municipio eliminado." : "No encontrado.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizar() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        if (cbDepartamento.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un departamento.");
            return;
        }
        
        String idDepartamento = cbDepartamento.getSelectedItem().toString().split(" - ")[0];

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE MUNICIPIO SET id_departamento=? WHERE nombre=?");
            ps.setString(1, idDepartamento);
            ps.setString(2, nombre);
            int filas = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, filas > 0 ? "Municipio actualizado." : "No encontrado.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void buscar() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM MUNICIPIO WHERE nombre=?");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idDep = rs.getInt("id_departamento");
                txtNombre.setText(rs.getString("nombre"));
                seleccionarDepartamento(idDep);
                JOptionPane.showMessageDialog(this, "Municipio encontrado.");
            } else {
                JOptionPane.showMessageDialog(this, "No encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private void seleccionarDepartamento(int idDep) {
        for (int i = 0; i < cbDepartamento.getItemCount(); i++) {
            if (cbDepartamento.getItemAt(i).startsWith(idDep + " -")) {
                cbDepartamento.setSelectedIndex(i);
                break;
            }
        }
    }
}
