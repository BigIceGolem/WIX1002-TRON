package game; // FIXED: Matches the rest of your project

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharacterStats {

    public static Character loadCharacter(String name) {
        System.out.println("DEBUG: The loadCharacter method has started!");
        
        try {
            // Looks for characters.txt in the project folder
            File file = new File("characters.txt"); 
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Format: Name,Speed,Handling,Lives,Discs,XP
                String[] data = line.split(","); 

                // Check if this line is the character we want
                if (data[0].equalsIgnoreCase(name)) {
                    
                    // 1. Create the correct Object (Empty first)
                    Character player = null;
                    
                    if (name.equalsIgnoreCase("Tron")) {
                        player = new Tron(); 
                    } else if (name.equalsIgnoreCase("Kevin") || name.equalsIgnoreCase("Kevin Flynn")) {
                        player = new KevinFlynn(); // FIXED: Uses your correct class name
                    }
                    
                    // If we found a match, fill in the stats
                    if (player != null) {
                        player.speed = Double.parseDouble(data[1]);
                        player.handling = Double.parseDouble(data[2]);
                        player.lives = Integer.parseInt(data[3]);
                        player.discsOwned = Integer.parseInt(data[4]);
                        player.xp = Integer.parseInt(data[5]);
                        
                        // We also need to sync the 'level' based on XP (Optional but good)
                        // player.level = (int) (player.xp / 100) + 1; 
                        
                        scanner.close();
                        return player;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: characters.txt file not found!");
        }
        return null; 
    }
}