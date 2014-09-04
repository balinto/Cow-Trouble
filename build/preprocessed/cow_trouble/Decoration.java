package cow_trouble;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Balint
 */

public class Decoration{
    private int x;
    private int y;
    protected int height = 60;
    protected int width = 60;
    private Image image;
    
    public Decoration(int aX, int aY) { 
        x = aX;
        y = aY;
        try {
            image = Image.createImage("/Tree.png");
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
    
    public void drawMe(Graphics g) {
        
        g.drawImage(image, x, y, Graphics.TOP | Graphics.LEFT);
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
