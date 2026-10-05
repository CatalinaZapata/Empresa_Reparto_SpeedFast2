package view;

import controller.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaPrincipal extends JFrame {
    private final PedidoController pedidoController;
    private final ClienteController clienteController;
    private final RepartidorController repartidorController;
    private final EntregaController entregaController;
    private final JButton btnReparto = new JButton("Iniciar reparto automático");

    public VentanaPrincipal(PedidoController pedidoController, ClienteController clienteController, RepartidorController repartidorController, EntregaController entregaController) {
        this.pedidoController = pedidoController;
        this.clienteController = clienteController;
        this.repartidorController = repartidorController;
        this.entregaController = entregaController;

        setTitle("SpeedFast - Sistema de Pedidos");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JButton btnClientes = new JButton("Gestionar clientes");
        JButton btnRepartidores = new JButton("Gestionar repartidores");
        JButton btnPedidos = new JButton("Gestionar pedidos");
        JButton btnEntregas = new JButton("Gestionar entregas");
        JButton btnCerrar = new JButton("Cerrar sesión");

        panel.add(btnClientes);
        panel.add(btnRepartidores);
        panel.add(btnPedidos);
        panel.add(btnEntregas);
        panel.add(btnReparto);
        panel.add(btnCerrar);
        add(panel);

        btnClientes.addActionListener(e -> abrir(() -> new VentanaClientes(clienteController, this)));
        btnRepartidores.addActionListener(e -> abrir(() -> new VentanaRepartidores(repartidorController, this)));
        btnPedidos.addActionListener(e -> abrir(() -> new VentanaPedidos(pedidoController, this)));
        btnEntregas.addActionListener(e -> abrir(() -> new VentanaEntregas(entregaController, this)));
        btnReparto.addActionListener(e -> iniciarReparto());

        btnCerrar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }

    private void abrir(Runnable crearVentana) {
        setVisible(false);
        crearVentana.run();
    }

    private void iniciarReparto() {
        btnReparto.setEnabled(false);

        Thread hiloReparto = new Thread(() -> {
            try {
                PedidoController.ResumenEntrega resumen = pedidoController.iniciarEntregas();
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    new VentanaResultadoEntrega(this, resumen.entregados(), resumen.fallidos());
                });
            } catch (IllegalStateException e) { // sin repartidores o sin pedidos pendientes
                avisar(e.getMessage(), JOptionPane.WARNING_MESSAGE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                avisar("El reparto fue interrumpido.", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                avisar("Error de base de datos:\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            } finally {
                SwingUtilities.invokeLater(() -> btnReparto.setEnabled(true));
            }
        }, "proceso-reparto");
        hiloReparto.start();
    }

    private void avisar(String mensaje, int tipo) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, mensaje, tipo == JOptionPane.ERROR_MESSAGE ? "Error" : "Aviso", tipo));
    }
}