package game;

import java.awt.Point;
import java.util.List;

public abstract class EnemyBot {
    
    public int x, y;
    public int color;
    public String name;
    public String displayName;
    
    // Inventory Mechanics
    protected int diskAmmo = 1;
    protected int cooldownTimer = 0;
    protected int maxCooldown = 5;
    
    // Trail management
    protected String trailIcon = "E";
    protected int trailColor = 0xFFFFFF;
    protected String lastDirection = "";

    public EnemyBot(int x, int y, int c, String name) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.name = name;
        this.displayName = name;
        
        if (name.equals("BrilliantEnemy")) {
            this.displayName = "Clu";
            this.trailIcon = "B";
            this.trailColor = 0xFFD700;
        } else if (name.equals("CleverEnemy")) {
            this.displayName = "Rinzler";
            this.trailIcon = "C";
            this.trailColor = 0xFF0000;
        } else if (name.equals("ModerateEnemy")) {
            this.displayName = "Sark";
            this.trailIcon = "M";
            this.trailColor = 0xFFFF00;
        } else if (name.equals("LowEnemy")) {
            this.displayName = "Koura";
            this.trailIcon = "L";
            this.trailColor = 0x00FF00;
        }
    }

    public abstract String makeMove(Map map, Point playerPos, List<EnemyBot> allBots);

    public void updateStatus() {
        if (cooldownTimer > 0) cooldownTimer--;
    }
    
    public void pickupDisk() {
        diskAmmo++;
        System.out.println(displayName + " picked up a disk! Ammo: " + diskAmmo);
    }

    protected boolean canShoot(Point p) {
        if (diskAmmo <= 0 || cooldownTimer > 0) return false;
        
        int dx = p.x - this.x;
        int dy = p.y - this.y;
        
        if (dy == 0 && Math.abs(dx) <= 3) return true;
        if (dx == 0 && Math.abs(dy) <= 3) return true;
        
        return false;
    }

    protected boolean isValid(int tx, int ty, Map map) {
        if (tx < 0 || tx >= map.getCols() || ty < 0 || ty >= map.getRows()) {
            return false;
        }
        
        String cell = map.getIconAt(ty, tx);
        
        return !cell.equals(Map.OBSTACLE) && 
               !cell.equals("^") && !cell.equals("#") && 
               !cell.equals("<") && !cell.equals(">") &&
               !cell.equals("B") && !cell.equals("C") &&
               !cell.equals("M") && !cell.equals("L") &&
               !cell.equals("E");
    }
    
    public String getTrailIcon(String direction) {
        lastDirection = direction;
        return trailIcon;
    }
    
    public String getDirectionTrailIcon() {
        return switch (lastDirection) {
            case "MOVE_UP" -> "B";
            case "MOVE_DOWN" -> "B";
            case "MOVE_LEFT" -> "B";
            case "MOVE_RIGHT" -> "B";
            default -> trailIcon;
        };
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public int getDiskAmmo() { return diskAmmo; }
    public void setDiskAmmo(int ammo) { this.diskAmmo = ammo; }
    public String getName() { return displayName; }
    public String getTrailCharacter() { return trailIcon; }
    public int getTrailColor() { return trailColor; }
    public String getLastDirection() { return lastDirection; }
}
