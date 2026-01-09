/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.game.player;

/**
 *
 * @author User
 */
public abstract class Character {
    protected String name;
    protected double speed;
    protected int lives;
    protected double handling;
    protected int discs;
protected int xp;
protected int level;

public Character(String name,double speed,double handling,int lives,int discs,int xp){
this.name=name;
this.speed=speed;
this.handling=handling;
this.lives=lives;
this.xp=xp;
this.discs=discs;
this.level=1;


}
public void levelUp(){
    
}

public String getName(){
    return this.name;
}
public double getSpeed(){
   
    return this.speed;
}
public double getHandling(){
    return this.handling;
}
public void setLives(int lives){
    this.lives=lives;
    if (this.lives < 0) {
            this.lives = 0;
        }
}
public int getLives(){
    return this.lives;
}
public int getXP(){
    return this.xp;
}
public int getDiscs(){
    return this.discs;
}

}




