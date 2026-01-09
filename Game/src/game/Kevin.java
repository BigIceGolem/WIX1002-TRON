/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.game.player;

/**
 *
 * @author User
 */
public class Kevin extends Character{
    public Kevin(double speed,double handling,int lives,int discs,int xp){
        super("Kevin",speed,handling,lives,discs,xp);
    }

    
    public void levelUp() {
        // Your distinct logic for Kevin (e.g. gains more Handling than Speed)
        if (this.level < 99) {
            this.handling += 2.0; // Kevin gets better steering
            this.speed += 0.5;    // Kevin gets a little faster
            this.level++;
        }
    }
}