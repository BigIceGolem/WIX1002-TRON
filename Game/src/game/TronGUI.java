package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList; 
import java.util.List;      

public class TronGUI extends JFrame {
    
    // Settings
    private final int CELL_SIZE = 20; 
    
    private GamePanel gamePanel;
    private JLabel statusLabel;
    
    // Game Objects
    private Arena myMap;
    private BasicMechanic player; 
    private Disc myDisc; 
    private EnemyBot enemy; 
    
    private Timer gameTimer;
    private long lastHitTime = 0;

    public TronGUI() {
        // 1. Setup Logic
        setupGameObjects(); 

        // 2. Setup GUI
        this.setTitle("Tron - Color Remastered");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(myMap.getCols() * CELL_SIZE, myMap.getRows() * CELL_SIZE));
        this.add(gamePanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" Health: " + player.getHealth() + " | WASD to Move | SPACE to Throw | R to Restart");
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
    
    private void setupGameObjects() {
        myMap = new Arena(40, 40, 4); 
        player = new BasicMechanic();
        myDisc = new Disc("Tron");
        enemy = new LowEnemy(5, 5, 1); 
    }

    private void gameLoop() {
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
            boolean isEnemyTrail = hitTarget.equals("E_TRAIL");

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

        // --- 2. ENEMY LOGIC (FIXED) ---
        List<EnemyBot> bots = new ArrayList<>();
        bots.add(enemy);

        String moveChoice = enemy.makeMove(myMap, new Point(player.getCol(), player.getRow()), bots);

        int oldEX = enemy.getX(); 
        int oldEY = enemy.getY(); 
        
        // Calculate the NEW position first (Don't move yet)
        int newEX = oldEX;
        int newEY = oldEY;

        switch (moveChoice) {
            case "MOVE_UP" -> newEY--;
            case "MOVE_DOWN" -> newEY++;
            case "MOVE_LEFT" -> newEX--;
            case "MOVE_RIGHT" -> newEX++;
        }

        // BOUNDARY CHECK: Only move if inside the map (0 to 39)
        if (newEX >= 0 && newEX < myMap.getCols() && newEY >= 0 && newEY < myMap.getRows()) {
            enemy.x = newEX;
            enemy.y = newEY;
            
            // Leave a YELLOW trail behind using our new code "E_TRAIL"
            myMap.setLocation(oldEY, oldEX, "E_TRAIL");
        } 
        // If invalid (out of bounds), the enemy simply waits this turn.

        // --- 3. DISC LOGIC ---
        myDisc.update();
        
        if (myDisc.isOnGround() && player.getRow() == myDisc.getRow() && player.getCol() == myDisc.getCol()) {
            myDisc.pickUp();
        }
        
        statusLabel.setText(" Health: " + player.getHealth() + " | WASD to Move | SPACE to Throw | R to Restart");
        gamePanel.repaint(); 
    }

    private void endGame(String msg) {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, msg);
        System.exit(0);
    }
    
    private void resetGame() {
        setupGameObjects();
        player.setDirection(""); 
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
                    // NEW: Draw Enemy Trail as YELLOW
                    else if (icon.equals("E_TRAIL")) {
                        g.setColor(Color.YELLOW); 
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(Color.ORANGE); 
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

            // Draw Enemy (Magenta to stand out)
            g.setColor(Color.MAGENTA); 
            g.fillRect(enemy.getX() * CELL_SIZE, enemy.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.WHITE); 
            g.fillRect((enemy.getX() * CELL_SIZE) + 5, (enemy.getY() * CELL_SIZE) + 5, 10, 10);

            // Draw Player
            g.setColor(Color.BLUE); 
            g.fillRect(player.getCol() * CELL_SIZE, player.getRow() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.WHITE); 
            g.fillRect((player.getCol() * CELL_SIZE) + 8, (player.getRow() * CELL_SIZE) + 8, 4, 4);
        }
    }
}