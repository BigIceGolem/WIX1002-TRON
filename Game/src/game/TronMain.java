package game; // FIXED: Added package

import javax.swing.SwingUtilities;

public class TronMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TronGUI(); // Ensure TronGUI is also in 'package game;'
        });
    }
}