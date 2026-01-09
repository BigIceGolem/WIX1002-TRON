import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TronGUI extends JFrame {
    
    // Settings
    private final int CELL_SIZE = 20; // Size of each block in pixels
    
    private GamePanel gamePanel;
    private JLabel statusLabel;
    private Arena myMap;
    private BasicMechanic player;
    private Timer gameTimer;
    private long lastHitTime = 0;

    public TronGUI() {
        // 1. Setup Logic
        myMap = new Arena(40, 40, 4); // Change 1 to 2, 3, or 4 for levels
        player = new BasicMechanic();

        // 2. Setup GUI
        this.setTitle("Tron - Color Remastered");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Create the custom drawing panel
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(myMap.getCols() * CELL_SIZE, myMap.getRows() * CELL_SIZE));
        this.add(gamePanel, BorderLayout.CENTER);

        // Add a status bar at the bottom
        statusLabel = new JLabel(" Health: " + player.getHealth() + " | Use WASD to move");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(Color.DARK_GRAY);
        statusLabel.setOpaque(true);
        this.add(statusLabel, BorderLayout.SOUTH);

        // Pack fits the window to the panel size automatically
        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
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
                }
            }
        });

        // 4. Game Loop
        gameTimer = new Timer(100, (ActionEvent e) -> gameLoop());
        gameTimer.start();
    }

    private void gameLoop() {
        if (player.getCurrentDir().equals("")) return; 

        int oldR = player.getRow();
        int oldC = player.getCol();

        player.move(); 

        String hitTarget = myMap.getIconAt(player.getRow(), player.getCol());
        
        // --- LOGIC CHECKS ---
        
        // 1. Boundary Death
        if (hitTarget.equals("OUT_OF_BOUNDS")) {
            endGame("CRASHED INTO BOUNDARY!");
            return;
        } 

        // 2. Speed Ramps
        if (hitTarget.equals(Map.SPEED_RAMP)) {
            gameTimer.setDelay(50); // Fast!
        } else {
            gameTimer.setDelay(100); // Normal
        }

        // 3. Obstacles / Trails
        boolean isObstacle = hitTarget.equals(Map.OBSTACLE);
        boolean isTrail = hitTarget.equals("^") || hitTarget.equals("#") || hitTarget.equals("<") || hitTarget.equals(">");
        
        if (isObstacle || isTrail) {
            long currentTime = System.currentTimeMillis();
            
            // Invincibility check (500ms)
            if (currentTime - lastHitTime < 500) { 
                player.undoPosition(oldR, oldC);
                player.setDirection("");
                gamePanel.repaint();
                return;
            }

            player.takeDamage(0.5); 
            lastHitTime = currentTime; 
            statusLabel.setText(" Health: " + player.getHealth() + " | OUCH!");
            
            if (player.getHealth() <= 0) {
                endGame("GAME OVER! Health Depleted.");
                return;
            }

            // Bounce back
            player.undoPosition(oldR, oldC);
            player.setDirection("");         
            gamePanel.repaint();
            return; 
        } 
        
        // 4. Valid Move
        // Leave a trail
        myMap.setLocation(oldR, oldC, player.jetWallIcon());
        
        // Update graphics
        statusLabel.setText(" Health: " + player.getHealth());
        gamePanel.repaint(); // This calls paintComponent
    }

    private void endGame(String msg) {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, msg);
        System.exit(0);
    }

    // --- INNER CLASS FOR CUSTOM DRAWING ---
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw Background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw Map
            for (int r = 0; r < myMap.getRows(); r++) {
                for (int c = 0; c < myMap.getCols(); c++) {
                    String icon = myMap.getIconAt(r, c);
                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;

                    if (icon.equals(Map.OBSTACLE)) {
                        g.setColor(Color.RED); // OBSTACLE IS RED
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(100, 0, 0)); // Darker border
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    } 
                    else if (icon.equals(Map.SPEED_RAMP)) {
                        g.setColor(Color.GREEN); // RAMP IS GREEN
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    else if (!icon.equals(Map.EMPTY)) {
                        // This handles the trails (^ < > #)
                        g.setColor(Color.CYAN.darker()); 
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        g.setColor(Color.CYAN);
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                    
                    // Draw grid lines (optional, looks cool)
                    g.setColor(new Color(30, 30, 30));
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }

            // Draw Player (On top of everything)
            g.setColor(Color.BLUE); // PLAYER IS BLUE
            g.fillRect(player.getCol() * CELL_SIZE, player.getRow() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.WHITE); // Small dot in center of player
            g.fillRect((player.getCol() * CELL_SIZE) + 8, (player.getRow() * CELL_SIZE) + 8, 4, 4);
        }
    }
}
