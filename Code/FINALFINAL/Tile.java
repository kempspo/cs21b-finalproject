
/**
 * Write a description of interface Tile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;
public interface Tile
{
    /**
     * An example of a method header - replace this comment with your own
     * 
     * @param  y    a sample parameter for a method
     * @return        the result produced by sampleMethod 
     */
    public void draw(Graphics2D g2d);
    public int getX();
    public int getY();
    public int getSide();
}
