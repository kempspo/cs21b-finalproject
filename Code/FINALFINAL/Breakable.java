
/**
 * An instance of Breakable generates a brick tile that can be destroyed by the bomb and randomly spawn a power-up once destroyed.
 * 
 * @author Tomy Callanta & Kemp Po
 * @version 5/9/2016
 */
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class Breakable implements Tile
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private BufferedImage img;

    /**
     * Constructor for objects of class Breakable
     */
    public Breakable(int x, int y)
    {
        this.x = x;
        this.y = y;
        
        try{
            img = ImageIO.read(new File("Bricks.png"));
        }catch(IOException e){}
    }

    /**
     * Draws the brick tile image
     * 
     * @param  g2d where the components are drawn on
     */
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(img, x, y, null);
    }
    
    /**
     * returns the current x coordinate of the top-left corner of the tile
     * 
     * @return the current x coordinate of the top-left corner of the tile
     */
    public int getX(){
        return x;
    }
    
    /**
     * returns the current y coordinate of the top-left corner of the tile
     * 
     * @return the current y coordinate of the top-left corner of the tile
     */
    public int getY(){
        return y;
    }
    
     /**
     * returns the length of a side of the tile
     * 
     * @return the length of a side of the tile
     */
    public int getSide(){
        return 40;
    }
}
