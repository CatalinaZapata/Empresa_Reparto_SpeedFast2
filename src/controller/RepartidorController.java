package controller;

import dao.RepartidorDAO;
import model.Repartidor;
import java.sql.SQLException;
import java.util.List;

public class RepartidorController {
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    public List<Repartidor> listar() throws SQLException { return repartidorDAO.listarTodos();}
    public void registrar(Repartidor repartidor) throws SQLException { repartidorDAO.crear(repartidor);}
    public void actualizar(Repartidor repartidor) throws SQLException { repartidorDAO.actualizar(repartidor);}
    public void eliminar(int idRepartidor) throws SQLException { repartidorDAO.eliminar(idRepartidor);}
}