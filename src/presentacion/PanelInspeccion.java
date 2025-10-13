
package presentacion;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import logica.ControladorSistema;
import model.InspeccionFitosanitaria;
import java.util.List;

public class PanelInspeccion extends JPanel {
    private ControladorSistema controlador;

    private JTextField txtId, txtFecha, txtPredio, txtTecnico, txtObservaciones;
    private JButton btnRegistrar, btnConsultar, btnActualizar, btnLimpiar, btnListar;
    private JTable tablaInspecciones;
    private DefaultTableModel modeloTabla;

    public PanelInspeccion(ControladorSistema controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Inspecciones"));

        panelFormulario.add(new JLabel("ID Inspección:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Fecha:"));
        txtFecha = new JTextField();
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("ID Predio:"));
        txtPredio = new JTextField();
        panelFormulario.add(txtPredio);

        panelFormulario.add(new JLabel("Técnico:"));
        txtTecnico = new JTextField();
        panelFormulario.add(txtTecnico);

        panelFormulario.add(new JLabel("Observaciones:"));
        txtObservaciones = new JTextField();
        panelFormulario.add(txtObservaciones);

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

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Fecha", "Predio", "Técnico", "Observaciones"}, 0);
        tablaInspecciones = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaInspecciones);
        add(scrollTabla, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarInspeccion());
        btnConsultar.addActionListener(e -> consultarInspeccion());
        btnActualizar.addActionListener(e -> actualizarInspeccion());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnListar.addActionListener(e -> listarInspecciones());
    }

    private void registrarInspeccion() {
        try {
            if (txtId.getText().trim().isEmpty() || txtFecha.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID y Fecha son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            InspeccionFitosanitaria i = new InspeccionFitosanitaria();
            i.setId(txtId.getText().trim());
            i.setFecha(txtFecha.getText().trim());
            i.setPredioId(txtPredio.getText().trim());
            i.setTecnico(txtTecnico.getText().trim());
            i.setObservaciones(txtObservaciones.getText().trim());

            boolean exito = controlador.getGestorInspecciones().registrarInspeccion(i);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Inspección registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarInspecciones();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe una inspección con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarInspeccion() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el ID de la inspección a consultar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            InspeccionFitosanitaria i = controlador.getGestorInspecciones().consultarInspeccion(id);
            if (i != null) {
                txtFecha.setText(i.getFecha());
                txtPredio.setText(i.getPredioId());
                txtTecnico.setText(i.getTecnico());
                txtObservaciones.setText(i.getObservaciones());
                JOptionPane.showMessageDialog(this, "Inspección encontrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la inspección", "No encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarInspeccion() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID es obligatorio para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            InspeccionFitosanitaria i = new InspeccionFitosanitaria();
            i.setId(txtId.getText().trim());
            i.setFecha(txtFecha.getText().trim());
            i.setPredioId(txtPredio.getText().trim());
            i.setTecnico(txtTecnico.getText().trim());
            i.setObservaciones(txtObservaciones.getText().trim());

            boolean exito = controlador.getGestorInspecciones().actualizarInspeccion(i);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Inspección actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarInspecciones();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la inspección para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtFecha.setText("");
        txtPredio.setText("");
        txtTecnico.setText("");
        txtObservaciones.setText("");
        txtId.requestFocus();
    }

    private void listarInspecciones() {
        modeloTabla.setRowCount(0);
        List<InspeccionFitosanitaria> lista = controlador.getGestorInspecciones().listarInspecciones();
        for (InspeccionFitosanitaria i : lista) {
            modeloTabla.addRow(new Object[]{i.getId(), i.getFecha(), i.getPredioId(), i.getTecnico(), i.getObservaciones()});
        }
    }
}
