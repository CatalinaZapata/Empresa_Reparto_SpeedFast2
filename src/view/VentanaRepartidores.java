package view;

import controller.RepartidorController;
import model.Repartidor;
import util.Validaciones;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaRepartidores extends VentanaCrud<Repartidor> {
    private final RepartidorController controller;
    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();

    public VentanaRepartidores(RepartidorController controller, VentanaPrincipal principal) {
        super("Gestión de Repartidores", principal, "ID", "Nombre");
        this.controller = controller;
        iniciar();
    }

    @Override
    protected JPanel crearFormulario() {
        txtId.setEditable(false);
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        p.add(new JLabel("ID:"));
        p.add(txtId);
        p.add(new JLabel("Nombre del repartidor:"));
        p.add(txtNombre);
        return p;
    }

    @Override
    protected List<Repartidor> consultar() throws SQLException { return controller.listar();}
    @Override
    protected Object[] fila(Repartidor r) { return new Object[]{r.getIdRepartidor(), r.getNombreRepartidor()};}
    @Override
    protected int idDe(Repartidor r) { return r.getIdRepartidor();}
    @Override
    protected String descripcion(Repartidor r) { return "Repartidor #" + r.getIdRepartidor();}

    @Override
    protected void mostrarEnFormulario(Repartidor r) {
        txtId.setText(String.valueOf(r.getIdRepartidor()));
        txtNombre.setText(r.getNombreRepartidor());
    }

    @Override
    protected void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
    }

    @Override
    protected Repartidor leerFormulario(Repartidor existente) {
        String nombre = txtNombre.getText().trim();
        String error = Validaciones.errorNombre(nombre, "el nombre del repartidor", 50);
        if (error != null) return invalido(error, txtNombre);
        return new Repartidor(existente == null ? 0 : existente.getIdRepartidor(), nombre);
    }

    @Override
    protected void insertar(Repartidor r) throws SQLException {
        controller.registrar(r);
    }

    @Override
    protected void modificar(Repartidor r) throws SQLException {
        controller.actualizar(r);
    }

    @Override
    protected void borrar(Repartidor r) throws SQLException {
        controller.eliminar(r.getIdRepartidor());
    }
}