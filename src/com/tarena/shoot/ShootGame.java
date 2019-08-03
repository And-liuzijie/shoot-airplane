package com.tarena.shoot;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.*;





/*主程序类*/
public class ShootGame extends JPanel {

    public static final int WIDTH=400;
    public static final int HEIGHT=654;
    public static final int START=0;
    public static final int RUNNING=1;
    public static final int PAUSE=2;
    public static final int GAMEOVER=3;
    private int state=START;
    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    public static BufferedImage airplane;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage hero0;
    public static BufferedImage hero1;

    private Hero hero=new Hero();
    private FlyingObject[] flyings={};
    private Bullet[] bullets={};
    ShootGame(){

    }

    //静态块  初始化静态资源  在类被加载期间执行
    static {
        try {
               background=ImageIO.read(ShootGame.class.getResource("background.png"));//类和图片必须在同一个包
               start=ImageIO.read(ShootGame.class.getResource("start.png"));//类和图片必须在同一个包
               pause=ImageIO.read(ShootGame.class.getResource("pause.png"));//类和图片必须在同一个包
               gameover=ImageIO.read(ShootGame.class.getResource("gameover.png"));//类和图片必须在同一个包
               airplane=ImageIO.read(ShootGame.class.getResource("airplane.png"));//类和图片必须在同一个包
               bee=ImageIO.read(ShootGame.class.getResource("bee.png"));//类和图片必须在同一个包
               bullet=ImageIO.read(ShootGame.class.getResource("bullet.png"));//类和图片必须在同一个包
               hero0=ImageIO.read(ShootGame.class.getResource("hero0.png"));//类和图片必须在同一个包
               hero1=ImageIO.read(ShootGame.class.getResource("hero1.png"));//类和图片必须在同一个包
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //生成敌人对象
    public FlyingObject nextone(){
        Random rand=new Random();
        int type=rand.nextInt(20);
        if(type<4)
        {
            return new Bee();
        }else {
            return new Airplane();
        }
    }

    public void action(){
        MouseAdapter l=new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(state==RUNNING){
                   int x=e.getX();
                   int y=e.getY();
                   hero.moveTo(x,y);
                }
            }
            public void mouseClicked(MouseEvent e){
                switch (state){
                    case START:
                        state=RUNNING;
                        break;
                    case GAMEOVER:
                        score=0;
                        hero=new Hero();
                        flyings = new FlyingObject[0];
                        bullets=new Bullet[0];
                        state=START;
                        break;
                }
            }
            public void mouseExited(MouseEvent e){
                if(state==RUNNING)
                    state=PAUSE;
            }
            public void mouseEntered(MouseEvent e){
                if (state==PAUSE)
                    state=RUNNING;

            }


        };
        this.addMouseListener(l);//处理鼠标操作事件
        this.addMouseMotionListener(l);//处理鼠标滑动事件
        Timer timer=new Timer();
        int intervel=10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//定时要干的事
                if (state==RUNNING) {
                    enterAction();
                    stepAction();
                    shootAction();
                    bangAction();
                    checkGameOverAction();
                }
                    repaint();
            }
        }, intervel, intervel);

    }

    private void checkGameOverAction() {
        if (isGameOver()){
                state=GAMEOVER;

        }

    }
    public boolean isGameOver(){
        for (int i = 0; i <flyings.length ; i++) {
            FlyingObject f=flyings[i];
            if(hero.hit(f)){//撞上了
                hero.substactLife();
                hero.clearDoubleFire();
                //交换
                FlyingObject t=flyings[i];
                flyings[i]=flyings[flyings.length-1];
                flyings[flyings.length-1]=t;
                flyings=Arrays.copyOf(flyings,flyings.length-1);
            }
        }
        return hero.getLife()<=0;
    }
    private void bangAction() {
        for (int i = 0; i <bullets.length ; i++) {
            Bullet b=bullets[i];
            bang(b);

        }
    }
    int score=0;
    private void bang(Bullet b){
        int index=-1;
        for (int j = 0; j <flyings.length ; j++) {
            FlyingObject f=flyings[j];
            if (f.shootBy(b)){
                 index=j;
            }
            if (index!=-1){
                FlyingObject flying=flyings[index];
                if(flying instanceof Enemy){
                    Enemy e=(Enemy) flying;
                    score+=e.getScore();}
                if (flying instanceof Award) {
                    Award a = (Award) flying;
                    int type = a.getType();
                    switch (type) {
                        case Award.DOUBLE_FIRE:
                            hero.addDoubleFire();
                            break;
                        case Award.LIFE:
                            hero.addLife();
                            break;
                    }
            }
                FlyingObject t = flyings[index];
                flyings[index] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = t;
                //缩容
                flyings = Arrays.copyOf(flyings, flyings.length - 1);

            }

        }
    }

    int shootIndex=0;
    private void shootAction() {
        shootIndex++;//每十毫秒+1
        if (shootIndex%30==0){
           Bullet[] bs=hero.shoot();
           bullets=Arrays.copyOf(bullets,bullets.length+bs.length);//扩容
           System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);
        }
    }

    private void stepAction() {
        hero.step();
        for (int i = 0; i <flyings.length ; i++) {
            flyings[i].step();
        }
        for (int i = 0; i <bullets.length ; i++) {
            bullets[i].step();
        }
    }

    int flyEnterIndex=0;
    private void enterAction() {
        flyEnterIndex++;
        if(flyEnterIndex%40==0){
            FlyingObject one=nextone();
            flyings= Arrays.copyOf(flyings,flyings.length+1);
            flyings[flyings.length-1]=one;
        }
    }

    /*g画笔  系统自动调用*/
    public void paint(Graphics g){
        g.drawImage(background,0,0,null);
        paintHero(g);
        paintFlyingObjiects(g);
        paintBullet(g);
        paintScoreAndLife(g);
        paintState(g);

    }

    public void paintHero(Graphics g){
        g.drawImage(hero.image,hero.x,hero.y,null);
    }
    //敌人  小蜜蜂
    public void paintFlyingObjiects(Graphics g){
        for (int i = 0; i <flyings.length ; i++) {
            FlyingObject f=flyings[i];
            g.drawImage(f.image,f.x,f.y,null);
        }
    }
    public void paintBullet(Graphics g){
        for (int i = 0; i <bullets.length ; i++) {
            Bullet b=bullets[i];
            g.drawImage(b.image,b.x,b.y,null);
        }
    }
    public void paintScoreAndLife(Graphics g){
         g.setColor(Color.red);
         g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
         g.drawString("SCORE："+score,15,20);
         g.drawString("LIFE："+hero.getLife(),15,45);
    }
    public void paintState(Graphics g){
        switch (state){
            case START:
                g.drawImage(start,0,0,null);
                break;
            case PAUSE:
                g.drawImage(pause,0,0,null);
                break;
            case GAMEOVER:
                g.drawImage(gameover,0,0,null);
                break;
        }
    }

        public static void main(String[] args) {
        JFrame frame=new JFrame("airshoot");
        ShootGame game=new ShootGame();//面板
        frame.add(game);
        frame.setSize(WIDTH,HEIGHT);
        frame.setAlwaysOnTop(true);//一直在最上面
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);//  尽快调用paint

        game.action();


    }
}
