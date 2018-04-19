
/**
 * Write a description of class Bomb here.
 * 
 * @author Tomy Callanta and Kemp Po 
 * @version (a version number or a date)
 */
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import javax.imageio.*;

public class Bomb implements Tile
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private ArrayList<Integer> i;
    private BufferedImage img;

    private int time;
    private Timer timer;
    private boolean checker;
    private boolean maxt;
    private Tile t1;
    private ArrayList<Tile> tiles;
    private Character c;
    /**
     * Constructor for objects of class Bomb
     */
    public Bomb(int x, int y)
    {
        this.x = x;
        this.y = y;
        checker = false;
        maxt = false;
        time = 0;
        i=new ArrayList<Integer>();
        tiles = new ArrayList<Tile>();
        try{
            img = ImageIO.read(new File("Bomb.png"));
        }catch(IOException e){}
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, x, y, null);
    }

    public void fill(Tile t1,int kind) {
        tiles.add(t1);
        i.add(kind);
        checker = true;
        //startTimer();
    }

    public void startTimer(){
        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    time++;
                    if(getCurrTime() >= 60) {
                        maxt = true;
                        timer.stop();
                    }               
                }
            };

        timer = new Timer(50,al);
        timer.start();
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
