public class BasicMechanic {
    private int currentRow;
    private int currentCol;
    private double health;
    private String currentDir = "";
    
    // Constants
    private final int DEFAULT_ROW = 20;
    private final int DEFAULT_COL = 20;
    private final double DEFAULT_HEALTH = 3.0;

    public BasicMechanic() {
        this.currentRow = DEFAULT_ROW;
        this.currentCol = DEFAULT_COL;
        this.health = DEFAULT_HEALTH;
    }

    public BasicMechanic(int startRow, int startCol, double startHealth) {
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
}
