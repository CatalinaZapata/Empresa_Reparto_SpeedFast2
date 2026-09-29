package view;

import controller.PedidoController;
import dao.RepartidorDAO;
import model.Repartidor;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaRegistroRepartidor extends JFrame {
    private final PedidoController controller;
    private JTextField txtNombre;
    private final VentanaPrincipal principal;

    public VentanaRegistroRepartidor(PedidoController controller, VentanaPrincipal principal) {
        this.controller = controller;
        this.principal = principal;
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { volverInicio();}
        });

        setTitle("Registro de Repartidor");
        setSize(400, 130);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formulario.add(new JLabel("Nombre:"));

        txtNombre = new JTextField();
        formulario.add(txtNombre);

        JButton btnGuardar = new JButton("Guardar repartidor");
        JButton btnCerrar = new JButton("Cerrar");

        formulario.add(btnGuardar);
        formulario.add(btnCerrar);

        add(formulario, BorderLayout.CENTER);
        btnGuardar.addActionListener(e -> guardarRepartidor());
        btnCerrar.addActionListener(e -> volverInicio());
        setVisible(true);
    }

    private void guardarRepartidor() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre del repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Repartidor repartidor = new Repartidor(nombre, null);
            RepartidorDAO dao = new RepartidorDAO();
            dao.guardar(repartidor);
            JOptionPane.showMessageDialog(this, "Repartidor registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtNombre.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,"Error al registrar el repartidor:\n" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void volverInicio() {
        dispose();
        principal.setVisible(true);
    }
}