package dao;

import model.Entrega;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {
    public void guardar(Entrega entrega) throws SQLException  {
        String sql = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection con = ConexionDB.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(4, java.sql.Time.valueOf(entrega.getHora()));

            ps.executeUpdate();
        }
    }

    public List<Entrega> listarTodos() throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT * FROM entrega";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );
                entregas.add(entrega);
             }
        }
        return entregas;
    }
}
