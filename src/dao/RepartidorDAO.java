package dao;

import model.Repartidor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {
    public RepartidorDAO() {
        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS repartidor (
                    idRepartidor INT PRIMARY KEY AUTO_INCREMENT,
                    nombreRepartidor VARCHAR(50) NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("No fue posible preparar la tabla repartidor", e);
        }
    }

    public void crear(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidor(nombreRepartidor) VALUES (?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, repartidor.getNombreRepartidor());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("MySQL no devolvió el ID generado del repartidor.");
                }
                repartidor.setIdRepartidor(rs.getInt(1));
            }
        }
    }

    public List<Repartidor> listarTodos() throws SQLException {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT idRepartidor, nombreRepartidor FROM repartidor ORDER BY idRepartidor";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                repartidores.add(new Repartidor(rs.getInt("idRepartidor"), rs.getString("nombreRepartidor")));
            }
        }
        return repartidores;
    }

    public void actualizar(Repartidor repartidor) throws SQLException {
        String sql = "UPDATE repartidor SET nombreRepartidor = ? WHERE idRepartidor = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, repartidor.getNombreRepartidor());
            st.setInt(2, repartidor.getIdRepartidor());
            if (st.executeUpdate() == 0) {
                throw new SQLException("El repartidor #" + repartidor.getIdRepartidor() + " no existe.");
            }
        }
    }

    public void eliminar(int idRepartidor) throws SQLException {
        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement conteo = con.prepareStatement(
                    "SELECT COUNT(*) FROM entrega WHERE idRepartidor = ?")) {
                conteo.setInt(1, idRepartidor);
                try (ResultSet rs = conteo.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("No se puede eliminar el repartidor #" + idRepartidor
                                + ": tiene " + rs.getInt(1) + " entrega(s) registrada(s).\n"
                                + "Elimine o reasigne esas entregas primero.");
                    }
                }
            }
            try (PreparedStatement st = con.prepareStatement("DELETE FROM repartidor WHERE idRepartidor = ?")) {
                st.setInt(1, idRepartidor);
                if (st.executeUpdate() == 0) {
                    throw new SQLException("El repartidor #" + idRepartidor + " no existe.");
                }
            }
        }
    }
}