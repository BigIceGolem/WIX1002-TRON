package game;

import java.awt.Point;
import java.util.List;

public class BrilliantEnemy extends EnemyBot {

    public BrilliantEnemy(int x, int y, int c) { super(x, y, c, "BrilliantEnemy"); }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        
        // 1. Aggressive Attack
        if (canShoot(playerPos)) {
            if (playerPos.y < y) return "THROW_UP";
            if (playerPos.y > y) return "THROW_DOWN";
            if (playerPos.x < x) return "THROW_LEFT";
            if (playerPos.x > x) return "THROW_RIGHT";
        }

        // 2. Weighted Decision Making
        String bestMove = "MOVE_UP";
        int maxScore = -9999;

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        String[] moves = {"MOVE_UP", "MOVE_DOWN", "MOVE_LEFT", "MOVE_RIGHT"};

        for (int i=0; i<4; i++) {
            int tx = x + dx[i];
            int ty = y + dy[i];
            int score = 0;

            if (!isValid(tx, ty, map)) continue; 

            // --- SCORING ---
            // Note: Map methods take (row, col) which is (ty, tx)
            if (map.isSpeedRamp(ty, tx)) score += 50;
            if (map.isDisk(ty, tx)) score += (diskAmmo == 0) ? 40 : 10;

            double dist = playerPos.distance(tx, ty);
            score -= (int)dist; 

            if (score > maxScore) {
                maxScore = score;
                bestMove = moves[i];
            }
        }

        return bestMove;
    }
}