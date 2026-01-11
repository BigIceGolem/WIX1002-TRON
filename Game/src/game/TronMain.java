package game; 

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TronMain {
    public static void main(String[] args) {
        
        System.out.println("!!! MAIN METHOD IS STARTING !!!");
        
        Character myRPGCharacter = CharacterStats.loadCharacter("Tron");
        
         if (myRPGCharacter == null) {
                JOptionPane.showMessageDialog(null, "Error: Could not load characters.txt!");
                return;
            }

            // 2. PASS IT TO THE GUI
            // We send 'myRPGCharacter' into the constructor
        
        System.out.println("Sending Player to GUI -> Speed: " + myRPGCharacter.getSpeed());
        
        SwingUtilities.invokeLater(() -> {
            new TronGUI(myRPGCharacter); // Ensure TronGUI is also in 'package game;'
        });
    }
}