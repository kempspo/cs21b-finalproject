
/**
 * Updates the DrawingComponent
 * 
 * @author Tomy Callanta & Kemp Po
 * @version 5/9/2016
 */
import java.awt.event.*;
import java.awt.*;

public class Animation implements Runnable
{
    // instance variables - replace the example below with your own
    private DrawingComponent dc;

    /**
     * Constructor for objects of class Animation
     */
    public Animation(DrawingComponent dc)
    {
        this.dc = dc;
    }

    /**
     * updates the DrawingComponent after every 25 milliseconds
     */
    public void run()
    {
        try{
            while(true){
                dc.repaint();
                Thread.sleep(25);
            }
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
    }
}
