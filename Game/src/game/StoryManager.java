package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class StoryManager extends JPanel {

    private TronGUI mainGame;
    private Image currentImage;
    private String currentText;
    private int sceneStep = 0;
    private boolean isActive = false;

    public StoryManager(TronGUI game) {
        this.mainGame = game;
        this.setBackground(new Color(0, 0, 0, 200)); // Semi-transparent black
        this.setLayout(new BorderLayout());

        // Click to advance to next video/text
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isActive) nextStep();
            }
        });
    }

    public void startIntro() {
        this.isActive = true;
        this.sceneStep = 0;
        this.setVisible(true);
        this.requestFocus(); // Grab focus so player can't move yet
        loadScene();
    }

    private void nextStep() {
        sceneStep++;
        loadScene();
    }

    private void loadScene() {
        // --- YOUR REQUESTED SEQUENCE ---
        
        if (sceneStep == 0) {
            // 1. FIRST VIDEO
            // Make sure you have 'video1.gif' in your images folder
            setImage("video1.gif"); 
            currentText = "System Booting..."; 
        } 
        else if (sceneStep == 1) {
            // 2. NARRATIVE TEXT
            // You can use a static background image here
            setImage("imageIII.jpeg"); 
            currentText = "Welcome to TRON..."; // The text you requested
        } 
        else if (sceneStep == 2) {
            // 3. SECOND VIDEO
            setImage("video2.gif"); 
            currentText = "Entering the Grid...";
        } 
        else {
            // End of sequence
            endScene();
        }
        repaint();
    }

    private void endScene() {
        this.isActive = false;
        this.setVisible(false);
        mainGame.resumeGame(); // Start the game logic
    }

    private void setImage(String path) {
        // Try loading from src/images or resources
        URL imgUrl = getClass().getClassLoader().getResource(path);
        if (imgUrl != null) {
            this.currentImage = new ImageIcon(imgUrl).getImage();
        } else {
            // Fallback for direct file path
            this.currentImage = new ImageIcon("src/" + path).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isActive) return;

        // 1. Background
        g.setColor(new Color(0, 0, 0, 230));
        g.fillRect(0, 0, getWidth(), getHeight());

        // 2. Draw Image/Video (Centered)
        if (currentImage != null) {
            int imgW = 600; // Adjust size as needed
            int imgH = 400;
            int x = (getWidth() - imgW) / 2;
            int y = (getHeight() - imgH) / 2;
            g.drawImage(currentImage, x, y, imgW, imgH, this);
        }

        // 3. Text Box
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() - 100, getWidth(), 100);
        g.setColor(Color.CYAN);
        g.drawRect(0, getHeight() - 100, getWidth(), 100);

        // 4. Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.drawString(currentText, 50, getHeight() - 50);
        
        g.setFont(new Font("Arial", Font.ITALIC, 12));
        g.drawString("[CLICK TO CONTINUE]", getWidth() - 150, getHeight() - 20);
    }
}