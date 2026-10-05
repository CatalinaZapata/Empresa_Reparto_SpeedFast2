package dao;

import model.Entrega;
import model.Estado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {
    public EntregaDAO() {
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS entrega (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    idPedido INT NOT NULL,
                    idRepartidor INT NOT NULL,
                    fecha DATE NOT NULL,
                    hora TIME NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("No fue posible preparar la tabla entrega", e);
        }
    }

    public void crear(Entrega entrega) throws SQLException {
        Transaccion.ejecutar(con -> {
            exigirExistencia(con, "pedido", "idPedido", entrega.getIdPedido(), "El pedido");
            exigirExistencia(con, "repartidor", "idRepartidor", entrega.getIdRepartidor(), "El repartidor");
            exigirPedidoSinEntrega(con, entrega.getIdPedido(), 0);
            String sql = "INSERT INTO entrega(idPedido, idRepartidor, fecha, hora) VALUES (?, ?, ?, ?)";
            try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                st.setInt(1, entrega.getIdPedido());
                st.setInt(2, entrega.getIdRepartidor());
                st.setDate(3, Date.valueOf(entrega.getFecha()));
                st.setTime(4, Time.valueOf(entrega.getHora()));
                st.executeUpdate();
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (!rs.next()) { throw new SQLException("MySQL no devolvió el ID generado de la entrega.");}
                    entrega.setId(rs.getInt(1));
                }
            }
            marcarPedidoEntregado(con, entrega.getIdPedido());
        });
    }

    public List<Entrega> listarTodos() throws SQLException { return consultar(null, 0);}
    public List<Entrega> listarPorPedido(int idPedido) throws SQLException { return consultar("idPedido", idPedido);}
    public List<Entrega> listarPorRepartidor(int idRepartidor) throws SQLException { return consultar("idRepartidor", idRepartidor);}

    public void actualizar(Entrega entrega) throws SQLException {
        Transaccion.ejecutar(con -> {
            int pedidoAnterior = pedidoDeLaEntrega(con, entrega.getId());
            exigirExistencia(con, "pedido", "idPedido", entrega.getIdPedido(), "El pedido");
            exigirExistencia(con, "repartidor", "idRepartidor", entrega.getIdRepartidor(), "El repartidor");
            boolean cambiaPedido = pedidoAnterior != entrega.getIdPedido();
            if (cambiaPedido) { exigirPedidoSinEntrega(con, entrega.getIdPedido(), entrega.getId());}

            String sql = "UPDATE entrega SET idPedido = ?, idRepartidor = ?, fecha = ?, hora = ? WHERE id = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setInt(1, entrega.getIdPedido());
                st.setInt(2, entrega.getIdRepartidor());
                st.setDate(3, Date.valueOf(entrega.getFecha()));
                st.setTime(4, Time.valueOf(entrega.getHora()));
                st.setInt(5, entrega.getId());
                st.executeUpdate();
            }
            if (cambiaPedido) {
                liberarPedido(con, pedidoAnterior);
                marcarPedidoEntregado(con, entrega.getIdPedido());
            }
        });
    }

    public void eliminar(int idEntrega) throws SQLException {
        Transaccion.ejecutar(con -> {
            int idPedido = pedidoDeLaEntrega(con, idEntrega);
            try (PreparedStatement st = con.prepareStatement("DELETE FROM entrega WHERE id = ?")) {
                st.setInt(1, idEntrega);
                st.executeUpdate();
            }
            liberarPedido(con, idPedido);
        });
    }

    private List<Entrega> consultar(String columnaFiltro, int valor) throws SQLException {
        String sql = "SELECT id, idPedido, idRepartidor, fecha, hora FROM entrega" + (columnaFiltro == null ? "" : " WHERE " + columnaFiltro + " = ?") + " ORDER BY id";
        List<Entrega> entregas = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            if (columnaFiltro != null) { st.setInt(1, valor);}
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    entregas.add(new Entrega(
                            rs.getInt("id"),
                            rs.getInt("idPedido"),
                            rs.getInt("idRepartidor"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime()
                    ));
                }
            }
        }
        return entregas;
    }

    private void exigirExistencia(Connection con, String tabla, String columna, int id, String etiqueta) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("SELECT 1 FROM " + tabla + " WHERE " + columna + " = ?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) { throw new SQLException(etiqueta + " #" + id + " no existe.");}
            }
        }
    }

    private void exigirPedidoSinEntrega(Connection con, int idPedido, int idEntregaIgnorada) throws SQLException {
        String sql = "SELECT COUNT(*) FROM entrega WHERE idPedido = ? AND id <> ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idPedido);
            st.setInt(2, idEntregaIgnorada);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) { throw new SQLException("El pedido #" + idPedido + " ya tiene una entrega registrada.");}
            }
        }
    }

    private int pedidoDeLaEntrega(Connection con, int idEntrega) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("SELECT idPedido FROM entrega WHERE id = ?")) {
            st.setInt(1, idEntrega);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) { throw new SQLException("La entrega #" + idEntrega + " no existe.");}
                return rs.getInt(1);
            }
        }
    }

    private void marcarPedidoEntregado(Connection con, int idPedido) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("UPDATE pedido SET estado = ? WHERE idPedido = ?")) {
            st.setString(1, Estado.ENTREGADO.name());
            st.setInt(2, idPedido);
            st.executeUpdate();
        }
    }

    private void liberarPedido(Connection con, int idPedido) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE idPedido = ? AND estado = ? " + "AND NOT EXISTS (SELECT 1 FROM entrega WHERE idPedido = ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, Estado.PENDIENTE.name());
            st.setInt(2, idPedido);
            st.setString(3, Estado.ENTREGADO.name());
            st.setInt(4, idPedido);
            st.executeUpdate();
        }
    }
}