/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.game.player;

/**
 *
 * @author User
 */
public class Main {
    public static void main(String[] args) {
Character myPlayer = CharacterStats.loadCharacter("Tron");

        // 2. Check if it worked
        if (myPlayer != null) {
            System.out.println("Success! Loaded " + myPlayer.getName());
            System.out.println("Speed is: " + myPlayer.getSpeed());
        } else {
            System.out.println("Failed to load character.");
        }
    }
}