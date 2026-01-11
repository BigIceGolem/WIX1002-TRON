package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList; 
import java.util.List;
import java.util.HashMap;
import java.io.File;

public class TronGUI extends JFrame {
    
    private final int CELL_SIZE = 20; 
    private GamePanel gamePanel;
    private JLabel statusLabel;
    
    // VISUAL SETTINGS
    private final Color COLOR_OBSTACLE = new Color(138, 43, 226); 
    private final int MAX_TRAIL_LENGTH = 30; 
    
    // Character Colors
    private final Color TRON_BODY = Color.BLUE;
    private final Color TRON_TRAIL = Color.CYAN.darker();
    private final Color KEVIN_BODY = new Color(135, 206, 250); 
    private final Color KEVIN_TRAIL = Color.WHITE;
    
    private Arena myMap;
    private BasicMechanic player; 
    private List<Disc> discPool = new ArrayList<>();
    
    private int currentLevel = 1; 
    private List<Enemy> enemies = new ArrayList<>();
    private HashMap<Point, Enemy> enemyTrails = new HashMap<>(); 
    
    private boolean playerHasMoved = false; 
    private Timer gameTimer;
    private long lastHitTime = 0;
    private int enemyMoveCounter = 0;

    public TronGUI(Character loadedPlayer) {
        this.player = (BasicMechanic) loadedPlayer;
        
        this.setTitle("Tron - Campaign Mode");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(40 * CELL_SIZE, 40 * CELL_SIZE));
        this.add(gamePanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Loading..."); 
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(Color.DARK_GRAY);
        statusLabel.setOpaque(true);
        this.add(statusLabel, BorderLayout.SOUTH);

        setupLevel(currentLevel); 

        this.pack();
        this.setLocationRelativeTo(null); 
        this.setVisible(true);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> player.setDirection("w");
                    case KeyEvent.VK_S -> player.setDirection("s");
                    case KeyEvent.VK_A -> player.setDirection("a");
                    case KeyEvent.VK_D -> player.setDirection("d");
                    case KeyEvent.VK_SPACE -> fireDisc(); 
                    case KeyEvent.VK_R -> restartLevel();    
                }
            }
        });

        gameTimer = new Timer(50, (ActionEvent e) -> gameLoop());
        gameTimer.start();
        
        showStory(1);
    }
    
    private void fireDisc() {
        for (Disc d : discPool) {
            if (d.canThrow()) {
                d.throwDisc(player.getRow(), player.getCol(), player.getCurrentDir());
                updateStatus(); 
                return; 
            }
        }
    }
    
    private void setupLevel(int level) {
        myMap = new Arena(40, 40, level); 
        player.undoPosition(20, 20); 
        player.setDirection("");
        player.getTrailHistory().clear(); 
        playerHasMoved = false;
        
        discPool.clear();
        int ammoCount = player.getDiscs(); 
        for (int i = 0; i < ammoCount; i++) {
            discPool.add(new Disc(player.getName()));
        }
        
        spawnEnemies(level);
        updateStatus();
    }
    
    private void spawnEnemies(int level) {
        enemies.clear();
        enemyTrails.clear();
        
        if (level == 1) {
            enemies.add(new Koura_LowEnemy(2, 2, 0x00FF00)); 
            enemies.add(new Koura_LowEnemy(35, 35, 0x00FF00));
            enemies.add(new Sark_ModerateEnemy(35, 5, 0xFFFF00));
        } else if (level == 2) {
            enemies.add(new Sark_ModerateEnemy(5, 5, 0xFFFF00));
            enemies.add(new Sark_ModerateEnemy(35, 35, 0xFFFF00));
            enemies.add(new Rinzler_CleverEnemy(5, 35, 0xFF0000));
            enemies.add(new Koura_LowEnemy(35, 5, 0x00FF00));
        } else if (level == 3) { 
            enemies.add(new Rinzler_CleverEnemy(5, 5, 0xFF0000));
            enemies.add(new Rinzler_CleverEnemy(35, 35, 0xFF0000));
            enemies.add(new Sark_ModerateEnemy(5, 35, 0xFFFF00));
            enemies.add(new Sark_ModerateEnemy(35, 5, 0xFFFF00));
            enemies.add(new Koura_LowEnemy(20, 5, 0x00FF00));
        } else { 
            enemies.add(new Clu_BrilliantEnemy(20, 5, 0xFFD700)); 
            enemies.add(new Rinzler_CleverEnemy(5, 5, 0xFF0000));
            enemies.add(new Rinzler_CleverEnemy(35, 5, 0xFF0000));
            enemies.add(new Rinzler_CleverEnemy(5, 35, 0xFF0000));
            enemies.add(new Rinzler_CleverEnemy(35, 35, 0xFF0000));
        }
    }

    private void gameLoop() {
        if (!playerHasMoved) {
            if (!player.getCurrentDir().equals("")) playerHasMoved = true;
            else { gamePanel.repaint(); return; }
        }

        if (!player.getCurrentDir().equals("")) {
            int oldR = player.getRow();
            int oldC = player.getCol();
            
            player.getTrailHistory().add(new Point(oldC, oldR)); 
            if (player.getTrailHistory().size() > MAX_TRAIL_LENGTH) {
                Point tail = player.getTrailHistory().removeFirst(); 
                String iconAtTail = myMap.getIconAt(tail.y, tail.x);
                if (iconAtTail.equals("^") || iconAtTail.equals("v") || 
                    iconAtTail.equals("<") || iconAtTail.equals(">")) {
                     myMap.setLocation(tail.y, tail.x, Map.EMPTY); 
                }
            }
            
            player.move(); 

            String hitTarget = myMap.getIconAt(player.getRow(), player.getCol());

            if (hitTarget.equals("OUT_OF_BOUNDS")) {
                if (currentLevel == 3) { player.takeDamage(999); endGame("FELL OFF THE GRID!"); return; } 
                else { handleDamage(0.5, oldR, oldC); return; }
            } 

            boolean isObstacle = hitTarget.equals(Map.OBSTACLE);
            boolean isTrail = hitTarget.equals("^") || hitTarget.equals("#") || hitTarget.equals("<") || hitTarget.equals(">");
            boolean isEnemyTrail = hitTarget.equals("B") || hitTarget.equals("C") || hitTarget.equals("M") || hitTarget.equals("L") || hitTarget.equals("E");

            if (isObstacle || isTrail || isEnemyTrail) {
                handleDamage(0.5, oldR, oldC);
                return;
            } 

            if (hitTarget.equals(Map.SPEED_RAMP)) gameTimer.setDelay(25);
            else gameTimer.setDelay(50);

            myMap.setLocation(player.getRow(), player.getCol(), player.jetWallIcon());
        }

        enemyMoveCounter++;
        if (enemyMoveCounter >= 2) {
            moveEnemies(new Point(player.getCol(), player.getRow()));
            enemyMoveCounter = 0;
        }
        checkEnemyCollisions(new Point(player.getCol(), player.getRow()));
        
        if (enemies.isEmpty()) { advanceLevel(); return; }

        for (Disc d : discPool) {
            d.update();
            if (d.isOnGround() && player.getRow() == d.getRow() && player.getCol() == d.getCol()) {
                d.pickUp();
                updateStatus();
            }
        }
        
        updateStatus();
        gamePanel.repaint(); 
    }
    
    private void advanceLevel() {
        gameTimer.stop();
        
        // 1. Show Cutscene (Now with Error Reporting)
        if (player.hasPendingCutscene()) {
            showResizedCutscene(player.getPendingTitle(), player.getPendingMessage(), player.getPendingImagePath());
            player.clearPendingCutscene(); 
        }
        
        // 2. Offer Switch (Now Independent of Cutscene success)
        if (player.getLevel() >= 5 && player.getName().equals("Tron")) {
            offerCharacterSwitch();
        }
        
        JOptionPane.showMessageDialog(this, "SECTOR CLEARED! Advancing...");
        
        currentLevel++;
        if (currentLevel > 4) {
            JOptionPane.showMessageDialog(this, "VICTORY! The Grid is saved.\nFinal XP: " + player.getXP());
            System.exit(0);
        } else {
            showStory(currentLevel); 
            setupLevel(currentLevel);
            gameTimer.start();
        }
    }
    
    // --- UPDATED: PATH DETECTIVE ---
    private void showResizedCutscene(String title, String msg, String path) {
        File imgFile = new File(path);
        
        // Debug Print to Console
        System.out.println("Looking for image at: " + imgFile.getAbsolutePath());
        
        if (imgFile.exists()) {
            ImageIcon originalIcon = new ImageIcon(path);
            Image img = originalIcon.getImage();
            Image newImg = img.getScaledInstance(500, -1, java.awt.Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(newImg);
            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE, resizedIcon);
        } else {
            // Popup the Error so you can see the path
            String errorMsg = msg + "\n\n[ERROR: Image Missing]\nI looked here:\n" + imgFile.getAbsolutePath();
            JOptionPane.showMessageDialog(this, errorMsg, title, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void offerCharacterSwitch() {
        String msg = "Kevin Flynn has been unlocked!\n\n" +
                     "Current: TRON (Speed Focus)\n" +
                     "New: KEVIN FLYNN (Handling Focus + Extra Ammo)\n\n" +
                     "Who will you control?";
                     
        Object[] options = {"Keep Tron", "Switch to Kevin"};
        int choice = JOptionPane.showOptionDialog(this, msg, "Character Select",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, options, options[1]);
                
        if (choice == 1) { 
            double currentXP = player.getXP();
            double currentLives = player.getHealth();
            Character newKevin = CharacterStats.loadCharacter("Kevin");
            if (newKevin == null) newKevin = new KevinFlynn(); 
            
            newKevin.setLives(currentLives);
            newKevin.gainXP((int)currentXP); 
            this.player = (BasicMechanic) newKevin; 
            setupLevel(currentLevel); 
            JOptionPane.showMessageDialog(this, "Switched to KEVIN FLYNN.\nStats loaded & updated.");
        }
    }

    private void showStory(int level) {
        String msg = switch(level) {
            case 1 -> "System Initialized. Survive the basic grid.";
            case 2 -> "Intrusion Detected. Sark's sentries inbound.";
            case 3 -> "Warning: Open Grid. Do not fall off!";
            case 4 -> "The Source. Clu is waiting.";
            default -> "Ready?";
        };
        JOptionPane.showMessageDialog(this, msg, "Level " + level, JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDamage(double damage, int oldR, int oldC) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime > 500) { 
             player.takeDamage(damage); 
             lastHitTime = currentTime; 
        }
        if (player.getHealth() <= 0) endGame("GAME OVER!");
        else {
            player.undoPosition(oldR, oldC);
            player.setDirection("");
        }
    }
    
    private void updateStatus() {
        int active = 0;
        for(Disc d : discPool) if(d.canThrow()) active++;
        statusLabel.setText(" Lives: " + player.getHealth() + " | Ammo: " + active + "/" + player.getDiscs() + " | XP: " + (int)player.getXP() + " | Level: " + player.getLevel() + " | Enemies: " + enemies.size());
    }

    private void moveEnemies(Point playerPos) {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            int oldX = enemy.x; int oldY = enemy.y;
            
            enemy.getTrailHistory().add(new Point(oldX, oldY));
            if (enemy.getTrailHistory().size() > MAX_TRAIL_LENGTH) {
                Point tail = enemy.getTrailHistory().removeFirst();
                String checkIcon = myMap.getIconAt(tail.y, tail.x);
                if (checkIcon.equals("B") || checkIcon.equals("C") || checkIcon.equals("M") || checkIcon.equals("L") || checkIcon.equals("E")) {
                    myMap.setLocation(tail.y, tail.x, Map.EMPTY);
                }
            }
            
            String move = enemy.makeMove(myMap, playerPos, enemies);
            
            if (move.startsWith("MOVE")) {
                int newX = enemy.x; int newY = enemy.y;
                switch (move) {
                    case "MOVE_UP" -> newY--; case "MOVE_DOWN" -> newY++;
                    case "MOVE_LEFT" -> newX--; case "MOVE_RIGHT" -> newX++;
                }
                if (isValidEnemyMove(oldX, oldY, newX, newY, enemy)) {
                    myMap.setLocation(oldY, oldX, getEnemyTrailIcon(enemy));
                    enemy.x = newX; enemy.y = newY;
                } else {
                    killEnemy(enemy, i); 
                }
            }
        }
    }
    
    private void checkEnemyCollisions(Point p) {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            if (p.x == e.x && p.y == e.y) {
                player.takeDamage(1.0);
                killEnemy(e, i);
                if (player.getHealth() <= 0) endGame("Killed by " + e.getName());
            }
        }
    }
    
    private void killEnemy(Enemy enemy, int index) {
        myMap.setLocation(enemy.y, enemy.x, getEnemyTrailIcon(enemy));
        enemies.remove(index);
        
        int xpGain = 100;
        if (enemy instanceof Clu_BrilliantEnemy) xpGain = 1000;
        else if (enemy instanceof Rinzler_CleverEnemy) xpGain = 500;
        
        player.gainXP(xpGain); 
        updateStatus(); 
    }
    
    private boolean isValidEnemyMove(int oldX, int oldY, int newX, int newY, Enemy enemy) {
        if (newX < 0 || newX >= 40 || newY < 0 || newY >= 40) return false;
        String cell = myMap.getIconAt(newY, newX);
        return !(cell.equals(Map.OBSTACLE) || cell.equals("B") || cell.equals("C") || cell.equals("M") || cell.equals("L") || cell.equals("E") || cell.equals("^") || cell.equals("<"));
    }
    
    private String getEnemyTrailIcon(Enemy e) {
        if (e instanceof Clu_BrilliantEnemy) return "B";
        if (e instanceof Rinzler_CleverEnemy) return "C";
        if (e instanceof Sark_ModerateEnemy) return "M";
        return "L";
    }

    private void endGame(String msg) {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, msg);
        System.exit(0);
    }
    
    private void restartLevel() {
        setupLevel(currentLevel);
        gamePanel.repaint();
    }
    
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (int r = 0; r < myMap.getRows(); r++) {
                for (int c = 0; c < myMap.getCols(); c++) {
                    String icon = myMap.getIconAt(r, c);
                    int x = c * CELL_SIZE; int y = r * CELL_SIZE;

                    if (icon.equals(Map.OBSTACLE)) { g.setColor(COLOR_OBSTACLE); g.fillRect(x, y, CELL_SIZE, CELL_SIZE); } 
                    else if (icon.equals(Map.SPEED_RAMP)) { g.setColor(Color.GREEN); g.fillRect(x, y, CELL_SIZE, CELL_SIZE); }
                    else if (icon.equals("B")) { g.setColor(new Color(200, 160, 0)); g.fillRect(x, y, CELL_SIZE, CELL_SIZE); } 
                    else if (icon.equals("C")) { g.setColor(new Color(180, 0, 0)); g.fillRect(x, y, CELL_SIZE, CELL_SIZE); } 
                    else if (icon.equals("M")) { g.setColor(new Color(180, 180, 0)); g.fillRect(x, y, CELL_SIZE, CELL_SIZE); } 
                    else if (icon.equals("L")) { g.setColor(new Color(0, 150, 0)); g.fillRect(x, y, CELL_SIZE, CELL_SIZE); } 
                    else if (!icon.equals(Map.EMPTY)) { 
                        boolean isPlayerTrail = icon.equals("^") || icon.equals("v") || icon.equals("<") || icon.equals(">");
                        if (isPlayerTrail) {
                            if (player.getName().contains("Kevin")) g.setColor(KEVIN_TRAIL);
                            else g.setColor(TRON_TRAIL);
                        } else {
                            g.setColor(Color.CYAN.darker()); 
                        }
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE); 
                    }
                }
            }
            
            for (Disc d : discPool) {
                if (d.isActive() || d.isOnGround()) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(d.getCol() * CELL_SIZE + 2, d.getRow() * CELL_SIZE + 2, 16, 16); 
                }
            }

            for (Enemy enemy : enemies) {
                Color bodyColor = new Color(enemy.color).brighter();
                g.setColor(bodyColor);
                g.fillRect(enemy.getX() * CELL_SIZE, enemy.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.WHITE); 
                g.fillRect(enemy.getX() * CELL_SIZE + 5, enemy.getY() * CELL_SIZE + 5, 10, 10);
            }
            
            if (player.getName().contains("Kevin")) g.setColor(KEVIN_BODY);
            else g.setColor(TRON_BODY);
            g.fillRect(player.getCol() * CELL_SIZE, player.getRow() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }
}