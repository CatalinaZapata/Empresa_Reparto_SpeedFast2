package controller;

import data.ZonaDeCarga;
import model.Estado;
import model.Pedido;
import model.Repartidor;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {
    private final List<Pedido> pedidos;

    public PedidoController(){
        pedidos = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido){
        pedidos.add(pedido);
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

        Thread repartidor1 = new Thread(new Repartidor("Ana", z));
        Thread repartidor2 = new Thread(new Repartidor("Juan", z));
        Thread repartidor3 = new Thread(new Repartidor("Pedro", z));

        repartidor1.start();
        repartidor2.start();
        repartidor3.start();

        repartidor1.join();
        repartidor2.join();
        repartidor3.join();
    }
}
