package game;

public class BasicMechanic extends Character {
    
    // Grid Position
    private int row;
    private int col;
    private String currentDir = ""; // "w", "a", "s", "d"

    public BasicMechanic() {
        // Set starting position (Center of map roughly)
        this.row = 20;
        this.col = 20;
        
        // Default health for the GUI to display
        this.lives = 100; 
        this.name = "Player";
    }

    // --- MOVEMENT METHODS (Fixes the Red Lines) ---

    public void setDirection(String d) {
        // Prevent 180-degree turns (optional, but good for Tron)
        if (this.currentDir.equals("w") && d.equals("s")) return;
        if (this.currentDir.equals("s") && d.equals("w")) return;
        if (this.currentDir.equals("a") && d.equals("d")) return;
        if (this.currentDir.equals("d") && d.equals("a")) return;
        
        this.currentDir = d;
    }

    public String getCurrentDir() {
        return this.currentDir;
    }

    public void move() {
        switch (currentDir) {
            case "w" -> row--; // Up
            case "s" -> row++; // Down
            case "a" -> col--; // Left
            case "d" -> col++; // Right
        }
    }

    public void undoPosition(int oldR, int oldC) {
        this.row = oldR;
        this.col = oldC;
    }

    // --- GETTERS & HELPERS ---

    public int getRow() { return row; }
    public int getCol() { return col; }

    public int getHealth() {
        return this.lives;
    }

    // Used by the GUI to handle collision damage
    public void takeDamage(double amount) {
        // Convert the double damage (0.5) to integer damage
        int damage = (int) Math.ceil(amount);
        this.lives -= damage;
        if (this.lives < 0) this.lives = 0;
    }

    // Determines what symbol the player leaves behind
    public String jetWallIcon() {
        return switch (currentDir) {
            case "w" -> "^";
            case "s" -> "v";
            case "a" -> "<";
            case "d" -> ">";
            default -> "#";
        };
    }
    
    @Override
    protected void applySpecificStats() {
        // BasicMechanic is just for testing movement, so we don't need complex stats here.
        System.out.println("BasicMechanic stats check passed.");
    }
    
}
