package game; // FIXED: Changed from 'com.game.player'

public class TestCharacterLoad {
    public static void main(String[] args) {
        // This assumes Aasim created a 'CharacterStats' class in the same package
        Character myPlayer = CharacterStats.loadCharacter("Tron");

        // 2. Check if it worked
        if (myPlayer != null) {
            // Note: Make sure your Character.java has a 'getName()' and 'getSpeed()' method!
            System.out.println("Success! Loaded " + myPlayer.getName());
            System.out.println("Speed is: " + myPlayer.getSpeed());
        } else {
            System.out.println("Failed to load character.");
        }
    }
}