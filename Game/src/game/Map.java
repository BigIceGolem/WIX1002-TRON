public class Map {
    protected int rows;
    protected int cols;
    protected String[][] board;
    
    // Constants
    public static final String OBSTACLE = "X";
    public static final String SPEED_RAMP = "»"; 
    public static final String TRAIL = "T"; // New constant for trails
    public static final String EMPTY = " ";

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
        if (isValid(r, c)) board[r][c] = icon;
    }
    
    public String getIconAt(int r, int c) {
        if (isValid(r, c)) return board[r][c];
        return "OUT_OF_BOUNDS";
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public int getRows() { 
        return rows; 
    }
    public int getCols() { 
        return cols; 
    }
}
