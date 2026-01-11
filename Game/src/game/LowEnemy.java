package game;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class LowEnemy extends EnemyBot {

    public LowEnemy(int x, int y, int c) { 
        super(x, y, c, "LowEnemy"); 
    }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        List<String> validMoves = new ArrayList<>();
        
        if (isValid(x, y-1, map)) validMoves.add("MOVE_UP");
        if (isValid(x, y+1, map)) validMoves.add("MOVE_DOWN");
        if (isValid(x-1, y, map)) validMoves.add("MOVE_LEFT");
        if (isValid(x+1, y, map)) validMoves.add("MOVE_RIGHT");

        // If we have valid moves, pick one randomly
        if (!validMoves.isEmpty()) {
            Random rand = new Random();
            return validMoves.get(rand.nextInt(validMoves.size()));
        }
        
        // If no valid moves, try any move (even into trail/obstacle)
        // This will cause death
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
