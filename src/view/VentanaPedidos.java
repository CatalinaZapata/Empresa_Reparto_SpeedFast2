package view;

import controller.PedidoController;
import model.*;
import util.Validaciones;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaPedidos extends VentanaCrud<Pedido> {
    private static final String TODOS = "Todos";
    private final PedidoController controller;
    private final JTextField txtId = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JComboBox<TipoPedido> comboTipo = new JComboBox<>(TipoPedido.values());
    private final JComboBox<Estado> comboEstado = new JComboBox<>(Estado.values());
    private final JComboBox<Object> filtroEstado = new JComboBox<>(conTodos(Estado.values()));
    private final JComboBox<Object> filtroTipo = new JComboBox<>(conTodos(TipoPedido.values()));

    public VentanaPedidos(PedidoController controller, VentanaPrincipal principal) {
        super("Gestión de Pedidos", principal, "ID", "Dirección", "Tipo", "Estado");
        this.controller = controller;
        iniciar();
    }

    private static Object[] conTodos(Object[] valores) {
        Object[] items = new Object[valores.length + 1];
        items[0] = TODOS;
        System.arraycopy(valores, 0, items, 1, valores.length);
        return items;
    }

    @Override
    protected JPanel crearFormulario() {
        txtId.setEditable(false);
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        p.add(new JLabel("ID:"));
        p.add(txtId);
        p.add(new JLabel("Dirección de entrega:"));
        p.add(txtDireccion);
        p.add(new JLabel("Tipo de pedido:"));
        p.add(comboTipo);
        p.add(new JLabel("Estado:"));
        p.add(comboEstado);
        return p;
    }

    @Override
    protected JPanel crearFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        p.add(new JLabel("Filtrar por estado:"));
        p.add(filtroEstado);
        p.add(new JLabel("Filtrar por tipo:"));
        p.add(filtroTipo);
        filtroEstado.addActionListener(e -> refrescar());
        filtroTipo.addActionListener(e -> refrescar());
        return p;
    }

    @Override
    protected List<Pedido> consultar() throws SQLException {
        Object estado = filtroEstado.getSelectedItem();
        Object tipo = filtroTipo.getSelectedItem();
        return controller.listarPedidos(estado instanceof Estado ? (Estado) estado : null, tipo instanceof TipoPedido ? (TipoPedido) tipo : null);
    }

    @Override
    protected Object[] fila(Pedido p) {
        return new Object[]{p.getIdPedido(), p.getDireccionEntrega(), p.getTipo(), p.getEstado()};
    }

    @Override
    protected int idDe(Pedido p) {
        return p.getIdPedido();
    }

    @Override
    protected String descripcion(Pedido p) {
        return "Pedido #" + p.getIdPedido();
    }

    @Override
    protected void mostrarEnFormulario(Pedido p) {
        txtId.setText(String.valueOf(p.getIdPedido()));
        txtDireccion.setText(p.getDireccionEntrega());
        comboTipo.setSelectedItem(p.getTipo());
        comboEstado.setSelectedItem(p.getEstado());
    }

    @Override
    protected void limpiarFormulario() {
        txtId.setText("");
        txtDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        comboEstado.setSelectedItem(Estado.PENDIENTE);
    }

    @Override
    protected Pedido leerFormulario(Pedido existente) {
        String direccion = txtDireccion.getText().trim();
        String error = Validaciones.errorTexto(direccion, "la dirección de entrega", 3, 150);
        if (error != null) return invalido(error, txtDireccion);
        return new Pedido(existente == null ? 0 : existente.getIdPedido(), direccion, (TipoPedido) comboTipo.getSelectedItem(), (Estado) comboEstado.getSelectedItem());
    }

    @Override
    protected void insertar(Pedido p) throws SQLException {
        controller.agregarPedido(p);
    }

    @Override
    protected void modificar(Pedido p) throws SQLException {
        controller.actualizarPedido(p);
    }

    @Override
    protected void borrar(Pedido p) throws SQLException {
        controller.eliminarPedido(p.getIdPedido());
    }
}