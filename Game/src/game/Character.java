package game;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public abstract class Character {
    protected String name;
    protected int level = 1;
    protected double xp = 0;
    protected int lives;
    protected double speed;
    protected double handling;
    protected int discsOwned;

    // Requirement: XP from enemies based on difficulty
    public void gainXP(int amount) {
        if (this.level >= 99) return; // Requirement: Max level 99
        
        this.xp += amount;
        
        // Requirement: Custom leveling-up algorithm
        while (this.xp >= getRequiredXP()) {
            this.xp -= getRequiredXP();
            performLevelUp();
        }
    }

    // Requirement: Rapid early advancement for first 10 levels
    private int getRequiredXP() {
        if (this.level <= 10) {
            return this.level * 100; // Lower threshold
        }
        return this.level * 500; // Normal threshold
    }

    private void performLevelUp() {
        this.level++;
        
        // Requirement: +1 life every 10 levels
        if (this.level % 10 == 0) {
            this.lives += 1;
        }

        // Requirement: Additional disc slot every 15 levels
        if (this.level % 15 == 0) {
            this.discsOwned += 1;
        }

        // Check for Story and Character unlocks
        checkUnlocks();

        // Apply unique character growth
        applySpecificStats();
    }

    private void checkUnlocks() {
        switch (this.level) {
            case 5:
                // Milestone: Unlocking the Story
                showCutscene("Chapter 1: The Arrival", 
                             "You have survived the initial processing.\nWelcome to the Grid.", 
                             "images/story_chapter1.jpg");
                break;
                
            case 10:
                // Milestone: Unlocking Kevin Flynn 
                showCutscene("Character Unlocked!", 
                             "Your resilience has awakened the Creator.\nKevin Flynn is now playable!", 
                             "images/kevin_flynn_unlock.jpg");
                break;
                
            case 20:
                showCutscene("Chapter 2: The Rise of Clu", 
                             "Perfection is an illusion.\nClu has begun his purge.", 
                             "images/story_chapter2.jpg");
                break;
        }
    }


public double getSpeed() {
    return this.speed;
}

public String getName() {
    return this.name;
}
    
    // Helper method to display the visual cutscene
    private void showCutscene(String title, String message, String imagePath) {
        // Load the image (Make sure you have an 'images' folder in your project!)
        ImageIcon icon = new ImageIcon(imagePath);
        
        // Show a popup window with the image and text
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }

    // Abstract method to force unique growth for subclasses
    protected abstract void applySpecificStats();
}
