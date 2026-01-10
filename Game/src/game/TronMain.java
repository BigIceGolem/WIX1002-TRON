import javax.swing.SwingUtilities;

public class TronMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TronGUI(); 
        });
    }
}
