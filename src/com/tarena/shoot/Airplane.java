package com.tarena.shoot;

import java.util.Random;

//敌机
public class Airplane extends FlyingObject implements Enemy {

    private int speed=2;
    public Airplane(){
        image=ShootGame.airplane;
        width=image.getWidth();
        height=image.getHeight();
        Random rand=new Random();
        x=rand.nextInt(ShootGame.WIDTH-this.width);
        y=-this.height;
    }
    public int getScore() {
        return 5;
    }//敌机

    @Override
    public void step() {
        y+=speed;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }
}
