/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_fop.sem1;

/**
 *
 * @author ammar
 */
public class Disc {
    private int x,y; //current position
    private int startX, startY; //calculate range
    private boolean isThrown; //is the disc currently thrown?
    private boolean onGround; //Is it on the ground?
    private String chara; //chech if you can pick it up //e.g. player can pick up the disc own by them and their teammates of the same colour
    private String currentDirection;
    
    //CD
    private long throwTime;
    private static final long COOLDOWN_TIME = 5000; //5s in ms
    
    public Disc(String chara){
    this.chara = chara;
    this.isThrown = false;
    this.onGround = false;
    // Initialize throwTime to -5000 so they can throw immediately at start
    this.throwTime = System.currentTimeMillis() - COOLDOWN_TIME;
}
    //check player CD skills
    public boolean canThrow(){
        long currentTime = System.currentTimeMillis();
        // check if (currentTime - lastThrow) > CD
        boolean cooldownOver = (currentTime - throwTime) >= COOLDOWN_TIME;
        // 2. Check if the player actually has the disc (not in air, not on ground)
        boolean hasDisc = !isThrown && !onGround;
        return cooldownOver && hasDisc;
    }
    public void throwDisc(int startX, int startY, String direction){
        if(canThrow()){
            this.startX = startX;
            this.startY = startY;
            this.x = startX;
            this.y = startY;
            this.currentDirection = direction;
            this.isThrown = true;
            this.throwTime = System.currentTimeMillis();
        }
    }
    public void update(){
        if (isThrown) {
            // Move based on the saved direction
            // (Assuming speed is 1 grid per update)
            switch (currentDirection) {
                case "UP":    y--; break;
                case "DOWN":  y++; break;
                case "LEFT":  x--; break;
                case "RIGHT": x++; break;
            }

            // Check if distance > 3 units
            int distance = Math.abs(x - startX) + Math.abs(y - startY);
            if (distance >= 3) {
                land();
            }
        }
    }
    public void land(){
        isThrown = false;
        onGround = true;
    }
    
    //can the disc pick up by?
    public boolean captureDisc(String characterName){
        if(onGround && characterName.equals(this.chara)){
            return true;
        }return false;
    }
    public void reset(){
        onGround = false;
        isThrown = false;
    }
    // Getters for X and Y so the Main class can draw the disc
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return isThrown; }
    public boolean isOnGround() { return onGround; }
    
}
