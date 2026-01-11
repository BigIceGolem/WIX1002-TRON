package game;

public class Tron extends BasicMechanic {

    public Tron() {
        super();
        this.name = "Tron";
        // REMOVED HARDCODED STATS. 
        // Initial stats are loaded from characters.txt via CharacterStats class.
    }

    @Override
    protected void applySpecificStats() {
        // Buffs applied EVERY level up
        this.speed += 1.5;    
        this.handling += 0.5; 
        System.out.println("Tron leveled up! Speed +1.5");
    }
}