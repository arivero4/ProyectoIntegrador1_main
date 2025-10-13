
package presentacion;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import logica.ControladorSistema;
import model.ResultadoTecnico;
import java.util.List;

public class PanelRegistroResultado extends JPanel {
    private ControladorSistema controlador;

    private JTextField txtId, txtInspeccionId, txtDescripcion, txtFecha;
    private JButton btnRegistrar, btnConsultar, btnActualizar, btnLimpiar, btnListar;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;

    public PanelRegistroResultado(ControladorSistema controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Resultados Técnicos"));

        panelFormulario.add(new JLabel("ID Resultado:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("ID Inspección:"));
        txtInspeccionId = new JTextField();
        panelFormulario.add(txtInspeccionId);

        panelFormulario.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panelFormulario.add(txtDescripcion);

        panelFormulario.add(new JLabel("Fecha:"));
        txtFecha = new JTextField();
        panelFormulario.add(txtFecha);

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

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Inspección", "Descripción", "Fecha"}, 0);
        tablaResultados = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        add(scrollTabla, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarResultado());
        btnConsultar.addActionListener(e -> consultarResultado());
        btnActualizar.addActionListener(e -> actualizarResultado());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnListar.addActionListener(e -> listarResultados());
    }

    private void registrarResultado() {
        try {
            if (txtId.getText().trim().isEmpty() || txtInspeccionId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID y Inspección son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ResultadoTecnico r = new ResultadoTecnico();
            r.setId(txtId.getText().trim());
            r.setInspeccionId(txtInspeccionId.getText().trim());
            r.setDescripcion(txtDescripcion.getText().trim());
            r.setFecha(txtFecha.getText().trim());

            boolean exito = controlador.getGestorResultados().registrarResultado(r);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Resultado registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarResultados();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un resultado con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarResultado() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el ID del resultado a consultar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ResultadoTecnico r = controlador.getGestorResultados().consultarResultado(id);
            if (r != null) {
                txtInspeccionId.setText(r.getInspeccionId());
                txtDescripcion.setText(r.getDescripcion());
                txtFecha.setText(r.getFecha());
                JOptionPane.showMessageDialog(this, "Resultado encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el resultado", "No encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarResultado() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID es obligatorio para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ResultadoTecnico r = new ResultadoTecnico();
            r.setId(txtId.getText().trim());
            r.setInspeccionId(txtInspeccionId.getText().trim());
            r.setDescripcion(txtDescripcion.getText().trim());
            r.setFecha(txtFecha.getText().trim());

            boolean exito = controlador.getGestorResultados().actualizarResultado(r);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Resultado actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarResultados();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el resultado para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtInspeccionId.setText("");
        txtDescripcion.setText("");
        txtFecha.setText("");
        txtId.requestFocus();
    }

    private void listarResultados() {
        modeloTabla.setRowCount(0);
        List<ResultadoTecnico> lista = controlador.getGestorResultados().listarResultados();
        for (ResultadoTecnico r : lista) {
            modeloTabla.addRow(new Object[]{r.getId(), r.getInspeccionId(), r.getDescripcion(), r.getFecha()});
        }
    }
}
