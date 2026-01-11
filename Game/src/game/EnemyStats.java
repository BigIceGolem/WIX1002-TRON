package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class EnemyStats {
    
    private static HashMap<String, double[]> statsMap = new HashMap<>();
    private static boolean isLoaded = false;

    public static double[] getStats(String name) {
        if (!isLoaded) {
            loadStats();
        }
        // Returns [Speed, Handling, Aggro, XP]
        return statsMap.getOrDefault(name, new double[]{1.0, 1.0, 1.0, 10});
    }

    private static void loadStats() {
        try {
            File file = new File("enemies.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length >= 5) {
                    String enemyName = data[0].trim();
                    double speed = Double.parseDouble(data[1]);
                    double handling = Double.parseDouble(data[2]);
                    double aggro = Double.parseDouble(data[3]);
                    double xp = Double.parseDouble(data[4]);

                    statsMap.put(enemyName, new double[]{speed, handling, aggro, xp});
                }
            }
            scanner.close();
            isLoaded = true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: enemies.txt not found! Using defaults.");
        }
    }
}