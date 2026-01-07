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
    private int startX, startY; //calculate range999999999999999999999999999999999999999999
    private boolean isThrown; //is the disc currently thrown?
    private boolean onGround; //Is it on the ground?
    private String chara; //chech if you can pick it up //e.g. player can pick up the disc own by them and their teammates of the same colour
    
    //CD
    private long throwTime;
    private static final long COOLDOWN_TIME = 5000; //5s in ms
    
    public Disc(String chara){
    this.chara = chara;
    this.isThrown = false;
    this.onGround = false;
}
    //check player CD skills
    public boolean canThrow(){
        long currentTime = System.currentTimeMillis();
        // check if (currentTime - lastThrow) > CD
        return false;
    }
    public void throwDisc(int startX, int startY, String direction){
        if(canThrow()){
            this.startX = startX;
            this.startY = startY;
            this.x = startX;
            this.y = startY;
            this.isThrown = true;
            this.throwTime = System.currentTimeMillis();
        }
    }
    public void update(){
        if(isThrown){
            //tinggalkan dulu
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
    
}
