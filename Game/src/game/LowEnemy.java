package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LowEnemy extends EnemyBot {

    public LowEnemy(int x, int y, int c) { super(x, y, c, "LowEnemy"); }

    @Override
    public String makeMove(Map map, Point playerPos, List<EnemyBot> allBots) {
        List<String> validMoves = new ArrayList<>();
        
        // 1. Check which moves don't hit a wall immediately
        if (isValid(x, y-1, map)) validMoves.add("MOVE_UP");
        if (isValid(x, y+1, map)) validMoves.add("MOVE_DOWN");
        if (isValid(x-1, y, map)) validMoves.add("MOVE_LEFT");
        if (isValid(x+1, y, map)) validMoves.add("MOVE_RIGHT");

        // 2. If stuck, default to UP
        if (validMoves.isEmpty()) return "MOVE_UP";

        // 3. Pick Randomly
        Random rand = new Random();
        return validMoves.get(rand.nextInt(validMoves.size()));
    }
}