package cow_trouble;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * @author Balint
 */

public class Midlet extends MIDlet {

    public final String DBNAME = "HiScore";
    private MainMenu mainMenu;
    private OptionsMenu optionsMenu;
    private HighScoresMenu highScoresMenu;
    private Level level;
    private int screen;
    private boolean sound, music, vibration;

    public Midlet() {
        mainMenu = new MainMenu(this);  
        optionsMenu = new OptionsMenu(this);
        highScoresMenu = new HighScoresMenu(this);
        screen = 0;
        sound = music = vibration = false;
    }
    
    public void showMainMenu() {
        Display.getDisplay(this).setCurrent(mainMenu);
        screen = 0;
    }
    
    public void showOptionsMenu() {
        Display.getDisplay(this).setCurrent(optionsMenu);
        screen = 1;
    }
    
    public void showHighScoresMenu() {
        highScoresMenu.readScore();
        Display.getDisplay(this).setCurrent(highScoresMenu);
        screen = 2;
    }
    
    public void showLevel() {
        level = new Level(this);
        level.setBest(highScoresMenu.bestScore()); //legjobb pontszám lekérdezése
        Display.getDisplay(this).setCurrent(level);
        level.startGame();
        screen = 3;
    }
    
    public void setSound(boolean s) {
        sound = s;
    }
    
    public boolean soundOn() {
        return sound;
    }
    
    public void setMusic(boolean m) {
        music = m;
    }
    
    public boolean musicOn() {
        return music;
    }
    
    public void setVibration(boolean v) {
        vibration = v;
    }
    
    public boolean vibrationOn() {
        return vibration;
    }

    public void startApp() {
        switch(screen){     //amelyik képernyőn lett megállítva az app, ott folytatja
            case 0:
                showMainMenu();
                break;
            case 1:
                showOptionsMenu();
                break;
            case 2:
                showHighScoresMenu();
                break;
            case 3:
                Display.getDisplay(this).setCurrent(level);
                level.repaint();
        }    
    }

    public void pauseApp() {
        if(screen == 3){
            level.pauseGame();
            level.stopGame();
        }
        notifyPaused();
    }

    public void destroyApp(boolean unconditional) {
    }
}

