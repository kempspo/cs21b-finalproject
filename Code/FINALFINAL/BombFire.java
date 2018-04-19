
/**
 * Write a description of class BombFire here.
 * 
 * @author Tomy Callanta and Kemp Po 
 * @version (a version number or a date)
 */
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.Timer;
public class BombFire implements Tile
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;

    private BufferedImage[] img;
    private int type;
    private String[] fileNames = {"FireCore.png","VerFireBeam.png","HorFireBeam.png"};
    private static int FireCore = 0;
    private static int VerFireBeam = 1;
    private static int HorFireBeam = 2;
    private static boolean maxt;
    private Timer timer;
    private int time;
    private boolean fromEnemy;

    /**
     * Constructor for objects of class BombFire
     */
    public BombFire(int x, int y, int type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        img = new BufferedImage[3];
        addImages();
        maxt = false;
        time = 0;
        startTimerBF();
        fromEnemy = false;
    }
    
    public BombFire(int x, int y, int type, boolean b)
    {
        fromEnemy = b;
        this.x = x;
        this.y = y;
        this.type = type;
        img = new BufferedImage[3];
        addImages();
        maxt = false;
        time = 0;
        startTimerBF();
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img[type], x, y, null);
    }

    public void addImages(){
        for(int i = 0; i < img.length; i++){
            try{
                img[i] = ImageIO.read(new File(fileNames[i]));
            }catch(IOException e){
            }
        }
    }

    public void startTimerBF(){
        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    time++;
                    if(time>=20){
                        maxt = true;
                        timer.stop();
                    }
                }
            };
        timer = new Timer(50,al);
        timer.start();
    }
    
    public boolean ownFlame(){
        return !fromEnemy;
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
