package cow_trouble;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Balint
 */
public class MainMenu extends Canvas {
    
    private Midlet midlet;
    private int menuMove;
    private Image bg, startImg, startImgS, optionsImg, optionsImgS, quitImg, quitImgS, highScoresImg, highScoresImgS;
    
    public MainMenu(Midlet aMidlet) {
        midlet = aMidlet;
        menuMove=1;
        try {
            bg = Image.createImage("/bg.jpg");
            startImg = Image.createImage("/Start.png");
            startImgS = Image.createImage("/sStart.png");
            optionsImg = Image.createImage("/Options.png");
            optionsImgS = Image.createImage("/sOptions.png");
            highScoresImg = Image.createImage("/HighScores.png");
            highScoresImgS = Image.createImage("/sHighScores.png");
            quitImg = Image.createImage("/Quit.png");
            quitImgS = Image.createImage("/sQuit.png");
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
    
    public void paint(Graphics g) {
        //alap fekete háttér
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        //háttérkép
        g.drawImage(bg, getWidth()/2, getHeight()/2, Graphics.VCENTER | Graphics.HCENTER);
        
        //start
        if (menuMove == 1){
            g.drawImage(startImgS, getWidth()/2-60, getHeight()/2-30, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(startImg, getWidth()/2-60, getHeight()/2-30, Graphics.TOP | Graphics.LEFT);
        }  
        
        //options
        if (menuMove == 2){
            g.drawImage(optionsImgS, getWidth()/2-60, getHeight()/2+10, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(optionsImg, getWidth()/2-60, getHeight()/2+10, Graphics.TOP | Graphics.LEFT);
        }  

        //high scores
        if (menuMove == 3){
            g.drawImage(highScoresImgS, getWidth()/2-60, getHeight()/2+50, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(highScoresImg, getWidth()/2-60, getHeight()/2+50, Graphics.TOP | Graphics.LEFT);
        } 
        
        //quit
        if (menuMove == 4){
            g.drawImage(quitImgS, getWidth()/2-60, getHeight()/2+90, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(quitImg, getWidth()/2-60, getHeight()/2+90, Graphics.TOP | Graphics.LEFT);
        } 
    }
    
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case -1: // Fel
                if(menuMove > 1){
                    menuMove--;
                    repaint();
                }
                break;
            case -2: // Le
                if(menuMove < 4){
                    menuMove++;
                    repaint();
                }
                break;
        }
    }
        
    protected void keyReleased(int keyCode) {
        if (keyCode == -5 || keyCode == 50) { //OK gomb vagy 2-es gomb
                switch (menuMove){
                    case 1: //Start
                        midlet.showLevel();
                        break;
                    case 2: //Options
                        midlet.showOptionsMenu();
                        break;//
                    case 3: //High Scores
                        midlet.showHighScoresMenu();
                        break;
                    case 4: //Quit
                        midlet.destroyApp(true);
                        midlet.notifyDestroyed();
                }
        }
    }
    
    protected void pointerReleased(int x, int y) {
        //start
        if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
           y >= (getHeight()/2-30) && y <= (getHeight()/2))
           midlet.showLevel();
        //options
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+10) && y <= (getHeight()/2+40))
                midlet.showOptionsMenu();
        //high scores
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+50) && y <= (getHeight()/2+80))
                midlet.showHighScoresMenu();
        //quit
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+90) && y <= (getHeight()/2+120))
                {
                    midlet.destroyApp(true);
                    midlet.notifyDestroyed();
                }
    }
    
    protected void pointerPressed(int x, int y) {
        //start
        if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
           y >= (getHeight()/2-30) && y <= (getHeight()/2))
           {
               menuMove = 1;
               repaint();
           }
        //options
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+10) && y <= (getHeight()/2+40))
                {
                    menuMove = 2;
                    repaint();
                 }
        //high scores
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+50) && y <= (getHeight()/2+80))
                {
                    menuMove = 3;
                    repaint();
                }
        //quit
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+90) && y <= (getHeight()/2+120))
                {
                    menuMove = 4;
                    repaint();
                }
    }
}
