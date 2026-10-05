package controller;

import dao.*;
import model.*;
import java.sql.SQLException;
import java.util.List;

public class EntregaController {
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public List<Entrega> listar(Integer idPedido, Integer idRepartidor) throws SQLException {
        if (idPedido != null) {
            List<Entrega> delPedido = entregaDAO.listarPorPedido(idPedido);
            return idRepartidor == null
                    ? delPedido
                    : delPedido.stream().filter(e -> e.getIdRepartidor() == idRepartidor).toList();
        }
        return idRepartidor != null ? entregaDAO.listarPorRepartidor(idRepartidor) : entregaDAO.listarTodos();
    }

    public void registrar(Entrega entrega) throws SQLException { entregaDAO.crear(entrega);}
    public void actualizar(Entrega entrega) throws SQLException { entregaDAO.actualizar(entrega);}
    public void eliminar(int idEntrega) throws SQLException { entregaDAO.eliminar(idEntrega);}
    public List<Pedido> listarPedidos() throws SQLException { return pedidoDAO.listarTodos();}
    public List<Repartidor> listarRepartidores() throws SQLException { return repartidorDAO.listarTodos();}
}