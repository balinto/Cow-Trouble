package cow_trouble;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Balint
 */
public class OptionsMenu extends Canvas {
    
    private Midlet midlet;
    private int menuMove;
    private Image bg, soundOnImg, soundOnImgS, soundOffImg, soundOffImgS,
            musicOnImg, musicOnImgS, musicOffImg, musicOffImgS, vibrationOnImg,
            vibrationOnImgS, vibrationOffImg, vibrationOffImgS, backImg, backImgS;
    
    public OptionsMenu(Midlet aMidlet) {
        midlet = aMidlet;
        menuMove=1;
        try {
            bg = Image.createImage("/bg2.jpg");
            soundOnImg = Image.createImage("/SoundOn.png");
            soundOnImgS = Image.createImage("/sSoundOn.png");
            soundOffImg = Image.createImage("/SoundOff.png");
            soundOffImgS = Image.createImage("/sSoundOff.png");
            musicOnImg = Image.createImage("/MusicOn.png");
            musicOnImgS = Image.createImage("/sMusicOn.png");
            musicOffImg = Image.createImage("/MusicOff.png");
            musicOffImgS = Image.createImage("/sMusicOff.png");
            vibrationOnImg = Image.createImage("/VibrationOn.png");
            vibrationOnImgS = Image.createImage("/sVibrationOn.png");
            vibrationOffImg = Image.createImage("/VibrationOff.png");
            vibrationOffImgS = Image.createImage("/sVibrationOff.png");
            backImg = Image.createImage("/Back.png");
            backImgS = Image.createImage("/sBack.png");
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
        
        //sound
        if (menuMove == 1){
            if(midlet.soundOn())
                g.drawImage(soundOnImgS, getWidth()/2-60, getHeight()/2-70, Graphics.TOP | Graphics.LEFT);
            else
                g.drawImage(soundOffImgS, getWidth()/2-60, getHeight()/2-70, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            if(midlet.soundOn())
                g.drawImage(soundOnImg, getWidth()/2-60, getHeight()/2-70, Graphics.TOP | Graphics.LEFT);
            else
                g.drawImage(soundOffImg, getWidth()/2-60, getHeight()/2-70, Graphics.TOP | Graphics.LEFT);
        }  
        
        //music
        if (menuMove == 2){
            if(midlet.musicOn())
                g.drawImage(musicOnImgS, getWidth()/2-60, getHeight()/2-30, Graphics.TOP | Graphics.LEFT);
            else
                g.drawImage(musicOffImgS, getWidth()/2-60, getHeight()/2-30, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            if(midlet.musicOn())
                g.drawImage(musicOnImg, getWidth()/2-60, getHeight()/2-30, Graphics.TOP | Graphics.LEFT);
            else
                g.drawImage(musicOffImg, getWidth()/2-60, getHeight()/2-30, Graphics.TOP | Graphics.LEFT);
        }  

        //vibration
        if (menuMove == 3){
            if(midlet.vibrationOn())
                g.drawImage(vibrationOnImgS, getWidth()/2-60, getHeight()/2+10, Graphics.TOP | Graphics.LEFT);
            else
                g.drawImage(vibrationOffImgS, getWidth()/2-60, getHeight()/2+10, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            if(midlet.vibrationOn())
                g.drawImage(vibrationOnImg, getWidth()/2-60, getHeight()/2+10, Graphics.TOP | Graphics.LEFT);
            else
                g.drawImage(vibrationOffImg, getWidth()/2-60, getHeight()/2+10, Graphics.TOP | Graphics.LEFT);
        } 
        
        //back
        if (menuMove == 4){
            g.drawImage(backImgS, getWidth()/2-60, getHeight()/2+70, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(backImg, getWidth()/2-60, getHeight()/2+70, Graphics.TOP | Graphics.LEFT);
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
        if (keyCode == -5 || keyCode == 50) { //OK gomb
                switch (menuMove){
                    case 1: //Sound
                        if(midlet.soundOn())
                            midlet.setSound(false);
                        else
                            midlet.setSound(true);
                        repaint();
                        break;
                    case 2: //Music
                        if(midlet.musicOn())
                            midlet.setMusic(false);
                        else
                            midlet.setMusic(true);
                        repaint();
                        break;//
                    case 3: //Vibration
                        if(midlet.vibrationOn())
                            midlet.setVibration(false);
                        else
                            midlet.setVibration(true);
                        repaint();
                        break;
                    case 4: //Back to MainMenu
                        midlet.showMainMenu();
                        break;                        
                }
        }
    }
    
    protected void pointerReleased(int x, int y) {
        //sound
        if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
           y >= (getHeight()/2-70) && y <= (getHeight()/2-40))
            {
                if(midlet.soundOn())
                    midlet.setSound(false);
                else
                    midlet.setSound(true);
                repaint();
            }
        //music
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2-30) && y <= (getHeight()/2))
                {
                    if(midlet.musicOn())
                        midlet.setMusic(false);
                    else
                        midlet.setMusic(true);
                    repaint();
                }
        //vibration
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+10) && y <= (getHeight()/2+40))
                {
                    if(midlet.vibrationOn())
                        midlet.setVibration(false);
                    else
                        midlet.setVibration(true);
                    repaint();
                }
        //back
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+70) && y <= (getHeight()/2+100))
                midlet.showMainMenu();
    }
    
    protected void pointerPressed(int x, int y) {
        //sound
        if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
           y >= (getHeight()/2-70) && y <= (getHeight()/2-40))
            {
                menuMove = 1;
                repaint();
            }
        //music
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2-30) && y <= (getHeight()/2))
                {
                    menuMove = 2;
                    repaint();
                }
        //vibration
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+10) && y <= (getHeight()/2+40))
                {
                    menuMove = 3;
                    repaint();
                }
        //back
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+70) && y <= (getHeight()/2+100))
                {
                    menuMove = 4;
                    repaint();
                }
    }
}
