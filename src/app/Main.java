package app;

import controller.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import view.VentanaPrincipal;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            try {
                VentanaPrincipal ventana = new VentanaPrincipal(new PedidoController(), new ClienteController(), new RepartidorController(), new EntregaController());
                ventana.setVisible(true);
            } catch (RuntimeException e) {
                Throwable causa = e.getCause() != null ? e.getCause() : e;
                JOptionPane.showMessageDialog(null, "No fue posible iniciar el sistema.\n"
                        + "Verifique que MySQL esté activo, que exista la base speedfast_db\n"
                        + "y que la variable de entorno SPEEDFAST_DB_PASSWORD esté definida.\n\n"
                        + "Detalle: " + causa.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
