package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import logica.*;

public class VentanaPrincipal extends JFrame {

    private JTabbedPane pestañas;
    private PanelPropietario panelPropietario;
    private PanelPredio panelPredio;
    private PanelInspeccion panelInspeccion;
    private PanelRegistroResultado panelRegistro;
    private ControladorSistema controlador;

    public VentanaPrincipal() {
        super("Sistema de Inspecciones Fitosanitarias");

        controlador = new ControladorSistema();

        pestañas = new JTabbedPane();

        panelPropietario = new PanelPropietario(controlador);
        panelPredio = new PanelPredio(controlador);
        panelInspeccion = new PanelInspeccion(controlador);
        panelRegistro = new PanelRegistroResultado(controlador);

        pestañas.addTab("Usuario", panelPropietario);
        pestañas.addTab("Predios", panelPredio);
        pestañas.addTab("Inspecciones", panelInspeccion);
        pestañas.addTab("Registros", panelRegistro);

        add(pestañas);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
