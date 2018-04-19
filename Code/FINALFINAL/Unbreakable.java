
/**
 * An instance of unBreakable generates a plain tile that cannot be destroyed with the bomb.
 * 
 * @author Tomy Callanta & Kemp Po
 * @version 5/9/2016
 */
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class Unbreakable implements Tile
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    /**
     * Constructor for objects of class Unbreakable
     */
    public Unbreakable(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * does nothing
     */
    public void draw(Graphics2D g2d)
    {
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
