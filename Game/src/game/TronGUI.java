package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList; 
import java.util.List;
import java.util.HashMap;  // <-- ADDED


public class TronGUI extends JFrame {
    
    // Settings
    private final int CELL_SIZE = 20; 
    
    private GamePanel gamePanel;
    private JLabel statusLabel;
    
    // Game Objects
    private Arena myMap;
    private BasicMechanic player; 
    private Disc myDisc;  
    private List<Enemy> enemies = new ArrayList<>();  // <-- ADDED: Changed from single enemy to list
    private int enemyMoveCounter = 0;                    // <-- ADDED: For enemy movement timing
    private HashMap<Point, Enemy> enemyTrails = new HashMap<>();  // <-- ADDED: Track enemy trails
    private boolean playerHasMoved = false;  // <-- ADD THIS LINE
    private Timer gameTimer;
    private long lastHitTime = 0;

    public TronGUI(Character loadedPlayer) {
        // 1. Setup Logic
        setupGameObjects(loadedPlayer); 

        // 2. Setup GUI
        this.setTitle("Tron - Color Remastered");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(myMap.getCols() * CELL_SIZE, myMap.getRows() * CELL_SIZE));
        this.add(gamePanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" Health: " + player.getHealth() + " | Enemies: " + enemies.size() +" | WASD to Move | SPACE to Throw | R to Restart");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(Color.DARK_GRAY);
        statusLabel.setOpaque(true);
        this.add(statusLabel, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null); 
        this.setVisible(true);

