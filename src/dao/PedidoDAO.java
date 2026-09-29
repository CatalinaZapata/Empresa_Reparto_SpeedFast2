package dao;

import model.Estado;
import model.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    public void guardar(Pedido pedido) throws SQLException {
        String sql = """
                INSERT INTO pedido
                (id, direccion, tipo, estado)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pedido.getIdPedido());
            ps.setString(2, pedido.getDireccionEntrega());
            ps.setString(3, pedido.getTipo().toString());
            ps.setString(4, pedido.getEstado().toString());

            ps.executeUpdate();
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        model.TipoPedido.valueOf(rs.getString("tipo")),
                        Estado.valueOf(rs.getString("estado")));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }
}
