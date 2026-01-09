/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.game.player;

/**
 *
 * @author User
 */
public class Tron extends Character {
    
    
public Tron(double speed, double handling, int lives, int discs, int xp) {
        super("Tron", speed, handling, lives, discs, xp);
    }

  

    @Override
    public void levelUp() {
      this.speed += 5;  // Tron specific logic
    }
}