        // 3. Inputs
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> player.setDirection("w");
                    case KeyEvent.VK_S -> player.setDirection("s");
                    case KeyEvent.VK_A -> player.setDirection("a");
                    case KeyEvent.VK_D -> player.setDirection("d");
                    case KeyEvent.VK_SPACE -> {
                        myDisc.throwDisc(player.getRow(), player.getCol(), player.getCurrentDir());
                    }
                    case KeyEvent.VK_R -> resetGame();    
                }
            }
        });

        // 4. Game Loop
        gameTimer = new Timer(100, (ActionEvent e) -> gameLoop());
        gameTimer.start();
    }
    
    private void setupGameObjects(Character loadedPlayer) {
        myMap = new Arena(40, 40, 4); 
        this.player = (BasicMechanic) loadedPlayer;
        myDisc = new Disc("Tron");
        initializeEnemies(); 
    }
    
     private void initializeEnemies() {
        enemies.clear();
        enemyTrails.clear();
        
        // Add 4 different enemy types at corners
        enemies.add(new Clu_BrilliantEnemy(5, 5, 0xFFD700));    // Gold - top-left
        enemies.add(new Rinzler_CleverEnemy(35, 5, 0xFF0000));      // Red - top-right  
        enemies.add(new Sork_ModerateEnemy(5, 35, 0xFFFF00));    // Yellow - bottom-left
        enemies.add(new Koura_LowEnemy(35, 35, 0x00FF00));        // Green - bottom-right
    }

    private void gameLoop() {
        // --- 0. CHECK IF PLAYER HAS STARTED MOVING ---
        if (!playerHasMoved && !player.getCurrentDir().equals("")) {
            playerHasMoved = true;  // <-- Player started moving
        }
        
        // Don't move enemies until player has moved
        if (!playerHasMoved) {
            gamePanel.repaint();
            return;  // <-- Exit early
        }
        // --- 1. PLAYER LOGIC ---
        if (!player.getCurrentDir().equals("")) {
            int oldR = player.getRow();
            int oldC = player.getCol();
            
            player.move(); 

            String hitTarget = myMap.getIconAt(player.getRow(), player.getCol());

            // --- COLLISION LOGIC ---
            if (hitTarget.equals("OUT_OF_BOUNDS")) {
                endGame("CRASHED INTO BOUNDARY!");
                return;
            } 

            if (hitTarget.equals(Map.SPEED_RAMP)) {
                gameTimer.setDelay(50); 
            } else {
                gameTimer.setDelay(100); 
            }

            boolean isObstacle = hitTarget.equals(Map.OBSTACLE);
            boolean isTrail = hitTarget.equals("^") || hitTarget.equals("#") || hitTarget.equals("<") || hitTarget.equals(">");
            // Check for Enemy Trail (The new "E_TRAIL" marker)
            boolean isEnemyTrail = hitTarget.equals("B") || hitTarget.equals("C") ||
                                   hitTarget.equals("M") || hitTarget.equals("L") ||
                                   hitTarget.equals("E");


            if (isObstacle || isTrail || isEnemyTrail) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastHitTime < 500) { 
                    player.undoPosition(oldR, oldC); 
                    player.setDirection(""); 
                    gamePanel.repaint();
                    return;
                }
                player.takeDamage(1.0); 
                lastHitTime = currentTime; 
                statusLabel.setText(" Health: " + player.getHealth() + " | OUCH!");

                if (player.getHealth() <= 0) {
                    endGame("GAME OVER!");
                    return;
                }
                
                player.undoPosition(oldR, oldC);
                player.setDirection("");
                gamePanel.repaint();
                return;
            } 

            myMap.setLocation(player.getRow(), player.getCol(), player.jetWallIcon());
        }

        // --- 2. ENEMY LOGIC (UPDATED FROM CODE 1) ---
        enemyMoveCounter++;
        if (enemyMoveCounter >= 2) {
            moveEnemies(new Point(player.getCol(), player.getRow()));
            enemyMoveCounter = 0;
        }
        
        // Check collisions between player and enemies
        checkEnemyCollisions(new Point(player.getCol(), player.getRow()));
        // --- 3. DISC LOGIC ---
        myDisc.update();
        
        if (myDisc.isOnGround() && player.getRow() == myDisc.getRow() && player.getCol() == myDisc.getCol()) {
            myDisc.pickUp();
        }
        
        statusLabel.setText(" Health: " + player.getHealth() + " | WASD to Move | SPACE to Throw | R to Restart");
        gamePanel.repaint(); 
    }
    
        // Method to move all enemies
    private void moveEnemies(Point playerPos) {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            
            int oldX = enemy.x;
            int oldY = enemy.y;
            
            String move = enemy.makeMove(myMap, playerPos, enemies);
            
            if (move.startsWith("MOVE")) {
                int newX = enemy.x;
                int newY = enemy.y;
                
                switch (move) {
                    case "MOVE_UP" -> newY--;
                    case "MOVE_DOWN" -> newY++;
                    case "MOVE_LEFT" -> newX--;
                    case "MOVE_RIGHT" -> newX++;
                }
                
                // Check if move is valid
                if (isValidEnemyMove(oldX, oldY, newX, newY, enemy)) {
                    // Leave appropriate trail based on enemy type
                    String trailIcon = getEnemyTrailIcon(enemy);
                    myMap.setLocation(oldY, oldX, trailIcon);
                    enemy.x = newX;
                    enemy.y = newY;
                } else {
                    // Kill enemy if invalid move
                    killEnemy(enemy, i, "crashed!");
                }
            }
        }
    }
    
    // Helper method to get trail icon for each enemy type
    private String getEnemyTrailIcon(Enemy enemy) {
        if (enemy instanceof Clu_BrilliantEnemy) return "B";
        if (enemy instanceof Rinzler_CleverEnemy) return "C";
        if (enemy instanceof Sork_ModerateEnemy) return "M";
        if (enemy instanceof Koura_LowEnemy) return "L";
        return "E";  // Default
    }
    
    // Check if enemy move is valid
    private boolean isValidEnemyMove(int oldX, int oldY, int newX, int newY, Enemy enemy) {
        if (newX < 0 || newX >= myMap.getCols() || newY < 0 || newY >= myMap.getRows()) {
            return false;
        }
        
        String cell = myMap.getIconAt(newY, newX);
        
        // Check for obstacles and trails
        if (cell.equals(Map.OBSTACLE) ||
            cell.equals("^") || cell.equals("#") || cell.equals("<") || cell.equals(">") ||
            cell.equals("B") || cell.equals("C") || cell.equals("M") || cell.equals("L") || cell.equals("E")) {
            return false;
        }
        
        // Check for other enemies
        for (Enemy other : enemies) {
            if (other != enemy && other.x == newX && other.y == newY) {
                return false;
            }
        }
        
        // Check for player
        if (newX == player.getCol() && newY == player.getRow()) {
            return false;
        }
        
        return true;
    }
    
    // Check collisions between player and enemies
    private void checkEnemyCollisions(Point playerPos) {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            
            if (playerPos.x == enemy.x && playerPos.y == enemy.y) {
                player.takeDamage(1.0);
                statusLabel.setText(" Health: " + player.getHealth() + " | HIT BY " + enemy.getName() + "!");
                
                killEnemy(enemy, i, "was destroyed by player collision!");
                
                if (player.getHealth() <= 0) {
                    endGame("GAME OVER! Defeated by " + enemy.getName());
                    return;
                }
            }
        }
    }
    
    // Kill enemy method
    private void killEnemy(Enemy enemy, int index, String reason) {
        // Mark enemy death position as trail
        myMap.setLocation(enemy.y, enemy.x, getEnemyTrailIcon(enemy));
        
        // Remove enemy from list
        enemies.remove(index);
        
        // Update status
        statusLabel.setText(" Health: " + player.getHealth() + 
                          " | " + enemy.getName() + " " + reason + 
                          " | Enemies remaining: " + enemies.size());
        
        // Check for victory
        if (enemies.isEmpty()) {
            endGame("VICTORY! All enemies defeated!");
        }
    }

    private void endGame(String msg) {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, msg);
        System.exit(0);
    }
    
    private void resetGame() {
        setupGameObjects(this.player);
        player.setDirection(""); 
        playerHasMoved = false;  // <-- ADD THIS LINE
        statusLabel.setText(" Health: " + player.getHealth() + " | Game Reset! | R to Restart");
        gamePanel.repaint();
    }
    
    // --- DRAWING ---
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (int r = 0; r < myMap.getRows(); r++) {
                for (int c = 0; c < myMap.getCols(); c++) {
                    String icon = myMap.getIconAt(r, c);
                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;

                    if (icon.equals(Map.OBSTACLE)) {
                        g.setColor(Color.RED); 
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(100, 0, 0)); 
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    } 
                    else if (icon.equals(Map.SPEED_RAMP)) {
                        g.setColor(Color.GREEN); 
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    // Draw enemy trail colors 
                    else if (icon.equals("B")) {  // Brilliant enemy trail
                        g.setColor(new Color(255, 215, 0));  // Gold
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(200, 170, 0));
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    else if (icon.equals("C")) {  // Clever enemy trail
                        g.setColor(Color.RED);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(200, 0, 0));
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    else if (icon.equals("M")) {  // Moderate enemy trail
                        g.setColor(Color.YELLOW);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(200, 200, 0));
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    else if (icon.equals("L")) {  // Low enemy trail
                        g.setColor(Color.GREEN);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(0, 200, 0));
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    else if (icon.equals("E")) {  // Default enemy trail
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(200, 200, 200));
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    else if (!icon.equals(Map.EMPTY)) {
                        g.setColor(Color.CYAN.darker()); 
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(Color.CYAN);
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    g.setColor(new Color(30, 30, 30));
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
            
            if (myDisc.isActive() || myDisc.isOnGround()) {
                g.setColor(Color.YELLOW);
                int dx = myDisc.getCol() * CELL_SIZE;
                int dy = myDisc.getRow() * CELL_SIZE;
                g.fillOval(dx + 2, dy + 2, 16, 16); 
            }

                       // Draw all enemies (ADDED FROM CODE 1)
            for (Enemy enemy : enemies) {
                Color enemyColor = new Color(enemy.color);
                g.setColor(enemyColor);
                
                g.fillRect(enemy.getX() * CELL_SIZE, enemy.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                
                g.setColor(Color.WHITE);
                g.drawRect(enemy.getX() * CELL_SIZE, enemy.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                
                // Draw enemy initial
                g.setFont(new Font("Arial", Font.BOLD, 10));
                String enemyInitial = enemy.getName().substring(0, 1);
                g.drawString(enemyInitial, 
                           (enemy.getX() * CELL_SIZE) + 7, 
                           (enemy.getY() * CELL_SIZE) + 12);
            }
            // Draw Player
            g.setColor(Color.BLUE); 
            g.fillRect(player.getCol() * CELL_SIZE, player.getRow() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.WHITE); 
            g.fillRect((player.getCol() * CELL_SIZE) + 8, (player.getRow() * CELL_SIZE) + 8, 4, 4);
        }
    }
}
