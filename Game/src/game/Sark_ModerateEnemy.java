package game;

import java.awt.Point;
import java.util.List;

public class Sark_ModerateEnemy extends Enemy {

    public Sark_ModerateEnemy(int x, int y, int c) { 
        super(x, y, c, "ModerateEnemy"); 
    }

    @Override

public String makeMove(Map map, Point playerPos, List<Enemy> allBots) {
    // 1. Attack Check
    if (canShoot(playerPos)) {
        if (playerPos.y < y) return "THROW_UP";
        if (playerPos.y > y) return "THROW_DOWN";
        if (playerPos.x < x) return "THROW_LEFT";
        if (playerPos.x > x) return "THROW_RIGHT";
    }

    // 2. Try safe moves first
    int[] dx = {0, 0, -1, 1};
    int[] dy = {-1, 1, 0, 0};
    String[] names = {"MOVE_UP", "MOVE_DOWN", "MOVE_LEFT", "MOVE_RIGHT"};
    
    // Try to find a safe move
    for(int i=0; i<4; i++) {
        int tx = x + dx[i];
        int ty = y + dy[i];
        if (isValid(tx, ty, map)) {
            return names[i];
        }
    }
    
    // 3. No safe moves - force a move (will cause death)
    // Just return the first direction that's within bounds
    for(int i=0; i<4; i++) {
        int tx = x + dx[i];
        int ty = y + dy[i];
        if (tx >= 0 && tx < map.getCols() && ty >= 0 && ty < map.getRows()) {
            return names[i];
        }
    }
    
    // 4. Last resort (should never happen)
    return "MOVE_UP";
}
}
