package view;

import controller.EntregaController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentanaEntregas extends VentanaCrud<Entrega> {
    private static final int TODOS = 0;
    private final EntregaController controller;
    private final JTextField txtId = new JTextField();
    private final JComboBox<ItemCombo> comboPedido = new JComboBox<>();
    private final JComboBox<ItemCombo> comboRepartidor = new JComboBox<>();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtHora = new JTextField();
    private final JComboBox<ItemCombo> filtroPedido = new JComboBox<>();
    private final JComboBox<ItemCombo> filtroRepartidor = new JComboBox<>();
    private final Map<Integer, String> textoPedidos = new HashMap<>();
    private final Map<Integer, String> textoRepartidores = new HashMap<>();
    private boolean cargandoCombos = false;

    public VentanaEntregas(EntregaController controller, VentanaPrincipal principal) {
        super("Gestión de Entregas", principal, "ID", "Pedido", "Repartidor", "Fecha", "Hora");
        this.controller = controller;
        iniciar();
    }

    @Override
    protected JPanel crearFormulario() {
        txtId.setEditable(false);
        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        p.add(new JLabel("ID:"));
        p.add(txtId);
        p.add(new JLabel("Pedido:"));
        p.add(comboPedido);
        p.add(new JLabel("Repartidor:"));
        p.add(comboRepartidor);
        p.add(new JLabel("Fecha (AAAA-MM-DD):"));
        p.add(txtFecha);
        p.add(new JLabel("Hora (HH:mm o HH:mm:ss):"));
        p.add(txtHora);
        return p;
    }

    @Override
    protected JPanel crearFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        p.add(new JLabel("Filtrar por pedido:"));
        p.add(filtroPedido);
        p.add(new JLabel("Filtrar por repartidor:"));
        p.add(filtroRepartidor);
        filtroPedido.addActionListener(e -> { if (!cargandoCombos) refrescar(); });
        filtroRepartidor.addActionListener(e -> { if (!cargandoCombos) refrescar(); });
        return p;
    }

    @Override
    protected void antesDeRefrescar() { cargarCombos(true);}

    @Override
    protected void alActivar() {
        cargarCombos(false);
    }

    private void cargarCombos(boolean avisarSiFalla) {
        cargandoCombos = true;
        try {
            List<Pedido> pedidos = controller.listarPedidos();
            List<Repartidor> repartidores = controller.listarRepartidores();

            textoPedidos.clear();
            List<ItemCombo> itemsPedido = pedidos.stream()
                    .map(p -> new ItemCombo(p.getIdPedido(), p.getIdPedido() + " - " + p.getDireccionEntrega()))
                    .toList();
            itemsPedido.forEach(i -> textoPedidos.put(i.id(), i.texto()));

            textoRepartidores.clear();
            List<ItemCombo> itemsRepartidor = repartidores.stream()
                    .map(r -> new ItemCombo(r.getIdRepartidor(), r.getIdRepartidor() + " - " + r.getNombreRepartidor()))
                    .toList();
            itemsRepartidor.forEach(i -> textoRepartidores.put(i.id(), i.texto()));

            llenar(comboPedido, itemsPedido, null);
            llenar(filtroPedido, itemsPedido, new ItemCombo(TODOS, "Todos los pedidos"));
            llenar(comboRepartidor, itemsRepartidor, null);
            llenar(filtroRepartidor, itemsRepartidor, new ItemCombo(TODOS, "Todos los repartidores"));
        } catch (SQLException e) {
            if (avisarSiFalla) {
                mostrarError("No fue posible cargar los pedidos y repartidores", e);
            } else {
                System.err.println("No fue posible refrescar los combos: " + e.getMessage());
            }
        } finally {
            cargandoCombos = false;
        }
    }

    private void llenar(JComboBox<ItemCombo> combo, List<ItemCombo> items, ItemCombo opcionTodos) {
        Integer idSeleccionado = idDelCombo(combo);
        DefaultComboBoxModel<ItemCombo> modelo = new DefaultComboBoxModel<>();
        if (opcionTodos != null) {
            modelo.addElement(opcionTodos);
        }
        items.forEach(modelo::addElement);
        combo.setModel(modelo);
        if (idSeleccionado != null) {
            seleccionarPorId(combo, idSeleccionado);
        }
    }

    private static Integer idDelCombo(JComboBox<ItemCombo> combo) {
        ItemCombo item = (ItemCombo) combo.getSelectedItem();
        return item == null ? null : item.id();
    }

    private static void seleccionarPorId(JComboBox<ItemCombo> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).id() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    protected List<Entrega> consultar() throws SQLException {
        Integer idPedido = idDelCombo(filtroPedido);
        Integer idRepartidor = idDelCombo(filtroRepartidor);
        return controller.listar(
                (idPedido == null || idPedido == TODOS) ? null : idPedido,
                (idRepartidor == null || idRepartidor == TODOS) ? null : idRepartidor);
    }

    @Override
    protected Object[] fila(Entrega e) {
        return new Object[]{
                e.getId(),
                textoPedidos.getOrDefault(e.getIdPedido(), "#" + e.getIdPedido()),
                textoRepartidores.getOrDefault(e.getIdRepartidor(), "#" + e.getIdRepartidor()),
                e.getFecha(),
                e.getHora()
        };
    }

    @Override
    protected int idDe(Entrega e) {
        return e.getId();
    }

    @Override
    protected String descripcion(Entrega e) {
        return "Entrega #" + e.getId();
    }

    @Override
    protected void mostrarEnFormulario(Entrega e) {
        txtId.setText(String.valueOf(e.getId()));
        seleccionarPorId(comboPedido, e.getIdPedido());
        seleccionarPorId(comboRepartidor, e.getIdRepartidor());
        txtFecha.setText(e.getFecha().toString());
        txtHora.setText(e.getHora().withNano(0).toString());
    }

    @Override
    protected void limpiarFormulario() {
        txtId.setText("");
        if (comboPedido.getItemCount() > 0) comboPedido.setSelectedIndex(0);
        if (comboRepartidor.getItemCount() > 0) comboRepartidor.setSelectedIndex(0);
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText(LocalTime.now().withNano(0).toString());
    }

    @Override
    protected Entrega leerFormulario(Entrega existente) {
        ItemCombo pedido = (ItemCombo) comboPedido.getSelectedItem();
        if (pedido == null) {
            return invalido("Debe seleccionar un pedido.\nSi la lista está vacía, registre un pedido primero.", comboPedido);
        }
        ItemCombo repartidor = (ItemCombo) comboRepartidor.getSelectedItem();
        if (repartidor == null) {
            return invalido("Debe seleccionar un repartidor.\nSi la lista está vacía, registre un repartidor primero.", comboRepartidor);
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(txtFecha.getText().trim());
        } catch (DateTimeParseException ex) {
            return invalido("Fecha inválida. Use el formato AAAA-MM-DD (por ejemplo, 2026-10-04).", txtFecha);
        }
        if (fecha.isAfter(LocalDate.now())) {
            return invalido("La fecha de la entrega no puede ser futura.", txtFecha);
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(txtHora.getText().trim());
        } catch (DateTimeParseException ex) {
            return invalido("Hora inválida. Use el formato HH:mm o HH:mm:ss (por ejemplo, 14:30).", txtHora);
        }

        return new Entrega(existente == null ? 0 : existente.getId(), pedido.id(), repartidor.id(), fecha, hora);
    }

    @Override
    protected void insertar(Entrega e) throws SQLException {
        controller.registrar(e);
    }

    @Override
    protected void modificar(Entrega e) throws SQLException {
        controller.actualizar(e);
    }

    @Override
    protected void borrar(Entrega e) throws SQLException {
        controller.eliminar(e.getId());
    }
}