package controller;

import dao.ClienteDAO;
import model.Cliente;
import java.sql.SQLException;
import java.util.List;

public class ClienteController {
    private final ClienteDAO clienteDAO = new ClienteDAO();
    public List<Cliente> listar() throws SQLException { return clienteDAO.listarTodos();}
    public void registrar(Cliente cliente) throws SQLException { clienteDAO.crear(cliente); }
    public void actualizar(Cliente cliente) throws SQLException {clienteDAO.actualizar(cliente);}
    public void eliminar(int idCliente) throws SQLException {clienteDAO.eliminar(idCliente);}
}