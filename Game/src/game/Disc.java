package game; // FIXED: Matches your project

public class Disc {
    private int col, row; // Renamed from x,y to col,row to match your Map system
    private int startCol, startRow; 
    private int dCol, dRow; // Velocity
    private boolean isThrown; 
    private boolean onGround; 
    private String ownerName; 
    
    // Cooldown logic
    private long throwTime;
    private static final long COOLDOWN_TIME = 2000; // 2 seconds cooldown (5s is too long!)
    
    public Disc(String ownerName){
        this.ownerName = ownerName;
        this.isThrown = false;
        this.onGround = false;
        // Start ready to throw
        this.throwTime = System.currentTimeMillis() - COOLDOWN_TIME;
    }

    public boolean canThrow(){
        long currentTime = System.currentTimeMillis();
        boolean cooldownOver = (currentTime - throwTime) >= COOLDOWN_TIME;
        boolean hasDisc = !isThrown && !onGround;
        return cooldownOver && hasDisc;
    }

    // Changed x,y to col,row to match the grid system
    public void throwDisc(int startRow, int startCol, String facingDirection){
        if(canThrow()){
            this.startRow = startRow;
            this.startCol = startCol;
            this.row = startRow;
            this.col = startCol;
            
            this.dRow = 0;
            this.dCol = 0;
            
            // Set velocity based on "w", "a", "s", "d"
            if (facingDirection.equals("w")) {      
                this.dRow = -1; 
            } else if (facingDirection.equals("s")) { 
                this.dRow = 1;  
            } else if (facingDirection.equals("a")) { 
                this.dCol = -1; 
            } else if (facingDirection.equals("d")) { 
                this.dCol = 1;  
            } else {
                return; 
            }

            this.isThrown = true;
            this.throwTime = System.currentTimeMillis();
            System.out.println("BOOM! Disc Thrown.");
        }
    }

    public void update(){
        if (isThrown) {
            // Move the disc
            col += dCol;
            row += dRow;

            // Range Check (Max 6 blocks)
            int distance = Math.abs(col - startCol) + Math.abs(row - startRow);
            if (distance >= 6) {
                land();
            }
        }
    }

    public void land(){
        isThrown = false;
        onGround = true;
        dCol = 0; 
        dRow = 0;
    }
    
    public void pickUp() {
        if (onGround) {
            onGround = false;
            isThrown = false;
            System.out.println("Disc Retrieved!");
        }
    }

    // Getters for the GUI to draw it
    public int getCol() { return col; }
    public int getRow() { return row; }
    public boolean isActive() { return isThrown; }
    public boolean isOnGround() { return onGround; }
}