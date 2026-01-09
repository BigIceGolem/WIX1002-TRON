/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.game.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharacterStats {

    // This method does all the work. Your group mates just call this.
    public static Character loadCharacter(String name) {
        
        try {
            // This looks for the file in the Project Root folder
            File file = new File("characters.txt"); 
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Expecting format: Name,Speed,Handling,Lives,Discs,XP
                String[] data = line.split(","); 

                // Check if this line is the character we want
                if (data[0].equalsIgnoreCase(name)) {
                    
                    // Convert Strings to numbers
                    double speed = Double.parseDouble(data[1]);
                    double handling = Double.parseDouble(data[2]);
                    int lives = Integer.parseInt(data[3]);
                    int discs = Integer.parseInt(data[4]);
                    int xp = Integer.parseInt(data[5]);

                    // Return the correct object based on the name
                    if (name.equalsIgnoreCase("Tron")) {
                        return new Tron(speed, handling, lives, discs, xp);
                    } else if (name.equalsIgnoreCase("Kevin")) {
                        return new Kevin(speed, handling, lives, discs, xp);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: characters.txt file not found!");
            e.printStackTrace();
        }
        return null; // Return null if character wasn't found
    }
}