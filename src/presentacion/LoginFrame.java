package presentacion;

import javax.swing.*;
import java.awt.event.*;
import logica.ControladorSistema;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        setLayout(null);

        JLabel lblTitulo = new JLabel("Login del Sistema");
        lblTitulo.setBounds(140, 20, 200, 30);
        add(lblTitulo);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(130, 100, 130, 30);
        add(btnLogin);

        JButton btnCrearCuenta = new JButton("Crear cuenta");
        btnCrearCuenta.setBounds(130, 150, 130, 30);
        add(btnCrearCuenta);

        // Acción del botón "Crear cuenta"
        btnCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirPanelPropietario();
            }
        });
    }

    private void abrirPanelPropietario() {
        // Cerrar login
        this.dispose();

    // Abrir PanelPropietario en una ventana aparte y centrada
    PanelPropietario panelPropietario = new PanelPropietario(new ControladorSistema());
        JFrame ventanaPropietario = new JFrame("Panel del Propietario");
        ventanaPropietario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaPropietario.getContentPane().add(panelPropietario);
        ventanaPropietario.pack();
        ventanaPropietario.setLocationRelativeTo(null); // Centrar en pantalla
        ventanaPropietario.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
