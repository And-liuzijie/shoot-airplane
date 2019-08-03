package com.tarena.shoot;

public class BigYellowBee extends FlyingObject  implements Award {
    @Override
    public int getType() {
        return 0 ;
    }

    @Override
    public void step() {

    }

    @Override
    public boolean outOfBounds() {
        return false;
    }
}
