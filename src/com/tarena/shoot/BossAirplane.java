package com.tarena.shoot;

public class BossAirplane extends FlyingObject implements Enemy{

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public void step() {

    }

    @Override
    public boolean outOfBounds() {
        return false;
    }
}
