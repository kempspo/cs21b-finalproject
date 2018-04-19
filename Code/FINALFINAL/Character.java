/**
 * Write a description of class Character here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

import java.awt.geom.*;
import javax.swing.Timer;
import java.util.*;

public class Character
{
    private int x;
    private int y;
    private int rate;
    private int maxBomb;
    private int maxRange;
    private int dropped;
    private int totalP;
    private String direction;
    private int display;
    private boolean isMoving;
    private boolean isDead;
    private boolean isExploding;
    private String[] fileNames = {"CharacterDown.png","CharacterUp.png",
            "CharacterLeft.png","CharacterRight.png"};
    private BufferedImage[] img;
    private int side;
    private Timer timer;
    private int time;

    /**
     * Constructor for objects of class Character
     */
    public Character(int x, int y)
    {
        this.x = x;
        this.y = y;
        maxBomb = 1;
        dropped = 0;
        rate = 5;
        display = 0;
        maxRange = 3;
        img = new BufferedImage[4];
        side = 40;
        addImages();
        time = 0;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void draw(Graphics2D g2d)
    {
        g2d.draw(new Rectangle2D.Double(x,y,40,40));
        g2d.drawImage(img[display], x+5, y-20, null);
    }

    public void addImages(){
        for(int i = 0; i < img.length; i++){
            try{
                img[i] = ImageIO.read(new File(fileNames[i]));
            }catch(IOException e){
            }
        }
    }

    public void moveUp(){
        y-=rate;
        display = 1;
    }

    public void moveDown(){
        y+=rate;
        display =0;
    }

    public void moveLeft(){
        x-=rate;
        display=2;
    }

    public void moveRight(){
        x+=rate;
        display=3;
    }

    public void useItem(int type){
        switch(type){
            case 0:
            if(maxRange < 12)
                maxRange++;
            break;

            case 1:
            if(maxBomb < 10)
                maxBomb++;
            break;

            case 3:
            skullEffect();
            break;
        }
    }

    public void skullEffect(){
        Random rand = new Random();
        int r = rand.nextInt(2);

        switch(r){
            case 0:
            if(maxRange>3)
                maxRange--;
            break;

            case 1:
            if(maxBomb > 1)
                maxBomb--;
            break;

            case 2:
                slowDown(rate);
            break;
        }
    }

    public void slowDown(int origRate){
        int r = origRate;
        rate = 1;
        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    time++;
                    if(time >= 100) {
                        rate = origRate;
                        time = 0;
                        timer.stop();
                    }               
                }
            };

        timer = new Timer(50,al);
        timer.start();
    }

    public void addRange(){
        if(maxRange < 15){
            maxRange += 3;
        }
    }

    public void subRange(){
        if(maxRange > 3)
            maxRange -= 3;
    }

    public void subRate(int i){
        rate -= i;
    }

    public void addRate(int i){
        rate += i;
    }

    public void drop(){
        dropped++;
    }

    public void addMaxBomb(){
        maxBomb += 1;
    }

    public void subMaxBomb(){
        maxBomb -= 1;
    }

    public void reload(){ 
        dropped--;
    }

    public boolean canMove(Tile t1, Tile t2, Tile t3, int i){
        if((t1 == null || t1 instanceof BombFire || t1 instanceof Item) && 
           (t2 == null || t2 instanceof BombFire || t2 instanceof Item))
            return true;
        else if((t1 == null || t1 instanceof BombFire || t1 instanceof Item) && t2 != null)
            return !isColliding(t2, i);
        else
            return !isColliding(t1, i);
    }

    public boolean isColliding(Tile t, int i){
        int[] add = new int[4];
        for(int j = 0; j<4; j++){
            if(j == i)
                add[j] = rate;
            else
                add[j] = 0;
        }

        return !(y - add[0]>= t.getY() + t.getSide() ||
            y + side + add[1]<= t.getY() || 
            x - add[2]>= t.getX() + t.getSide() ||
            x + side + add[3]<= t.getX());
    }
    
    public void faceDirection(String dir){
        int temp = Integer.parseInt(dir);
        display = temp;
    }
    
    public void setXY(int newX, int newY){
        x = newX;
        y = newY;
    }

    public int getRange(){
        return maxRange;
    }

    public int getMaxBomb(){
        return maxBomb;
    }

    public int getDropped(){
        return dropped;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSide(){
        return side;
    }

    public int getTotalP(){
        return totalP;
    }
}