package dao;

import java.sql.Connection;
import java.sql.SQLException;

final class Transaccion {
    @FunctionalInterface
    interface Operacion { void ejecutar(Connection con) throws SQLException;}
    private Transaccion() { }
    static void ejecutar(Operacion operacion) throws SQLException {
        try (Connection con = ConexionDB.getConnection()) {
            con.setAutoCommit(false);
            try {
                operacion.ejecutar(con);
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        }
    }
}