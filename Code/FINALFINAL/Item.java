
/**
 * Write a description of class Bomb here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.Timer;
import java.util.*;

public class Item implements Tile
{
    private int x;
    private int y; 
    private BufferedImage[] img;    
    private int display;
    private int type;
    private static int FireUp = 0;
    private static int BombUp = 1;
    private static int SpeedUp = 2;
    private static int Skull = 3;
    private String[] fileNames = {"RangeUP.jpg","BombUP.jpg","SpeedUp.jpg","Skull.jpg"};
    
    private int time;
    private Timer timer;
    private boolean checker;
    private boolean maxt;
    
    /**
     * Constructor for objects of class Bomb
     */
    public Item(int x, int y)
    {
        this.x = x;
        this.y = y;
        img = new BufferedImage[4];
        addImages();
        startTimer();
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img[display], x, y, null);
    }
    
    public void addImages(){
        for(int i = 0; i < img.length; i++){
            try{
                img[i] = ImageIO.read(new File(fileNames[i]));
            }catch(IOException e){
            }
        }
    }
    
    public void defineType(){
        Random rand = new Random();
        int r = rand.nextInt(6);
        if(r>4){
            type = FireUp;
            display = FireUp;
        }else if(r>2){
            type = BombUp;
            display = BombUp;
        }else if(r>0){
        }else{
            type = Skull;
            display = Skull;
        }
    }
    
    public void setType(int i){
        type = i;
        display = i;
    }
    
    public void startTimer(){
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                time++;
                if(getCurrTime() >= 200) {
                    maxt = true;
                    checker = false;
                    timer.stop();
                }
            }
        };
        if(checker){
            timer = new Timer(50,al);
            timer.start();
        }   
    }
    
    public int getType(){
        return type;
    }
    
    public boolean maxTime(){
        return maxt;
    }
    public int getCurrTime(){
        return time;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getSide(){
        return 40;
    }
}
