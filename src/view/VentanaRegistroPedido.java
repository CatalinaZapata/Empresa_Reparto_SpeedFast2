package view;

import controller.PedidoController;
import model.*;
import dao.*;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {
    private final PedidoController controller;
    private JTextField txtId;
    private JTextField txtDireccion;
    private JComboBox <TipoPedido> comboTipo;
    private final VentanaPrincipal principal;

    public VentanaRegistroPedido(PedidoController controller, VentanaPrincipal principal) {
        this.controller = controller;
        this.principal = principal;
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { volverInicio();}
        });

        setTitle("Registro de Pedido");
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());
        JPanel formulario = new JPanel(new GridLayout(4, 2, 6, 6));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        formulario.add(txtId);

        formulario.add(new JLabel("Dirección: "));
        txtDireccion = new JTextField();
        formulario.add(txtDireccion);

        formulario.add(new JLabel("Tipo: "));
        comboTipo = new JComboBox<>(TipoPedido.values());
        formulario.add(comboTipo);

        JButton btnGuardar = new JButton("Guardar pedido");
        JButton btnCerrar = new JButton("Cerrar");
        //JButton btnVolver = new JButton("Volver");

        formulario.add(btnGuardar);
        formulario.add(btnCerrar);

        add(formulario, BorderLayout.CENTER);
        btnGuardar.addActionListener(e -> registrarPedido());
        btnCerrar.addActionListener(e -> volverInicio());
        //btnVolver.addActionListener(e -> volverInicio());

        setVisible(true);
    }

    private void registrarPedido(){
        try {
            String idTexto = txtId.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if(idTexto.isEmpty() || direccion.isEmpty()){
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idTexto);
            TipoPedido tipo = (TipoPedido) comboTipo.getSelectedItem();
            Pedido pedido = new Pedido(id, direccion, tipo, Estado.PENDIENTE);
            controller.agregarPedido(pedido);
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtId.setText("");
            txtDireccion.setText("");
            comboTipo.setSelectedIndex(0);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverInicio() {
        dispose();
        principal.setVisible(true);
    }
}
