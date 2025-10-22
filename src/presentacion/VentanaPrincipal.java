package presentacion;

import javax.swing.*;
import logica.*;

public class VentanaPrincipal extends JFrame {

    private JTabbedPane pestañas;
    private PanelPropietario panelPropietario;
    private PanelPredio panelPredio;
    private PanelInspeccion panelInspeccion;
    private PanelRegistroResultado panelRegistro;
    private PanelDepartamento panelDepartamento;
    private PanelMunicipio panelMunicipio;
    private PanelVereda panelVereda;
    private ControladorSistema controlador;

    public VentanaPrincipal() {
        super("Sistema de Inspecciones Fitosanitarias");

        controlador = new ControladorSistema();

        pestañas = new JTabbedPane();

        panelPropietario = new PanelPropietario(controlador);
        panelPredio = new PanelPredio(controlador);
        panelInspeccion = new PanelInspeccion(controlador);
        panelRegistro = new PanelRegistroResultado(controlador);
        panelDepartamento = new PanelDepartamento(controlador);
        panelMunicipio = new PanelMunicipio(controlador);
        panelVereda = new PanelVereda(controlador);

        // 🔹 Agregar todas las pestañas
        pestañas.addTab("Usuario", panelPropietario);
        pestañas.addTab("Departamentos", panelDepartamento);
        pestañas.addTab("Municipios", panelMunicipio);
        pestañas.addTab("Veredas", panelVereda);
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
