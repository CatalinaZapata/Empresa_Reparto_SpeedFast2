package model;

import data.ZonaDeCarga;
import dao.EntregaDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Repartidor implements Runnable{
    private int idRepartidor;
    private String nombreRepartidor;
    private ZonaDeCarga carga;
    private final EntregaDAO entregaDAO;

    public Repartidor(String nombreRepartidor, ZonaDeCarga carga) {
        this.idRepartidor = 0;
        this.nombreRepartidor = nombreRepartidor;
        this.carga = carga;
        this.entregaDAO = new EntregaDAO();
    }

    public Repartidor(int idRepartidor, String nombreRepartidor, ZonaDeCarga carga) {
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
        this.carga = carga;
        this.entregaDAO = new EntregaDAO();
    }

    public int getIdRepartidor() { return idRepartidor;}
    public String getNombreRepartidor() { return nombreRepartidor;}
    public ZonaDeCarga getCarga() { return carga;}

    @Override
     public void run() {
        while (true){
            Pedido pedido = carga.retirarPedido();
            if (pedido == null) {
                break;
            }
            System.out.println("[Repartidor - " + nombreRepartidor + "] Retirando pedido #" + pedido.getIdPedido());
            pedido.setEstado(Estado.EN_REPARTO);
            System.out.println("[Repartidor - " + nombreRepartidor + "] Estado: " + pedido.getEstado());
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Logística interrumpida...");
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[Repartidor - " + nombreRepartidor + "] Entregando pedido #" + pedido.getIdPedido());
            pedido.setEstado(Estado.ENTREGADO);
            System.out.println("[Repartidor - " + nombreRepartidor + "] Estado: " + pedido.getEstado());

            try {
                Entrega entrega = new Entrega(pedido.getIdPedido(), idRepartidor, LocalDate.now(), LocalTime.now());
                entregaDAO.guardar(entrega);
                System.out.println("[Entrega] Pedido #" + pedido.getIdPedido() + " registrado en la base de datos.");
            } catch (SQLException e) {
                System.out.println("[Entrega] Error al registrar la entrega del pedido #" + pedido.getIdPedido());
                e.printStackTrace();
            }
        }
     }
}
