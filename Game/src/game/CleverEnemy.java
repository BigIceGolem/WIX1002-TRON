package game;

import java.awt.Point;
import java.util.List;

public class CleverEnemy extends EnemyBot {

    public CleverEnemy(int x, int y, int c) { 
        super(x, y, c, "CleverEnemy"); 
    }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        EnemyBot partner = null;
        for(EnemyBot b : allBots) {
            if (b != this) { 
                partner = b; 
                break; 
            }
        }

        Point target = playerPos;
        if (partner != null) {
            int offsetX = (partner.x < playerPos.x) ? 5 : -5;
            target = new Point(playerPos.x + offsetX, playerPos.y);
        }

        int diffX = target.x - x;
        int diffY = target.y - y;

        // Try preferred direction first
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0 && isValid(x+1, y, map)) return "MOVE_RIGHT";
            if (diffX < 0 && isValid(x-1, y, map)) return "MOVE_LEFT";
            // Try other directions if preferred is blocked
            if (isValid(x, y+1, map)) return "MOVE_DOWN";
            if (isValid(x, y-1, map)) return "MOVE_UP";
        } else {
            if (diffY > 0 && isValid(x, y+1, map)) return "MOVE_DOWN";
            if (diffY < 0 && isValid(x, y-1, map)) return "MOVE_UP";
            // Try other directions if preferred is blocked
            if (isValid(x+1, y, map)) return "MOVE_RIGHT";
            if (isValid(x-1, y, map)) return "MOVE_LEFT";
        }

        // If all safe moves are blocked, try any move
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        String[] moves = {"MOVE_UP", "MOVE_DOWN", "MOVE_LEFT", "MOVE_RIGHT"};
        
        for (int i = 0; i < 4; i++) {
            int tx = x + dx[i];
            int ty = y + dy[i];
            if (tx >= 0 && tx < map.getCols() && ty >= 0 && ty < map.getRows()) {
                return moves[i];
            }
        }

        return "MOVE_UP";
    }
}
