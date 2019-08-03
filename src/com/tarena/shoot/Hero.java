package com.tarena.shoot;
import java.awt.image.BufferedImage;
//英雄机

public class Hero extends FlyingObject{
    private int life;
    private int doubleFire;
    private BufferedImage[] images;//图片数组 两张图片切换
    private int index;

    public Hero() {
        image=ShootGame.hero0;
        width=image.getWidth();
        height=image.getHeight();
        x=150;
        y=400;
        life=3;
        doubleFire=0;//单倍火力
        images=new BufferedImage[2];
        images[0]=ShootGame.hero0;
        images[1]=ShootGame.hero1;
        index=0;
    }

    public void step() {
        image=images[index++/10%images.length];
       /* index++;
        int a=index/10;
        int b=a%2;
        image=images[b];*/
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    public Bullet[] shoot(){
        int xStep=this.width/4;
        int yStep=20;
        if (doubleFire>0){
             Bullet[] bs=new Bullet[2];
             bs[0]=new Bullet(this.x+1*xStep,this.y-yStep);
             bs[1]=new Bullet(this.x+3*xStep,this.y-yStep);
             doubleFire-=2;
            return bs;
        }else {
            Bullet[] bs=new Bullet[1];
            bs[0]=new Bullet(this.x+2*xStep,this.y-yStep);
            return bs;
        }
    }

    public int getLife() {
        return life;
    }

    public void substactLife(){
        life--;
    }

    public void  addLife(){
        life++;
    }
    public void addDoubleFire(){
        doubleFire+=40;
    }

    public void clearDoubleFire(){
        doubleFire=0;
    }
    public boolean hit(FlyingObject obj){
        int x1=obj.x-this.width/2;
        int x2=obj.x+obj.width+this.width/2;
        int y1=obj.y-this.height/2;
        int y2=obj.y+obj.height+this.height/2;
        int x=this.x+this.width/2;
        int y=this.y+this.height/2;
        return x>x1&&x<x2
                &&
               y>y1&&y<y2;
    }

    public void moveTo(int x, int y) {
        this.x=x-this.width/2;
        this.y=y-this.height/2;
    }
}
