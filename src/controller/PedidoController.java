package controller;

import dao.*;
import data.ZonaDeCarga;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {
    public record ResumenEntrega(int entregados, int fallidos) { }
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;
    private volatile List<Pedido> pedidos = List.of();

    public PedidoController() {
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregaDAO = new EntregaDAO();
    }

    public synchronized void cargarPedidos() throws SQLException {
        pedidos = List.copyOf(pedidoDAO.listarTodos());
    }

    public List<Pedido> listarPedidos() throws SQLException {
        cargarPedidos();
        return pedidos;
    }

    public List<Pedido> listarPedidos(Estado estado, TipoPedido tipo) throws SQLException {
        return pedidoDAO.listarFiltrado(estado, tipo);
    }

    public List<Pedido> obtenerPedidos() { return pedidos;}

    public synchronized void agregarPedido(Pedido pedido) throws SQLException {
        pedidoDAO.crear(pedido);
        List<Pedido> nueva = new ArrayList<>(pedidos);
        nueva.add(pedido);
        pedidos = List.copyOf(nueva);
    }

    public synchronized void actualizarPedido(Pedido pedido) throws SQLException {
        pedidoDAO.actualizar(pedido);
        List<Pedido> nueva = new ArrayList<>(pedidos);
        for (int i = 0; i < nueva.size(); i++) {
            if (nueva.get(i).getIdPedido() == pedido.getIdPedido()) {
                nueva.set(i, pedido);
                break;
            }
        }
        pedidos = List.copyOf(nueva);
    }

    public synchronized void eliminarPedido(int idPedido) throws SQLException {
        pedidoDAO.eliminar(idPedido);
        List<Pedido> nueva = new ArrayList<>(pedidos);
        nueva.removeIf(p -> p.getIdPedido() == idPedido);
        pedidos = List.copyOf(nueva);
    }

    public synchronized void actualizarEstado(int idPedido, Estado estado) throws SQLException {
        pedidoDAO.actualizarEstado(idPedido, estado);
        List<Pedido> nueva = new ArrayList<>(pedidos);
        for (int i = 0; i < nueva.size(); i++) {
            Pedido actual = nueva.get(i);
            if (actual.getIdPedido() == idPedido) {
                nueva.set(i, new Pedido(idPedido, actual.getDireccionEntrega(), actual.getTipo(), estado));
                break;
            }
        }
        pedidos = List.copyOf(nueva);
    }

    public ResumenEntrega iniciarEntregas() throws InterruptedException, SQLException {
        List<Repartidor> registrados = repartidorDAO.listarTodos();
        if (registrados.isEmpty()) { throw new IllegalStateException("No hay repartidores registrados.");}
        List<Pedido> pendientes = listarPedidos().stream() .filter(p -> p.getEstado() == Estado.PENDIENTE).toList();
        if (pendientes.isEmpty()) { throw new IllegalStateException("No hay pedidos pendientes.");}
        ZonaDeCarga zona = new ZonaDeCarga();
        pendientes.forEach(zona::agregarPedido);
        List<Repartidor> equipo = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();
        for (Repartidor r : registrados) {
            Repartidor worker = new Repartidor(r.getIdRepartidor(), r.getNombreRepartidor(), zona, pedidoDAO, entregaDAO);
            Thread hilo = new Thread(worker, "repartidor-" + r.getIdRepartidor());
            equipo.add(worker);
            hilos.add(hilo);
            hilo.start();
        }
        try {
            for (Thread hilo : hilos) {
                hilo.join();
            }
        } catch (InterruptedException e) {
            hilos.forEach(Thread::interrupt);
            throw e;
        }
        cargarPedidos();
        int entregados = 0;
        int fallidos = 0;
        for (Repartidor worker : equipo) {
            entregados += worker.getEntregados();
            fallidos += worker.getFallidos();
        }
        return new ResumenEntrega(entregados, fallidos);
    }
}