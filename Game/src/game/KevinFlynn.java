package game;

public class KevinFlynn extends Character {

    public KevinFlynn() {
        this.name = "Kevin Flynn";
    }

    @Override
    protected void applySpecificStats() {
        // Requirement: Kevin Flynn gains more handling precision
        this.handling += 1.5;
        this.speed += 0.5;

        // Requirement: Kevin gains disc capacity faster than others
        if (this.level % 5 == 0) {
            this.discsOwned += 1;
            System.out.println("Kevin Flynn unlocked an extra disc slot!");
        }

        System.out.println("Kevin Flynn leveled up! Handling precision increased.");
    }
}