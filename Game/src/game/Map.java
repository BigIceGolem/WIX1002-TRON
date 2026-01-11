package game;

public class Map {
    protected int rows;
    protected int cols;
    protected String[][] board;
    
    // Constants
    public static final String OBSTACLE = "X";
    public static final String SPEED_RAMP = "»"; 
    public static final String TRAIL = "T";
    public static final String EMPTY = " ";
    public static final String DISK = "D";
    public static final String ENEMY_TRAIL_BRILLIANT = "B";
    public static final String ENEMY_TRAIL_CLEVER = "C";
    public static final String ENEMY_TRAIL_MODERATE = "M";
    public static final String ENEMY_TRAIL_LOW = "L";
    public static final String ENEMY_TRAIL_DEFAULT = "E";;

    public Map(int row, int col) {
        this.rows = row;
        this.cols = col;
        this.board = new String[row][col];
        genEmptyBoard();
    }
    
    public void genEmptyBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = EMPTY; 
            }
        }
    }
    
    public void setLocation(int r, int c, String icon) {
        if (isValidPos(r, c)) board[r][c] = icon;
    }
    
    public String getIconAt(int r, int c) {
        if (isValidPos(r, c)) return board[r][c];
        return "OUT_OF_BOUNDS";
    }

    // --- NEW HELPER METHODS FOR AI ---
    public boolean isValidPos(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public boolean isWall(int r, int c) {
        if (!isValidPos(r, c)) return true; // Bounds are walls
        return board[r][c].equals(OBSTACLE) || board[r][c].equals(TRAIL);
    }
    public boolean isTrail(int r, int c) {
        if (!isValidPos(r, c)) return false;
            String icon = board[r][c];
            return icon.equals("^") || icon.equals("#") || 
            icon.equals("<") || icon.equals(">") ||
            icon.equals(TRAIL) ||
            icon.equals(ENEMY_TRAIL_BRILLIANT) ||
            icon.equals(ENEMY_TRAIL_CLEVER) ||
            icon.equals(ENEMY_TRAIL_MODERATE) ||
            icon.equals(ENEMY_TRAIL_LOW) ||
            icon.equals(ENEMY_TRAIL_DEFAULT);
    }

    public boolean isSpeedRamp(int r, int c) {
        return isValidPos(r, c) && board[r][c].equals(SPEED_RAMP);
    }
    
    public boolean isDisk(int r, int c) {
        return isValidPos(r, c) && board[r][c].equals(DISK);
    }
    
    public boolean isEmpty(int r, int c) {
        return isValidPos(r, c) && board[r][c].equals(EMPTY);
    }
    
   public boolean isEnemyTrail(int r, int c) {
        if (!isValidPos(r, c)) return false;
        String icon = board[r][c];
        return icon.equals(ENEMY_TRAIL_BRILLIANT) ||
           icon.equals(ENEMY_TRAIL_CLEVER) ||
           icon.equals(ENEMY_TRAIL_MODERATE) ||
           icon.equals(ENEMY_TRAIL_LOW) ||
           icon.equals(ENEMY_TRAIL_DEFAULT);
    }
    
    public void addDisk(int r, int c) {
        setLocation(r, c, DISK);
    }
    
    public void removeDisk(int r, int c) {
        if (isDisk(r, c)) {
            setLocation(r, c, EMPTY);
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
