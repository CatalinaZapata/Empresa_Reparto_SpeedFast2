package view;

import controller.PedidoController;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final PedidoController controller;
    private JButton btnEntrega;

    public VentanaPrincipal(PedidoController controller) {
        this.controller = controller;

        setTitle("SpeedFast - Sistema de Pedidos");
        setSize(500, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JButton btnRegistrar = new JButton("Registrar pedido");
        JButton btnListar = new JButton("Listar pedidos");
        JButton btnRepartidor = new JButton("Registrar repartidor");
        btnEntrega = new JButton("Asignar repartidor / Iniciar entrega");
        JButton btnCerrar = new JButton("Cerrar sesión");

        panel.add(btnRegistrar);
        panel.add(btnListar);
        panel.add(btnRepartidor);
        panel.add(btnEntrega);
        panel.add(btnCerrar);

        add(panel);

        btnRegistrar.addActionListener(e -> {
            setVisible(false);
            new VentanaRegistroPedido(controller, this);
        });

        btnListar.addActionListener(e -> {
            setVisible(false);
            new VentanaListaPedidos(controller, this);
        });

        btnRepartidor.addActionListener(e -> {
            setVisible(false);
            new VentanaRegistroRepartidor(controller, this);
        });

        btnEntrega.addActionListener(e -> iniciarEntregas());

        btnCerrar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }

    private void iniciarEntregas() {
        if (controller.obtenerPedidos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay pedidos registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean hayPendientes = controller.obtenerPedidos().stream().anyMatch(p -> p.getEstado() == model.Estado.PENDIENTE);

        if (!hayPendientes) {
            JOptionPane.showMessageDialog( this,"No hay pedidos pendientes.","Aviso",JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnEntrega.setEnabled(false);

        Thread hiloEntrega = new Thread(() -> {
            try {
                controller.iniciarEntregas();
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    btnEntrega.setEnabled(true);
                    new VentanaResultadoEntrega(this);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "La entrega fue interrumpida.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    btnEntrega.setEnabled(true);
                });
            }
        });
        hiloEntrega.start();
    }
}
