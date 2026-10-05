package view;

import controller.ClienteController;
import model.Cliente;
import util.Validaciones;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaClientes extends VentanaCrud<Cliente> {
    private final ClienteController controller;
    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();

    public VentanaClientes(ClienteController controller, VentanaPrincipal principal) {
        super("Gestión de Clientes", principal, "ID", "Nombre", "Teléfono", "Correo");
        this.controller = controller;
        iniciar();
    }

    @Override
    protected JPanel crearFormulario() {
        txtId.setEditable(false);
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        p.add(new JLabel("ID:"));
        p.add(txtId);
        p.add(new JLabel("Nombre:"));
        p.add(txtNombre);
        p.add(new JLabel("Teléfono:"));
        p.add(txtTelefono);
        p.add(new JLabel("Correo electrónico:"));
        p.add(txtCorreo);
        return p;
    }

    @Override
    protected List<Cliente> consultar() throws SQLException { return controller.listar();}
    @Override
    protected Object[] fila(Cliente c) { return new Object[]{c.getIdCliente(), c.getNombreCliente(), c.getTelefono(), c.getCorreo()};}
    @Override
    protected int idDe(Cliente c) { return c.getIdCliente();}
    @Override
    protected String descripcion(Cliente c) { return "Cliente #" + c.getIdCliente();}

    @Override
    protected void mostrarEnFormulario(Cliente c) {
        txtId.setText(String.valueOf(c.getIdCliente()));
        txtNombre.setText(c.getNombreCliente());
        txtTelefono.setText(c.getTelefono());
        txtCorreo.setText(c.getCorreo());
    }

    @Override
    protected void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
    }

    @Override
    protected Cliente leerFormulario(Cliente existente) {
        String nombre = txtNombre.getText().trim();
        String error = Validaciones.errorNombre(nombre, "el nombre del cliente", 50);
        if (error != null) return invalido(error, txtNombre);

        error = Validaciones.errorTelefono(txtTelefono.getText());
        if (error != null) return invalido(error, txtTelefono);

        String correo = txtCorreo.getText().trim();
        error = Validaciones.errorCorreo(correo, 100);
        if (error != null) return invalido(error, txtCorreo);

        int id = existente == null ? 0 : existente.getIdCliente();
        return new Cliente(id, nombre, Validaciones.normalizarTelefono(txtTelefono.getText()), correo);
    }

    @Override
    protected void insertar(Cliente c) throws SQLException {
        controller.registrar(c);
    }

    @Override
    protected void modificar(Cliente c) throws SQLException {
        controller.actualizar(c);
    }

    @Override
    protected void borrar(Cliente c) throws SQLException {
        controller.eliminar(c.getIdCliente());
    }
}