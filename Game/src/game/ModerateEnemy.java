package game;

import java.awt.Point;
import java.util.List;

public class ModerateEnemy extends EnemyBot {

    public ModerateEnemy(int x, int y, int c) { super(x, y, c, "ModerateEnemy"); }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        
        // 1. Attack Check
        if (canShoot(playerPos)) {
            if (playerPos.y < y) return "THROW_UP";
            if (playerPos.y > y) return "THROW_DOWN";
            if (playerPos.x < x) return "THROW_LEFT";
            if (playerPos.x > x) return "THROW_RIGHT";
        }

        // 2. Chase the player
        String bestMove = "WAIT";
        double minDst = 9999;
        
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        String[] names = {"MOVE_UP", "MOVE_DOWN", "MOVE_LEFT", "MOVE_RIGHT"};

        for(int i=0; i<4; i++) {
            int tx = x + dx[i];
            int ty = y + dy[i];

            if (isValid(tx, ty, map)) {
                double d = playerPos.distance(tx, ty);
                if (d < minDst) {
                    minDst = d;
                    bestMove = names[i];
                }
            }
        }
        return bestMove;
    }
}