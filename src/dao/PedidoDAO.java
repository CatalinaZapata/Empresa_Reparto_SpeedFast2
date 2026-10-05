package dao;

import model.*;
import java.sql.*;
import java.util.*;

public class PedidoDAO {
    public PedidoDAO() {
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS pedido (
                    idPedido INT PRIMARY KEY AUTO_INCREMENT,
                    direccionEntrega VARCHAR(150) NOT NULL,
                    tipo VARCHAR(30) NOT NULL,
                    estado VARCHAR(20) NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("No fue posible preparar la tabla pedido", e);
        }
    }

    public void crear(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido(direccionEntrega, tipo, estado) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, pedido.getDireccionEntrega());
            st.setString(2, pedido.getTipo().name());
            st.setString(3, pedido.getEstado().name());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("MySQL no devolvió el ID generado del pedido.");
                }
                pedido.setIdPedido(rs.getInt(1));
            }
        }
    }

    public List<Pedido> listarTodos() throws SQLException { return listarFiltrado(null, null);}

    public List<Pedido> listarFiltrado(Estado estado, TipoPedido tipo) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT idPedido, direccionEntrega, tipo, estado FROM pedido WHERE 1 = 1");
        List<String> parametros = new ArrayList<>();
        if (estado != null) {
            sql.append(" AND estado = ?");
            parametros.add(estado.name());
        }
        if (tipo != null) {
            sql.append(" AND tipo = ?");
            parametros.add(tipo.name());
        }
        sql.append(" ORDER BY idPedido");
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                st.setString(i + 1, parametros.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new Pedido(
                            rs.getInt("idPedido"),
                            rs.getString("direccionEntrega"),
                            TipoPedido.valueOf(rs.getString("tipo")),
                            Estado.valueOf(rs.getString("estado"))
                    ));
                }
            }
        }
        return pedidos;
    }

    public void actualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedido SET direccionEntrega = ?, tipo = ?, estado = ? WHERE idPedido = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, pedido.getDireccionEntrega());
            st.setString(2, pedido.getTipo().name());
            st.setString(3, pedido.getEstado().name());
            st.setInt(4, pedido.getIdPedido());
            if (st.executeUpdate() == 0) {
                throw new SQLException("El pedido #" + pedido.getIdPedido() + " no existe.");
            }
        }
    }

    public void actualizarEstado(int idPedido, Estado estado) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE idPedido = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, estado.name());
            st.setInt(2, idPedido);
            if (st.executeUpdate() == 0) { throw new SQLException("El pedido #" + idPedido + " no existe.");}
        }
    }

    public void eliminar(int idPedido) throws SQLException {
        Transaccion.ejecutar(con -> {
            try (PreparedStatement entregas = con.prepareStatement("DELETE FROM entrega WHERE idPedido = ?")) {
                entregas.setInt(1, idPedido);
                entregas.executeUpdate();
            }
            try (PreparedStatement pedido = con.prepareStatement("DELETE FROM pedido WHERE idPedido = ?")) {
                pedido.setInt(1, idPedido);
                if (pedido.executeUpdate() == 0) {
                    throw new SQLException("El pedido #" + idPedido + " no existe.");
                }
            }
        });
    }
}