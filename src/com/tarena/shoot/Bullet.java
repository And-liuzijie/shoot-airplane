package com.tarena.shoot;


//子弹

import java.util.Random;

public class Bullet extends FlyingObject {
    private int speed=3;

    public Bullet(int x,int y) {
        image=ShootGame.bullet;
        width=image.getWidth();
        height=image.getHeight();
        Random rand=new Random();
        this.x=x;
        this.y=y;
    }

    public void step() {
        y-=speed;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }


}