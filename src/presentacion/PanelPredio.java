
package presentacion;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import logica.ControladorSistema;
import model.Predio;
import java.util.List;

public class PanelPredio extends JPanel {
    private ControladorSistema controlador;

    private JTextField txtId, txtCodigoIca, txtDireccion, txtArea, txtLatitud, txtLongitud;
    private JTextField txtIdPropietario, txtIdVereda, txtCodLugarProduccion;
    private JButton btnRegistrar, btnConsultar, btnActualizar, btnLimpiar, btnListar;
    private JTable tablaPredios;
    private DefaultTableModel modeloTabla;

    public PanelPredio(ControladorSistema controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Predios"));

        panelFormulario.add(new JLabel("ID Predio:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Código ICA:"));
        txtCodigoIca = new JTextField();
        panelFormulario.add(txtCodigoIca);

    panelFormulario.add(new JLabel("ID Propietario:"));
    txtIdPropietario = new JTextField();
    panelFormulario.add(txtIdPropietario);

        panelFormulario.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel("Área (ha):"));
        txtArea = new JTextField();
        panelFormulario.add(txtArea);

        panelFormulario.add(new JLabel("Latitud:"));
        txtLatitud = new JTextField();
        panelFormulario.add(txtLatitud);

        panelFormulario.add(new JLabel("Longitud:"));
        txtLongitud = new JTextField();
        panelFormulario.add(txtLongitud);

    panelFormulario.add(new JLabel("ID Vereda:"));
    txtIdVereda = new JTextField();
    panelFormulario.add(txtIdVereda);

    panelFormulario.add(new JLabel("Cod Lugar Producción:"));
    txtCodLugarProduccion = new JTextField();
    panelFormulario.add(txtCodLugarProduccion);

        btnRegistrar = new JButton("Registrar");
        btnConsultar = new JButton("Consultar");
        btnActualizar = new JButton("Actualizar");
        btnLimpiar = new JButton("Limpiar");
        btnListar = new JButton("Listar Todos");

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnConsultar);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnLimpiar);
        panelFormulario.add(btnListar);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla para mostrar los predios
    modeloTabla = new DefaultTableModel(new Object[]{"ID", "Código ICA", "Dirección", "Área", "Latitud", "Longitud", "ID_Propietario", "ID_Vereda", "Cod_Lugar_Produccion"}, 0);
        tablaPredios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPredios);
        add(scrollTabla, BorderLayout.CENTER);

        // Listeners
        btnRegistrar.addActionListener(e -> registrarPredio());
        btnConsultar.addActionListener(e -> consultarPredio());
        btnActualizar.addActionListener(e -> actualizarPredio());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnListar.addActionListener(e -> listarPredios());
    }

    private void registrarPredio() {
        try {
            if (txtId.getText().trim().isEmpty() || txtCodigoIca.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID y Código ICA son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Predio p = new Predio();
            p.setId(txtId.getText().trim());
            p.setCodigoIca(txtCodigoIca.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());
            p.setArea(Double.parseDouble(txtArea.getText().trim().isEmpty() ? "0" : txtArea.getText().trim()));
            p.setLatitud(Double.parseDouble(txtLatitud.getText().trim().isEmpty() ? "0" : txtLatitud.getText().trim()));
            p.setLongitud(Double.parseDouble(txtLongitud.getText().trim().isEmpty() ? "0" : txtLongitud.getText().trim()));
            p.setIdVereda(txtIdVereda.getText().trim());
            // asignar propietario por id si se proporcionó
            String idProp = txtIdPropietario.getText().trim();
            if (!idProp.isEmpty()) {
                model.Propietario owner = new model.Propietario();
                owner.setId(idProp);
                p.setPropietario(owner);
            }

            boolean exito = controlador.getGestorPredios().registrarPredio(p);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Predio registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarPredios();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un predio con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarPredio() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el ID del predio a consultar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Predio p = controlador.getGestorPredios().consultarPredio(id);
            if (p != null) {
                txtCodigoIca.setText(p.getCodigoIca());
                txtDireccion.setText(p.getDireccion());
                txtArea.setText(String.valueOf(p.getArea()));
                txtLatitud.setText(String.valueOf(p.getLatitud()));
                txtLongitud.setText(String.valueOf(p.getLongitud()));
                txtIdPropietario.setText(p.getPropietario() != null ? p.getPropietario().getId() : "");
                txtIdVereda.setText(p.getIdVereda());
                txtCodLugarProduccion.setText(p.getCodLugarProduccion());
                JOptionPane.showMessageDialog(this, "Predio encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el predio", "No encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPredio() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID es obligatorio para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Predio p = new Predio();
            p.setId(txtId.getText().trim());
            p.setCodigoIca(txtCodigoIca.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());
            p.setArea(Double.parseDouble(txtArea.getText().trim().isEmpty() ? "0" : txtArea.getText().trim()));
            p.setLatitud(Double.parseDouble(txtLatitud.getText().trim().isEmpty() ? "0" : txtLatitud.getText().trim()));
            p.setLongitud(Double.parseDouble(txtLongitud.getText().trim().isEmpty() ? "0" : txtLongitud.getText().trim()));
            p.setIdVereda(txtIdVereda.getText().trim());
            String idProp = txtIdPropietario.getText().trim();
            if (!idProp.isEmpty()) {
                model.Propietario owner = new model.Propietario();
                owner.setId(idProp);
                p.setPropietario(owner);
            }

            boolean exito = controlador.getGestorPredios().actualizarPredio(p);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Predio actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarPredios();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el predio para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
    txtId.setText("");
    txtCodigoIca.setText("");
    txtDireccion.setText("");
    txtArea.setText("");
    txtLatitud.setText("");
    txtLongitud.setText("");
    txtIdPropietario.setText("");
    txtIdVereda.setText("");
    txtCodLugarProduccion.setText("");
    txtId.requestFocus();
    }

    private void listarPredios() {
        modeloTabla.setRowCount(0);
        List<Predio> lista = controlador.getGestorPredios().listarPredios();
        for (Predio p : lista) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getCodigoIca(), p.getDireccion(), p.getArea(), p.getLatitud(), p.getLongitud(), p.getPropietario()!=null? p.getPropietario().getId():"", p.getIdVereda(), p.getCodLugarProduccion()});
        }
    }
}
