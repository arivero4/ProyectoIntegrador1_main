package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import logica.ControladorSistema;

public class PanelVereda extends JPanel {

    private JTextField txtNombre;
    private JComboBox<String> cbMunicipio;
    private JButton btnGuardar, btnActualizar, btnBuscar, btnEliminar;
    private ControladorSistema controlador;

    private final String URL = "jdbc:oracle:thin:@192.168.2.100:1521:XE";
    private final String USUARIO = "ica2";
    private final String CLAVE = "ica2";

    public PanelVereda(ControladorSistema controlador) {
        this.controlador = controlador;
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Veredas"));

        add(new JLabel("Nombre de la Vereda:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Municipio:"));
        cbMunicipio = new JComboBox<>();
        add(cbMunicipio);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnBuscar = new JButton("Buscar");
        btnEliminar = new JButton("Eliminar");

        add(btnGuardar);
        add(btnActualizar);
        add(btnBuscar);
        add(btnEliminar);

        cargarMunicipios();

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

    private void cargarMunicipios() {
        cbMunicipio.removeAllItems();
        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT m.id, m.nombre, d.nombre as departamento " +
                "FROM MUNICIPIO m " +
                "LEFT JOIN DEPARTAMENTO d ON m.id_departamento = d.id " +
                "ORDER BY m.nombre");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String departamento = rs.getString("departamento");
                if (id != null && nombre != null) {
                    String displayText = id + " - " + nombre;
                    if (departamento != null) {
                        displayText += " (" + departamento + ")";
                    }
                    cbMunicipio.addItem(displayText);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando municipios: " + ex.getMessage());
        }
        // Mostrar mensaje si no hay municipios
        if (cbMunicipio.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay municipios registrados. Por favor, registre primero un municipio.");
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }
        if (cbMunicipio.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Seleccione un municipio."); return; }

        String idMunicipio = cbMunicipio.getSelectedItem().toString().split(" - ")[0];

        try (Connection conn = conectar()) {
            PreparedStatement psMax = conn.prepareStatement("SELECT NVL(MAX(TO_NUMBER(id)),0)+1 FROM VEREDA");
            ResultSet rs = psMax.executeQuery(); rs.next();
            String id = rs.getString(1);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO VEREDA VALUES(?,?,?)");
            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setString(3, idMunicipio);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Vereda guardada.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizar() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        String idMunicipio = cbMunicipio.getSelectedItem().toString().split(" - ")[0];

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE VEREDA SET id_municipio=? WHERE nombre=?");
            ps.setString(1, idMunicipio);
            ps.setString(2, nombre);
            int filas = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, filas > 0 ? "Vereda actualizada." : "No encontrada.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void buscar() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM VEREDA WHERE nombre=?");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                seleccionarMunicipio(rs.getString("id_municipio"));
                JOptionPane.showMessageDialog(this, "Vereda encontrada.");
            } else {
                JOptionPane.showMessageDialog(this, "No encontrada.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        String nombre = txtNombre.getText();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre."); return; }

        try (Connection conn = conectar()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM VEREDA WHERE nombre=?");
            ps.setString(1, nombre);
            int filas = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, filas > 0 ? "Eliminada correctamente." : "No encontrada.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void seleccionarMunicipio(String idMun) {
        for (int i = 0; i < cbMunicipio.getItemCount(); i++) {
            if (cbMunicipio.getItemAt(i).startsWith(idMun + " -")) {
                cbMunicipio.setSelectedIndex(i);
                break;
            }
        }
    }
}
