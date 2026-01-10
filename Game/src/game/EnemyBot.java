package game;

import java.awt.Point;
import java.util.List;

public abstract class EnemyBot {
    
    public int x, y; // x = col, y = row
    public int color;
    public String name;
    
    // Inventory Mechanics
    protected int diskAmmo = 1;
    protected int cooldownTimer = 0;
    protected int maxCooldown = 5;

    public EnemyBot(int x, int y, int c, String name) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.name = name;
    }

    // --- THE RULEBOOK ---
    public abstract String makeMove(Map map, Point playerPos, List<EnemyBot> allBots);

    // --- SHARED TOOLS ---
    public void updateStatus() {
        if (cooldownTimer > 0) cooldownTimer--;
    }
    
    public void pickupDisk() {
        diskAmmo++;
        System.out.println(name + " picked up a disk! Ammo: " + diskAmmo);
    }

    protected boolean canShoot(Point p) {
        if (diskAmmo <= 0 || cooldownTimer > 0) return false;
        
        int dx = p.x - this.x;
        int dy = p.y - this.y;
        
        // Check alignment (Range <= 3)
        if (dy == 0 && Math.abs(dx) <= 3) return true;
        if (dx == 0 && Math.abs(dy) <= 3) return true;
        
        return false;
    }

    protected boolean isValid(int tx, int ty, Map map) {
        // Remember: y is Row, x is Col. Map checks (row, col)
        return map.isValidPos(ty, tx) && !map.isWall(ty, tx);
    }
    
    // Getters for integration
    public int getX() { return x; }
    public int getY() { return y; }
}