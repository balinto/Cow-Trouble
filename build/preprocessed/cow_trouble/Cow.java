package cow_trouble;

import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Balint
 */

public class Cow {
    private int x;
    private int y;
    protected int height = 60;
    protected int width = 60;
    private Image image, image2;
    private int r, l;
    
    public Cow(int aX, int aY, int aR) {
        x = aX;
        y = aY;
        r = aR;
        l = 0;
        
        try {
            image = Image.createImage("/Cow.png");
            image2 = Image.createImage("/Cow2.png");
	} catch (IOException e) {
		e.printStackTrace();
	}
    }

    public void drawMe(Graphics g) {
        
        if(r % 2 == 0)
            g.drawImage(image, x, y, Graphics.TOP | Graphics.LEFT);
        else
            g.drawImage(image2, x, y, Graphics.TOP | Graphics.LEFT);
    }
    
    public int getLane() {
        return l;
    }
    
    public void setLane(int l) {
        this.l = l;
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
