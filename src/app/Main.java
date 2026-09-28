package app;

import controller.PedidoController;
import javax.swing.SwingUtilities;
import view.VentanaPrincipal;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            PedidoController controller = new PedidoController();
            VentanaPrincipal ventana = new VentanaPrincipal(controller);
            ventana.setVisible(true);
        });
    }
}