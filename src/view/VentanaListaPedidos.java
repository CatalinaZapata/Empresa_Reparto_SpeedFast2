package view;

import controller.PedidoController;
import model.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaPedidos extends JFrame {
    private final PedidoController controller;
    private JTable tabla;
    private DefaultTableModel modelo;
    private final VentanaPrincipal principal;

    public VentanaListaPedidos(PedidoController controller, VentanaPrincipal principal) {
        this.controller = controller;
        this.principal = principal;
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { volverInicio();}
        });

        setTitle("Gestión de Pedidos");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};

        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        //JPanel panelBotones = new JPanel();

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 10));
        botones.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        botones.add(btnActualizar);
        botones.add(btnCerrar);
        add(botones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarPedidos());
        btnCerrar.addActionListener(e -> volverInicio());
        cargarPedidos();
        setVisible(true);
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);

        for(Pedido pedido : controller.obtenerPedidos()) {
            Object[] fila = {
                    pedido.getIdPedido(),
                    pedido.getDireccionEntrega(),
                    pedido.getTipo(),
                    pedido.getEstado()
            };
            modelo.addRow(fila);
        }
    }

    private void volverInicio() {
        dispose();
        principal.setVisible(true);
    }
}
