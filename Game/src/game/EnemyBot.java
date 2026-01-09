/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author USER
 */

import java.awt.Point;
import java.util.List;

public abstract class EnemyBot {
    
    public int x, y;
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
    // Every child MUST write their own version of this method
    public abstract String makeMove(Map map, Point playerPos, List<EnemyBot> allBots);

    // --- SHARED TOOLS ---
    
    // Call this every turn to lower cooldowns
    public void updateStatus() {
        if (cooldownTimer > 0) cooldownTimer--;
    }
    
    // Call this when bot walks over a disk
    public void pickupDisk() {
        diskAmmo++;
        System.out.println(name + " picked up a disk! Ammo: " + diskAmmo);
    }

    // Check if we can shoot the player (Aligned + Range <= 3)
    protected boolean canShoot(Point p) {
        if (diskAmmo <= 0 || cooldownTimer > 0) return false;
        
        int dx = p.x - this.x;
        int dy = p.y - this.y;
        
        // Horizontal Check
        if (dy == 0 && Math.abs(dx) <= 3) return true;
        // Vertical Check
        if (dx == 0 && Math.abs(dy) <= 3) return true;
        
        return false;
    }

    // Simple helper to check if a tile is safe (not a wall)
    protected boolean isValid(int tx, int ty, Map map) {
        return tx >= 0 && tx < map.getWidth() && 
               ty >= 0 && ty < map.getHeight() && 
               !map.isWall(tx, ty);
    }
}
