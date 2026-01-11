package game;

import java.io.File; 

public abstract class Character {
    protected String name;
    protected int level = 1;
    protected double xp = 0;
    
    protected double lives; 
    protected double speed;
    protected double handling;
    protected int discsOwned;
    
    private String pendingTitle = null;
    private String pendingMessage = null;
    private String pendingImagePath = null;

    public double getXP() { return xp; }
    public int getLevel() { return level; }
    public int getDiscs() { return discsOwned; }
    public void setLives(double l) { this.lives = l; }

    public double getHealth() { return lives; }
    
    public void takeDamage(double amount) {
        this.lives -= amount;
        if (this.lives < 0) this.lives = 0;
    }

    public void gainXP(int amount) {
        if (this.level >= 99) return; 
        this.xp += amount;
        while (this.xp >= getRequiredXP()) {
            this.xp -= getRequiredXP();
            performLevelUp();
        }
    }

    private int getRequiredXP() {
        if (this.level <= 10) return this.level * 100; 
        return this.level * 500; 
    }

    private void performLevelUp() {
        this.level++;
        if (this.level % 10 == 0) this.lives += 1.0; 
        if (this.level % 15 == 0) this.discsOwned += 1;
        checkUnlocks();
        applySpecificStats();
    }

    private void checkUnlocks() {
        switch (this.level) {
            case 2 -> saveCutscene("Chapter 1: The Arrival", 
                             "You have survived the initial processing.\nWelcome to the Grid.", 
                             "images/story_chapter1.jpg");
            
            case 5 -> saveCutscene("Character Unlocked!", 
                             "Your resilience has awakened the Creator.\nKevin Flynn is now playable!", 
                             "images/kevin_flynn_unlock.jpg"); 
                             
            case 10 -> saveCutscene("Chapter 2: The Rise of Clu", 
                             "Perfection is an illusion.\nClu has begun his purge.", 
                             "images/story_chapter2.jpg"); 
        }
    }
    
    private void saveCutscene(String title, String msg, String path) {
        this.pendingTitle = title;
        this.pendingMessage = msg;
        this.pendingImagePath = path;
    }

    public boolean hasPendingCutscene() { return pendingImagePath != null; }
    public String getPendingTitle() { return pendingTitle; }
    public String getPendingMessage() { return pendingMessage; }
    public String getPendingImagePath() { return pendingImagePath; }
    public void clearPendingCutscene() { this.pendingTitle = null; }

    public double getSpeed() { return this.speed; }
    public double getHandling() { return this.handling; }
    public String getName() { return this.name; }

    protected abstract void applySpecificStats();
}