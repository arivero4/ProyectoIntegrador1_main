package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import logica.ControladorSistema;

public class PanelPropietario extends JPanel {

    private JTextField txtNombre, txtDireccion, txtCorreo;
    private JTextField txtNumeroTarjeta;
    private JComboBox<String> cbRol;
    private JButton btnGuardar, btnActualizar, btnBuscar, btnEliminar;
    private ControladorSistema controlador; // referencia al controlador

    // Datos de conexión Oracle 
    private final String URL = "jdbc:oracle:thin:@192.168.2.100:1521:XE";
    private final String USUARIO = "ica2";
    private final String CLAVE = "ica2";

    // 🔹 Constructor 
    public PanelPropietario(ControladorSistema controlador) {
        this.controlador = controlador;
    setLayout(new GridLayout(7, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Usuarios"));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Rol:"));
    cbRol = new JComboBox<>(new String[]{"Propietario", "Productor", "Asistente Tecnico"});
        add(cbRol);

    // Campo adicional para Asistente Tecnico
    add(new JLabel("Número Tarjeta Profesional:"));
    JTextField txtNumeroTarjetaLocal = new JTextField();
    txtNumeroTarjetaLocal.setEnabled(false);
    add(txtNumeroTarjetaLocal);
    // make accessible to methods
    this.txtNumeroTarjeta = txtNumeroTarjetaLocal;

        add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        add(txtDireccion);

        add(new JLabel("Correo Electrónico:"));
        txtCorreo = new JTextField();
        add(txtCorreo);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnBuscar = new JButton("Buscar");
        btnEliminar = new JButton("Eliminar");

        add(btnGuardar);
        add(btnActualizar);
        add(btnBuscar);
        add(btnEliminar);

        // Eventos
        btnGuardar.addActionListener(e -> guardarRegistro());
        btnActualizar.addActionListener(e -> actualizarRegistro());
        btnBuscar.addActionListener(e -> buscarRegistro());
        btnEliminar.addActionListener(e -> eliminarRegistro());

        // Habilitar/deshabilitar el campo de tarjeta profesional según rol seleccionado
        cbRol.addActionListener(e -> {
            String seleccionado = (String) cbRol.getSelectedItem();
            if (seleccionado != null && seleccionado.equals("Asistente Tecnico")) {
                txtNumeroTarjeta.setEnabled(true);
            } else {
                txtNumeroTarjeta.setEnabled(false);
                txtNumeroTarjeta.setText("");
            }
        });
    }

    // -------------------- MÉTODOS DE CONEXIÓN Y CRUD --------------------
    private Connection conectar() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se encontró el driver JDBC de Oracle");
        }
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    private void guardarRegistro() {
        String rolSeleccionado = cbRol.getSelectedItem().toString();
        String tabla = rolSeleccionado.toUpperCase().replace(' ', '_');
        String nombre = txtNombre.getText();
        String rol = cbRol.getSelectedItem().toString();
        String direccion = txtDireccion.getText();
        String correo = txtCorreo.getText();
        String numeroTarjeta = txtNumeroTarjeta.getText();

        if (nombre.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
            return;
        }

        try (Connection conn = conectar()) {
            String sqlMax = "SELECT NVL(MAX(id),0)+1 FROM " + tabla;
            PreparedStatement psMax = conn.prepareStatement(sqlMax);
            ResultSet rs = psMax.executeQuery();
            int nuevoId = 1;
            if (rs.next()) nuevoId = rs.getInt(1);
            PreparedStatement ps;
            if (rolSeleccionado.equals("Asistente Tecnico")) {
                // Tabla ASISTENTE_TECNICO con columna NUMERO_TARJETA_PROFESIONAL
                String sqlInsert = "INSERT INTO " + tabla + " (id, nombre, rol, direccion, correo_electronico, numero_tarjeta_profesional) VALUES (?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sqlInsert);
                ps.setInt(1, nuevoId);
                ps.setString(2, nombre);
                ps.setString(3, rol);
                ps.setString(4, direccion);
                ps.setString(5, correo);
                ps.setString(6, numeroTarjeta);
            } else {
                String sqlInsert = "INSERT INTO " + tabla + " (id, nombre, rol, direccion, correo_electronico) VALUES (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sqlInsert);
                ps.setInt(1, nuevoId);
                ps.setString(2, nombre);
                ps.setString(3, rol);
                ps.setString(4, direccion);
                ps.setString(5, correo);
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro guardado exitosamente en tabla " + tabla + ".");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void actualizarRegistro() {
        String rolSeleccionado = cbRol.getSelectedItem().toString();
        String tabla = rolSeleccionado.toUpperCase().replace(' ', '_');
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String correo = txtCorreo.getText();
        String numeroTarjeta = txtNumeroTarjeta.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre para actualizar el registro.");
            return;
        }

        try (Connection conn = conectar()) {
            PreparedStatement ps;
            if (rolSeleccionado.equals("Asistente Tecnico")) {
                String sql = "UPDATE " + tabla + " SET direccion = ?, correo_electronico = ?, numero_tarjeta_profesional = ? WHERE nombre = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, direccion);
                ps.setString(2, correo);
                ps.setString(3, numeroTarjeta);
                ps.setString(4, nombre);
            } else {
                String sql = "UPDATE " + tabla + " SET direccion = ?, correo_electronico = ? WHERE nombre = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, direccion);
                ps.setString(2, correo);
                ps.setString(3, nombre);
            }

            int filas = ps.executeUpdate();
            if (filas > 0)
                JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
            else
                JOptionPane.showMessageDialog(this, "No se encontró el registro para actualizar.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void buscarRegistro() {
        String rolSeleccionado = cbRol.getSelectedItem().toString();
        String tabla = rolSeleccionado.toUpperCase().replace(' ', '_');
        String nombre = txtNombre.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre para buscar.");
            return;
        }

        try (Connection conn = conectar()) {
            String sql = "SELECT * FROM " + tabla + " WHERE nombre = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cbRol.setSelectedItem(rs.getString("rol"));
                txtDireccion.setText(rs.getString("direccion"));
                txtCorreo.setText(rs.getString("correo_electronico"));
                if (rolSeleccionado.equals("Asistente Tecnico")) {
                    try {
                        txtNumeroTarjeta.setText(rs.getString("numero_tarjeta_profesional"));
                        txtNumeroTarjeta.setEnabled(true);
                    } catch (SQLException sq) {
                        // columna no encontrada o null
                        txtNumeroTarjeta.setText("");
                        txtNumeroTarjeta.setEnabled(true);
                    }
                }
                JOptionPane.showMessageDialog(this, "Registro encontrado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el registro.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }

    private void eliminarRegistro() {
        String rolSeleccionado = cbRol.getSelectedItem().toString();
        String tabla = rolSeleccionado.toUpperCase().replace(' ', '_');
        String nombre = txtNombre.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre para eliminar.");
            return;
        }

        try (Connection conn = conectar()) {
            String sql = "DELETE FROM " + tabla + " WHERE nombre = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            int filas = ps.executeUpdate();

            if (filas > 0)
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
            else
                JOptionPane.showMessageDialog(this, "No se encontró el registro para eliminar.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}
