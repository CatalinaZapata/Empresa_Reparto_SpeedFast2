package model;

import data.ZonaDeCarga;
import dao.EntregaDAO;
import dao.PedidoDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Repartidor implements Runnable {
    private int idRepartidor;
    private final String nombreRepartidor;
    private final ZonaDeCarga carga;
    private final PedidoDAO pedidoDAO;
    private final EntregaDAO entregaDAO;
    private int entregados;
    private int fallidos;

    public Repartidor(int idRepartidor, String nombreRepartidor, ZonaDeCarga carga, PedidoDAO pedidoDAO, EntregaDAO entregaDAO) {
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
        this.carga = carga;
        this.pedidoDAO = pedidoDAO;
        this.entregaDAO = entregaDAO;
    }
    public Repartidor(String nombreRepartidor) { this(0, nombreRepartidor, null, null, null);}
    public Repartidor(int idRepartidor, String nombreRepartidor) { this(idRepartidor, nombreRepartidor, null, null, null);}

    public int getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(int idRepartidor) { this.idRepartidor = idRepartidor; }
    public String getNombreRepartidor() { return nombreRepartidor; }
    //public void setNombreRepartidor(String nombreRepartidor) { this.nombreRepartidor = nombreRepartidor; }
    public ZonaDeCarga getCarga() { return carga; }
    public int getEntregados() { return entregados; }
    public int getFallidos() { return fallidos; }

    @Override
    public void run() {
        if (carga == null || pedidoDAO == null || entregaDAO == null) {
            throw new IllegalStateException("No se encuentra la información necesaria para realizar la entrega.");
        }
        while (!Thread.currentThread().isInterrupted()) {
            Pedido pedido = carga.retirarPedido();
            if (pedido == null) {
                break;
            }
            entregar(pedido);
        }
    }

    private void revertirAPendiente(Pedido pedido) {
        try {
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), Estado.PENDIENTE);
            pedido.setEstado(Estado.PENDIENTE);
        } catch (SQLException e) {
            System.out.println("No se pudo devolver a PENDIENTE el pedido #" + pedido.getIdPedido() + ": " + e.getMessage());
        }
    }

    private void entregar(Pedido pedido) {
        int id = pedido.getIdPedido();
        System.out.println("Retirando pedido #" + id);
        try {
            pedidoDAO.actualizarEstado(id, Estado.EN_REPARTO);
        } catch (SQLException e) {
            System.out.println("No se pudo marcar EN_REPARTO el pedido #" + id + ": " + e.getMessage());
            fallidos++;
            return;
        }

        pedido.setEstado(Estado.EN_REPARTO);
        System.out.println("Estado: " + pedido.getEstado());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Logística interrumpida...");
            Thread.currentThread().interrupt();
            revertirAPendiente(pedido);
            fallidos++;
            return;
        }

        System.out.println("Entregando pedido #" + id);
        try {
            entregaDAO.crear(new Entrega(id, idRepartidor, LocalDate.now(), LocalTime.now()));
        } catch (SQLException e) {
            System.out.println("Error al registrar la entrega del pedido #" + id + ": " + e.getMessage());
            revertirAPendiente(pedido);
            fallidos++;
            return;
        }

        pedido.setEstado(Estado.ENTREGADO);
        entregados++;
        System.out.println("Estado final: " + pedido.getEstado() + " (pedido #" + id + " guardado.)");
    }
}