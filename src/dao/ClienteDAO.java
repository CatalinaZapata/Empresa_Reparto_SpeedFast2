package dao;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public ClienteDAO() {
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                    idCliente INT PRIMARY KEY AUTO_INCREMENT,
                    nombreCliente VARCHAR(50) NOT NULL,
                    telefono VARCHAR(20) NOT NULL,
                    correo VARCHAR(100) NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("No fue posible preparar la tabla cliente", e);
        }
    }

    public void crear(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente(nombreCliente, telefono, correo) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, cliente.getNombreCliente());
            st.setString(2, cliente.getTelefono());
            st.setString(3, cliente.getCorreo());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("MySQL no devolvió el ID generado del cliente.");
                }
                cliente.setIdCliente(rs.getInt(1));
            }
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT idCliente, nombreCliente, telefono, correo FROM cliente ORDER BY idCliente";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getInt("idCliente"), rs.getString("nombreCliente"),
                        rs.getString("telefono"), rs.getString("correo")));
            }
        }
        return clientes;
    }

    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombreCliente = ?, telefono = ?, correo = ? WHERE idCliente = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, cliente.getNombreCliente());
            st.setString(2, cliente.getTelefono());
            st.setString(3, cliente.getCorreo());
            st.setInt(4, cliente.getIdCliente());
            if (st.executeUpdate() == 0) {
                throw new SQLException("El cliente #" + cliente.getIdCliente() + " no existe.");
            }
        }
    }

    public void eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE idCliente = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idCliente);
            if (st.executeUpdate() == 0) {
                throw new SQLException("El cliente #" + idCliente + " no existe.");
            }
        }
    }
}