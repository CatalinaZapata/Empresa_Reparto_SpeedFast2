package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaResultadoEntrega extends JFrame{
    private final VentanaPrincipal principal;

    public VentanaResultadoEntrega(VentanaPrincipal principal, int entregados, int fallidos) {
        this.principal = principal;

        setTitle("Entrega finalizada");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(25, 30, 25, 30));
        JLabel titulo = new JLabel("Proceso de entrega finalizado", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        String detalle = "Pedidos entregados: " + entregados;
        if (fallidos > 0) {
            detalle += "<br>Pedidos con error (siguen PENDIENTE): " + fallidos + "<br>Revise la consola para ver el detalle.";
        }
        JLabel mensaje = new JLabel("<html><div style='text-align: center;'>" + detalle + "</div></html>", SwingConstants.CENTER);
        mensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(mensaje, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al menú principal");
        btnVolver.setPreferredSize(new Dimension(230, 35));
        btnVolver.addActionListener(e -> volverInicio());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVolver);

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);
        add(panelPrincipal);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                volverInicio();
            }
        });
        setVisible(true);
    }

    private void volverInicio() {
        dispose();
        principal.setVisible(true);
    }
}