public class Arena extends Map {

    public Arena(int rows, int cols, int levelNumber) {
        super(rows, cols);
        loadLevel(levelNumber);
    }

    private void loadLevel(int level) {
        genEmptyBoard(); // Clear first
        switch (level) {
            case 1 -> designLevel1(); 
            case 2 -> designLevel2(); 
            case 3 -> designLevel3();
            case 4 -> designLevel4(); // Added your random level
            default -> designLevel1();
        }
    }

    // --- LEVEL DESIGNS (Same as your logic) ---
    private void designLevel1() {
        addRectObstacle(4, 4, 6, 2);   addRectObstacle(4, 4, 2, 6);   
        addRectObstacle(4, 34, 6, 2);  addRectObstacle(4, 30, 2, 6);  
        addRectObstacle(30, 4, 6, 2);  addRectObstacle(34, 4, 2, 6);  
        addRectObstacle(30, 34, 6, 2); addRectObstacle(34, 30, 2, 6); 
        addRectObstacle(13, 17, 2, 7);  addRectObstacle(25, 17, 2, 7); 
        addRectObstacle(17, 15, 6, 2);  addRectObstacle(17, 24, 6, 2);
        setLocation(4, 19, SPEED_RAMP);   setLocation(35, 19, SPEED_RAMP);
        setLocation(19, 4, SPEED_RAMP);   setLocation(19, 35, SPEED_RAMP);
    }

    private void designLevel2() {
        for(int i = 0; i < 8; i++) {
            setLocation(i, i, OBSTACLE);           setLocation(i, 39-i, OBSTACLE);
            setLocation(39-i, i, OBSTACLE);       setLocation(39-i, 39-i, OBSTACLE);
        }
        addRectObstacle(10, 19, 8, 2);  addRectObstacle(18, 16, 2, 8);
        addRectObstacle(22, 19, 8, 2);  addRectObstacle(20, 16, 2, 8);
        setLocation(19, 1, SPEED_RAMP); setLocation(19, 38, SPEED_RAMP);
    }

    private void designLevel3() {
        addRectObstacle(2, 2, 2, 36);   
        addRectObstacle(2, 36, 30, 2);  
        addRectObstacle(36, 6, 2, 32);  
        addRectObstacle(10, 2, 28, 2);  
        addRectObstacle(10, 10, 4, 4); addRectObstacle(26, 26, 4, 4);
        for (int i = 12; i < 28; i++) {
            setLocation(i, 18, SPEED_RAMP); setLocation(18, i, SPEED_RAMP);
        }
    }

    private void designLevel4() {
        int obstaclesPlaced = 0;
        while (obstaclesPlaced < 15) { 
            int r = (int) (Math.random() * 35) + 2; 
            int c = (int) (Math.random() * 35) + 2;
            if (Math.abs(r - 20) > 5 || Math.abs(c - 20) > 5) {
                addRectObstacle(r, c, 2, 2);
                obstaclesPlaced++;
            }
        }
        int rampsPlaced = 0;
        while (rampsPlaced < 20) {
            int r = (int) (Math.random() * 38) + 1;
            int c = (int) (Math.random() * 38) + 1;
            if (getIconAt(r, c).equals(EMPTY)) {
                setLocation(r, c, SPEED_RAMP);
                rampsPlaced++;
            }
        }
    }

    private void addRectObstacle(int r, int c, int h, int w) {
        for (int i = r; i < r + h; i++) {
            for (int j = c; j < c + w; j++) setLocation(i, j, OBSTACLE);
        }
    }
}
