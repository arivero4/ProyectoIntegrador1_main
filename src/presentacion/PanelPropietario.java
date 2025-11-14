package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import dao.ConexionBD;
import logica.ControladorSistema;
import model.Propietario;

public class PanelPropietario extends JPanel {

    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtCorreo;
    private JTextField txtNumeroTarjeta;
    private JComboBox<String> cbRol;
    private JButton btnGuardar, btnActualizar, btnBuscar, btnEliminar;
    private boolean skipDatabase = false;
    private Consumer<Propietario> onSaveListener;
    private ControladorSistema controlador;

    public PanelPropietario(ControladorSistema controlador) {
        this.controlador = controlador;
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        setPreferredSize(new Dimension(600, 400));

        JLabel lblTitulo = new JLabel("Panel de Propietario");
        lblTitulo.setBounds(220, 10, 200, 30);
        add(lblTitulo);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(20, 60, 100, 25);
        add(lblRol);

        cbRol = new JComboBox<>(new String[] {"Propietario", "Asistente Tecnico"});
        cbRol.setBounds(130, 60, 200, 25);
        add(cbRol);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 100, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 100, 300, 25);
        add(txtNombre);

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setBounds(20, 140, 100, 25);
        add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setBounds(130, 140, 300, 25);
        add(txtDireccion);

        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setBounds(20, 180, 120, 25);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(150, 180, 280, 25);
        add(txtCorreo);

        JLabel lblTarjeta = new JLabel("Número Tarjeta Profesional:");
        lblTarjeta.setBounds(20, 220, 180, 25);
        add(lblTarjeta);

        txtNumeroTarjeta = new JTextField();
        txtNumeroTarjeta.setBounds(210, 220, 220, 25);
        txtNumeroTarjeta.setEnabled(false);
        add(txtNumeroTarjeta);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(50, 280, 100, 30);
        add(btnGuardar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(170, 280, 100, 30);
        add(btnActualizar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(290, 280, 100, 30);
        add(btnBuscar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(410, 280, 100, 30);
        add(btnEliminar);

        // Listeners
        cbRol.addActionListener(e -> {
            String seleccionado = (String) cbRol.getSelectedItem();
            boolean esAsistente = "Asistente Tecnico".equals(seleccionado);
            txtNumeroTarjeta.setEnabled(esAsistente);
            if (!esAsistente) txtNumeroTarjeta.setText("");
        });

        btnGuardar.addActionListener(e -> guardarRegistro());
        btnActualizar.addActionListener(e -> actualizarRegistro());
        btnBuscar.addActionListener(e -> buscarRegistro());
        btnEliminar.addActionListener(e -> eliminarRegistro());
    }

    public void setSkipDatabase(boolean skip) {
        this.skipDatabase = skip;
    }

    public void setNombre(String nombre) { if (txtNombre != null) txtNombre.setText(nombre); }
    public void setDireccion(String direccion) { if (txtDireccion != null) txtDireccion.setText(direccion); }
    public void setCorreo(String correo) { if (txtCorreo != null) txtCorreo.setText(correo); }
    public void setNumeroTarjeta(String tarjeta) { if (txtNumeroTarjeta != null) txtNumeroTarjeta.setText(tarjeta); }
    public void setRol(String rol) { if (cbRol != null) cbRol.setSelectedItem(rol); }

    public void setOnSaveListener(Consumer<Propietario> listener) { this.onSaveListener = listener; }

    // -------------------- CRUD (simplificado) --------------------
    private void guardarRegistro() {
        String rol = (String) cbRol.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String correo = txtCorreo.getText().trim();
        String numeroTarjeta = txtNumeroTarjeta.getText().trim();

        if (nombre.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.");
            return;
        }

        if (skipDatabase) {
            if (onSaveListener != null) {
                Propietario p = new Propietario();
                p.setNombre(nombre);
                p.setDireccion(direccion);
                p.setCorreoElectronico(correo);
                p.setRol(rol);
                onSaveListener.accept(p);
            }
            JOptionPane.showMessageDialog(this, "Registro preparado localmente (BD omitida en este modo).");
            return;
        }

        // Intentar guardar en la BD usando ConexionBD (si falla, notificar listener para permitir flujo)
        try (Connection conn = ConexionBD.getConexion()) {
            if (conn == null) throw new SQLException("No hay conexión a la BD");
            String tabla = rol.toUpperCase().replace(' ', '_');

            // Intento simple de insertar (si falla por esquema, se atrapará la excepción)
            String sql = "INSERT INTO " + tabla + " (id, nombre, rol, direccion, correo_electronico) VALUES ((SELECT NVL(MAX(id),0)+1 FROM " + tabla + "), ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, rol);
            ps.setString(3, direccion);
            ps.setString(4, correo);
            ps.executeUpdate();

            // Si es asistente técnico, intentar actualizar su número de tarjeta si la columna existe
            if ("Asistente Tecnico".equals(rol) && !numeroTarjeta.isEmpty()) {
                try {
                    String sql2 = "UPDATE " + tabla + " SET numero_tarjeta_profesional = ? WHERE nombre = ?";
                    PreparedStatement ps2 = conn.prepareStatement(sql2);
                    ps2.setString(1, numeroTarjeta);
                    ps2.setString(2, nombre);
                    ps2.executeUpdate();
                } catch (SQLException ex) {
                    // columna puede no existir, ignorar
                }
            }

            if (onSaveListener != null) {
                Propietario p = new Propietario();
                p.setNombre(nombre);
                p.setDireccion(direccion);
                p.setCorreoElectronico(correo);
                p.setRol(rol);
                onSaveListener.accept(p);
            }

            JOptionPane.showMessageDialog(this, "Registro guardado correctamente en la BD.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar en la BD: " + ex.getMessage() + "\nSe procederá en modo local.");
            if (onSaveListener != null) {
                Propietario p = new Propietario();
                p.setNombre(nombre);
                p.setDireccion(direccion);
                p.setCorreoElectronico(correo);
                p.setRol(rol);
                onSaveListener.accept(p);
            }
        }
    }

    private void actualizarRegistro() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de actualizar no implementada en este parche rápido.");
    }

    private void buscarRegistro() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de buscar no implementada en este parche rápido.");
    }

    private void eliminarRegistro() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de eliminar no implementada en este parche rápido.");
    }
}
