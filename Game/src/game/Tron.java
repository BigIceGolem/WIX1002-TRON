package game;

public class Tron extends Character {

    public Tron() {
        this.name = "Tron";
        // NO HARDCODED STATS HERE.
        // The GameLoader/CharacterStats class will read characters.txt 
        // and set this.speed, this.handling, etc.
    }

    @Override
    protected void applySpecificStats() {
        // Requirement: Tron gains more speed and stability per level
        this.speed += 1.5; 
        this.handling += 0.5; 
        
        System.out.println("Tron leveled up! Speed increased significantly.");
    }
}