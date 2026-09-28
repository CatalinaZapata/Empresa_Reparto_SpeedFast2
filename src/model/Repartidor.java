package model;

import data.ZonaDeCarga;

public class Repartidor implements Runnable{
    private String nombreRepartidor;
    private ZonaDeCarga carga;

    public Repartidor(String nombreRepartidor, ZonaDeCarga carga) {
        this.nombreRepartidor = nombreRepartidor;
        this.carga = carga;
    }

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
        }
     }
}
