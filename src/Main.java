import ui.PantallaArcade;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaArcade ventana = new PantallaArcade();
            ventana.setVisible(true);
        });
    }
}