package cow_trouble;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Balint
 */

public class Car {
    private int x;
    private int y;
    protected int height = 66;
    protected int width = 60;
    private Image image, damImage;
    
    public Car(int aX, int aY) {
        x = aX;
        y = aY;
        try {
            image = Image.createImage("/Car.png");
            damImage = Image.createImage("/CarDamaged.png");
	} catch (IOException e) {
		e.printStackTrace();
	}
    }

    public void drawMe(Graphics g, boolean isDamaged) {
        if(isDamaged)
            g.drawImage(damImage, x, y, Graphics.TOP | Graphics.LEFT);
        else
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
