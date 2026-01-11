package game;

public class KevinFlynn extends BasicMechanic {

    public KevinFlynn() {
        super();
        this.name = "Kevin Flynn";
        // REMOVED HARDCODED STATS.
        // Initial stats are loaded from characters.txt
    }

    @Override
    protected void applySpecificStats() {
        this.handling += 1.5; 
        this.speed += 0.5;    

        if (this.level % 5 == 0) {
            this.discsOwned += 1;
        }
        System.out.println("Kevin leveled up! Handling +1.5");
    }
}