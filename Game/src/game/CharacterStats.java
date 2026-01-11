package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharacterStats {

    public static Character loadCharacter(String name) {
        try {
            File file = new File("characters.txt"); 
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Format: Name,Speed,Handling,Lives,Discs,XP
                String[] data = line.split(","); 

                if (data[0].equalsIgnoreCase(name)) {
                    Character player = null;
                    
                    if (name.equalsIgnoreCase("Tron")) {
                        player = new Tron(); 
                    } else if (name.equalsIgnoreCase("Kevin") || name.equalsIgnoreCase("Kevin Flynn")) {
                        player = new KevinFlynn(); 
                    }
                    
                    if (player != null) {
                        player.speed = Double.parseDouble(data[1]);
                        player.handling = Double.parseDouble(data[2]);
                        // CHANGED: Parse as Double
                        player.lives = Double.parseDouble(data[3]);
                        player.discsOwned = Integer.parseInt(data[4]);
                        player.xp = Double.parseDouble(data[5]);
                        
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