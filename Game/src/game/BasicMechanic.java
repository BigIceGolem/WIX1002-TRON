package game;

import java.awt.Point;
import java.util.LinkedList;

public class BasicMechanic extends Character {
    
    private int row;
    private int col;
    private String currentDir = ""; 
    
    // NEW: Memory for disappearing trails
    private LinkedList<Point> trailHistory = new LinkedList<>();

    public BasicMechanic() {
        this.row = 20;
        this.col = 20;
        this.lives = 5.0; 
        this.name = "Player";
    }

    public void setDirection(String d) {
        if (this.currentDir.equals("w") && d.equals("s")) return;
        if (this.currentDir.equals("s") && d.equals("w")) return;
        if (this.currentDir.equals("a") && d.equals("d")) return;
        if (this.currentDir.equals("d") && d.equals("a")) return;
        this.currentDir = d;
    }

    public String getCurrentDir() { return this.currentDir; }

    public void move() {
        switch (currentDir) {
            case "w" -> row--; 
            case "s" -> row++; 
            case "a" -> col--; 
            case "d" -> col++; 
        }
    }

    public void undoPosition(int oldR, int oldC) {
        this.row = oldR;
        this.col = oldC;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    
    // NEW: Access to trail history
    public LinkedList<Point> getTrailHistory() { return trailHistory; }

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
        System.out.println("BasicMechanic stats check passed.");
    }
}