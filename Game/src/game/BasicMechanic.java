import java.util.ArrayList; // Added for Disc storage
import java.util.List;

public class BasicMechanic {
    private int currentRow;
    private int currentCol;
    private double health;
    private String currentDir = "";
    private String name; // Added: To identify owner
    private List<Disc> inventory = new ArrayList<>(); // Added: To store ammo
    
    // Constants
    private final int DEFAULT_ROW = 20;
    private final int DEFAULT_COL = 20;
    private final double DEFAULT_HEALTH = 3.0;

    public BasicMechanic() {
        this.currentRow = DEFAULT_ROW;
        this.currentCol = DEFAULT_COL;
        this.health = DEFAULT_HEALTH;
    }

    // Modified Constructor to accept Name
    public BasicMechanic(String name, int startRow, int startCol, double startHealth) {
        this.name = name;
        this.currentRow = startRow;
        this.currentCol = startCol;
        setHealth(startHealth);
    }

    public void setDirection(String input) {
        String in = input.toLowerCase();
        if (in.equals("w") || in.equals("a") || in.equals("s") || in.equals("d")) {
            // Prevent immediate 180-degree turns (optional polish)
            if (this.currentDir.equals("w") && in.equals("s")) return;
            if (this.currentDir.equals("s") && in.equals("w")) return;
            if (this.currentDir.equals("a") && in.equals("d")) return;
            if (this.currentDir.equals("d") && in.equals("a")) return;
            
            this.currentDir = in;
        } else if (in.equals("")) {
            this.currentDir = "";
        }
    }

    public void move() {
        switch (currentDir) {
            case "w" -> currentRow--;
            case "s" -> currentRow++;
            case "a" -> currentCol--;
            case "d" -> currentCol++;
        }
    }

    public void takeDamage(double amount) {
        this.health -= amount;
        if (this.health < 0) this.health = 0;
    }

    public void undoPosition(int r, int c) {
        this.currentRow = r;
        this.currentCol = c;
    }

    public String jetWallIcon() {
        // Returns the trail icon based on where we CAME from
        return switch (currentDir) {
            case "w" -> "^";
            case "s" -> "#";
            case "a" -> "<";
            case "d" -> ">";
            default -> "P";
        };
    }

    // Getters and Setters
    public int getRow() { return currentRow; }
    public int getCol() { return currentCol; }
    public double getHealth() { return health; }
    public String getCurrentDir() { return currentDir; }
    
    public void setHealth(double h) {
        if (h < 0) throw new IllegalArgumentException("Invalid health");
        this.health = h;
    }
    // ==========================================================
    // NEW METHODS ADDED FOR COLLISION MANAGER COMPATIBILITY
    // ==========================================================

    // 1. Alias methods (Your code calls X/Y, friend uses Col/Row)
    public int getX() { return currentCol; } 
    public int getY() { return currentRow; }
    
    // 2. Identification (CollisionManager needs to know who owns the disc)
    public String getName() { return name; }

    // 3. Stop Movement (Called when hitting walls)
    public void stopMovement() {
        this.currentDir = ""; // Stops the auto-move loop
    }

    // 4. Kill switch (Called for holes)
    public void setLives(double amount) {
        this.health = amount;
    }

    // 5. Ammo Handling (Called when picking up disc)
    public void addDisc(Disc d) {
        inventory.add(d);
        // Optional: System.out.println("Disc Reloaded!");
    }
}
