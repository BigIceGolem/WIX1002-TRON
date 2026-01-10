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

public class CleverEnemy extends EnemyBot {

    public CleverEnemy(int x, int y, int c) { super(x, y, c, "CleverEnemy"); }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        // 1. Find a Partner
        EnemyBot partner = null;
        for(EnemyBot b : allBots) {
            if (b != this) { partner = b; break; }
        }

        // 2. Determine Tactical Target
        Point target = playerPos;
        if (partner != null) {
            // "Pinch" Strategy: Target the opposite side of the player from my partner
            int offsetX = (partner.x < playerPos.x) ? 5 : -5; // If partner is Left, go Right (+5)
            target = new Point(playerPos.x + offsetX, playerPos.y);
        }

        // 3. Move towards Target
        // Prioritize the axis with the biggest difference
        int diffX = target.x - x;
        int diffY = target.y - y;

        if (Math.abs(diffX) > Math.abs(diffY)) {
            // Move Horizontal first
            if (diffX > 0 && isValid(x+1, y, map)) return "MOVE_RIGHT";
            if (diffX < 0 && isValid(x-1, y, map)) return "MOVE_LEFT";
        } else {
            // Move Vertical
            if (diffY > 0 && isValid(x, y+1, map)) return "MOVE_DOWN";
            if (diffY < 0 && isValid(x, y-1, map)) return "MOVE_UP";
        }

        // Fallback
        return "MOVE_UP";
    }
}

