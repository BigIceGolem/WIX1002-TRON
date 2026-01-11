package game;

import java.awt.Point;
import java.util.List;

public class BrilliantEnemy extends EnemyBot {

    public BrilliantEnemy(int x, int y, int c) { 
        super(x, y, c, "BrilliantEnemy");
    }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        updateStatus();
        
        if (canShoot(playerPos) && diskAmmo > 0 && cooldownTimer == 0) {
            if (playerPos.y < y) return "THROW_UP";
            if (playerPos.y > y) return "THROW_DOWN";
            if (playerPos.x < x) return "THROW_LEFT";
            if (playerPos.x > x) return "THROW_RIGHT";
        }

        String bestMove = "MOVE_UP";
        int maxScore = -9999;
        boolean foundValidMove = false;

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        String[] moves = {"MOVE_UP", "MOVE_DOWN", "MOVE_LEFT", "MOVE_RIGHT"};

        for (int i = 0; i < 4; i++) {
            int tx = x + dx[i];
            int ty = y + dy[i];
            int score = 0;

            if (!isValid(tx, ty, map)) {
                continue;
            }
            
            foundValidMove = true;

            if (map.isSpeedRamp(ty, tx)) {
                score += 50;
            }

            if (map.isDisk(ty, tx) && diskAmmo < 2) {
                score += 30;
            }

            for (EnemyBot bot : allBots) {
                if (bot != this && bot.x == tx && bot.y == ty) {
                    score -= 100;
                }
            }

            double distanceToPlayer = Math.sqrt(
                Math.pow(playerPos.x - tx, 2) + Math.pow(playerPos.y - ty, 2)
            );
            
            if (distanceToPlayer < 3) {
                score -= (int)(50 - distanceToPlayer * 10);
            } else if (distanceToPlayer > 8) {
                score -= (int)(distanceToPlayer - 8);
            } else {
                score += 20;
            }

            score += (int)(Math.random() * 15);

            if (map.isTrail(ty, tx)) {
                score -= 10;
            }

            if (map.isEmpty(ty, tx)) {
                score += 5;
            }

            if (score > maxScore) {
                maxScore = score;
                bestMove = moves[i];
            }
        }

        // If no valid safe moves, try any move (even into trail/obstacle)
        if (!foundValidMove) {
            for (int i = 0; i < 4; i++) {
                int tx = x + dx[i];
                int ty = y + dy[i];
                if (tx >= 0 && tx < map.getCols() && ty >= 0 && ty < map.getRows()) {
                    return moves[i];
                }
            }
        }

        return bestMove;
    }
}
