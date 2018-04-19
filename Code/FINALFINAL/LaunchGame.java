
/**
 * Write a description of class LaunchGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
public class LaunchGame
{
    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public static void main(String args[])
    {
        int w = 600;
        int h = 660;
        JFrame f = new JFrame();
        DrawingComponent dc = new DrawingComponent();
        
        f.getContentPane().setPreferredSize(new Dimension(w,h));
        f.add(dc);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
