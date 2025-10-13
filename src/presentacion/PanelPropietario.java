package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import logica.*;
import model.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PanelPropietario extends JPanel {

    private JTextField txtNumeroId, txtNombres, txtTelefono, txtCorreo, txtCodigoICA, txtDireccion;
    private JComboBox<String> cmbTipoId;
    private JButton btnRegistrar, btnConsultar, btnActualizar, btnLimpiar;
    private ControladorSistema controlador;

    public PanelPropietario(ControladorSistema controlador) {
        this.controlador = controlador;
        setLayout(new GridLayout(8, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Propietarios"));

        // Tipo de Identificación
        add(new JLabel("Tipo de Identificación:"));
        cmbTipoId = new JComboBox<>(new String[]{"CC", "NIT", "CE", "Pasaporte"});
        add(cmbTipoId);

        // Número de Identificación
        add(new JLabel("Número de Identificación:"));
        txtNumeroId = new JTextField();
        add(txtNumeroId);

        // Nombres Completos
        add(new JLabel("Nombres Completos:"));
        txtNombres = new JTextField();
        add(txtNombres);


    // Teléfono de Contacto
    add(new JLabel("Teléfono de Contacto:"));
    txtTelefono = new JTextField();
    add(txtTelefono);

    // Dirección
    add(new JLabel("Dirección:"));
    txtDireccion = new JTextField();
    add(txtDireccion);

    // Correo Electrónico
    add(new JLabel("Correo Electrónico:"));
    txtCorreo = new JTextField();
    add(txtCorreo);

    // Código ICA Predio
    add(new JLabel("Código ICA Predio:"));
    txtCodigoICA = new JTextField();
    add(txtCodigoICA);

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnConsultar = new JButton("Consultar");
        btnActualizar = new JButton("Actualizar");
        btnLimpiar = new JButton("Limpiar");

        add(btnRegistrar);
        add(btnConsultar);
        add(btnActualizar);
        add(btnLimpiar);

        // Action Listeners
        btnRegistrar.addActionListener(e -> registrarPropietario());
        btnConsultar.addActionListener(e -> consultarPropietario());
        btnActualizar.addActionListener(e -> actualizarPropietario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void registrarPropietario() {
        try {
            // Validar campos obligatorios
            if (txtNumeroId.getText().trim().isEmpty() || txtNombres.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Los campos Número de Identificación y Nombres Completos son obligatorios", 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear objeto Propietario
            String idUsuario = txtNumeroId.getText().trim();
            // Validar longitud del id para coincidir con VARCHAR2(10)
            if (idUsuario.length() > 10) {
                JOptionPane.showMessageDialog(this,
                    "El número de identificación es demasiado largo (máx. 10 caracteres) para la columna ID en la base de datos.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            String rol = "Propietario";
            java.util.List<String> permisos = new ArrayList<>(Arrays.asList("acceso_predios", "editar_predios"));
            java.util.List<String> lugaresProduccion = new ArrayList<>();



            Propietario p = new Propietario(
                idUsuario,
                rol,
                permisos,
                (String) cmbTipoId.getSelectedItem(),
                txtNumeroId.getText().trim(),
                txtNombres.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtCodigoICA.getText().trim(),
                lugaresProduccion
            );
            p.setDireccion(txtDireccion.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());

            boolean exito = controlador.registrarPropietario(p);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Propietario registrado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar propietario. Verifique que no exista un propietario con la misma identificación.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void consultarPropietario() {
        try {
            String numeroId = txtNumeroId.getText().trim();
            
            if (numeroId.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Ingrese el número de identificación a consultar", 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Propietario p = controlador.consultarPropietario(numeroId);
            
            if (p != null) {
                // Llenar los campos con la información del propietario

                cmbTipoId.setSelectedItem(p.getTipoIdentificacion());
                txtNombres.setText(p.getNombresCompletos());
                txtTelefono.setText(p.getTelefonoContacto());
                txtDireccion.setText(p.getDireccion());
                txtCorreo.setText(p.getCorreoElectronico());
                txtCodigoICA.setText(p.getCodigoICAPredio());
                
                JOptionPane.showMessageDialog(this, 
                    "Propietario encontrado", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró el propietario con la identificación proporcionada", 
                    "No Encontrado", 
                    JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al consultar: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarPropietario() {
        try {
            // Validar campos obligatorios
            if (txtNumeroId.getText().trim().isEmpty() || txtNombres.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Los campos Número de Identificación y Nombres Completos son obligatorios", 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear objeto Propietario con los datos actualizados
            String idUsuario = txtNumeroId.getText().trim();
            // Validar longitud del id para coincidir con VARCHAR2(10)
            if (idUsuario.length() > 10) {
                JOptionPane.showMessageDialog(this,
                    "El número de identificación es demasiado largo (máx. 10 caracteres) para la columna ID en la base de datos.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            String rol = "Propietario";
            java.util.List<String> permisos = new ArrayList<>(Arrays.asList("acceso_predios", "editar_predios"));
            java.util.List<String> lugaresProduccion = new ArrayList<>();

            Propietario p = new Propietario(
                idUsuario,
                rol,
                permisos,
                (String) cmbTipoId.getSelectedItem(),
                txtNumeroId.getText().trim(),
                txtNombres.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtCodigoICA.getText().trim(),
                lugaresProduccion
            );

            boolean exito = controlador.actualizarPropietario(p);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Propietario actualizado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar propietario. Verifique que el propietario exista.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        cmbTipoId.setSelectedIndex(0);
    txtNumeroId.setText("");
    txtNombres.setText("");
    txtTelefono.setText("");
    txtDireccion.setText("");
    txtCorreo.setText("");
    txtCodigoICA.setText("");
    txtNumeroId.requestFocus();
    }
}
