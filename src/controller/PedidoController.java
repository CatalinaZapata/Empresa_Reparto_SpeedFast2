package controller;

import dao.PedidoDAO;
import data.ZonaDeCarga;
import model.*;
import dao.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {
    private final List<Pedido> pedidos;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;

    public PedidoController() {
        pedidos = new ArrayList<>();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregaDAO = new EntregaDAO();
        cargarPedidos();
    }

    private void cargarPedidos() {
        try {
            pedidos.addAll(pedidoDAO.listarTodos());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarPedido(Pedido pedido){
        pedidos.add(pedido);

        try {
            pedidoDAO.guardar(pedido);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> obtenerPedidos(){
        return pedidos;
    }

    public void iniciarEntregas() throws InterruptedException {
        ZonaDeCarga z = new ZonaDeCarga();

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == Estado.PENDIENTE) {
                z.agregarPedido(pedido);
            }
        }

        List<Repartidor> repartidores;

        try {
            repartidores = repartidorDAO.listarTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (repartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }

        List<Thread> hilos = new ArrayList<>();

        for (Repartidor repartidor : repartidores) {
            Thread hilo = new Thread(new Repartidor(repartidor.getIdRepartidor(), repartidor.getNombreRepartidor(), z));
            hilos.add(hilo);
            hilo.start();
        }

        for (Thread hilo : hilos) {
            hilo.join();
        }
    }
}
