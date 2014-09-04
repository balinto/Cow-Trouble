package cow_trouble;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordStore;

/**
 * @author Balint
 */
public class Level extends Canvas {

    private Midlet midlet;
    private Car car;
    private Vector cows, dec;
    private int lane, t, speed, sleepTime, tiles, score, best;
    private boolean gameLooping, paused, damage, gameOver;
    private InputStream is1, is2;
    private Player music, sound;
    private Image road, sides;
    
    public Level(Midlet aMidlet) {
        midlet = aMidlet;
        
        lane = 1;
        t = score = best = 0;
        speed = 2;
        sleepTime = 10;
        damage = gameOver = false;
        tiles = getHeight()/100 + 1;
        
        //player incializálása
        car = new Car(0, 0);
        car.setX(getWidth() / 2 - car.getWidth() / 2);
        car.setY(getHeight() - car.getHeight() - 30);

        //tehenek inicializálása
        cows = new Vector();
        for(int i=0; i<4; i++) {
            cows.addElement(new Cow(0, getHeight()+100, i));
        } 
        
        //fák inicializálása
        dec = new Vector();
        for(int i=0; i<4; i++) {
            dec.addElement(new Decoration(0, getHeight()+100));
        } 

        //képek betöltése
        try {
         road = Image.createImage("/Road.jpg");
         sides = Image.createImage("/Sides.jpg");
         } catch (IOException e) {
         e.printStackTrace();
         }

    }
    
    public void startGame() {
        gameLooping = true;
        paused = false;
        new gameThread().start();
        if(midlet.musicOn() || midlet.soundOn())
            new musicPlay().start();
    }
    
    public void pauseGame() {
        gameLooping = false;
        paused = true;
        if(midlet.musicOn()){
            try {
                music.stop();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void stopGame() {
        gameLooping = false;
        if(midlet.musicOn() || midlet.soundOn())
            stopPlayer();
    }
    
    private class musicPlay extends Thread { 
        public void run()
        {
            //zene
            if(midlet.musicOn()){
                try {
                    if(is1 == null && music == null){
                        is1 = getClass().getResourceAsStream("/music.wav");     
                        music = Manager.createPlayer(is1, "audio/X-wav");
                        music.setLoopCount(-1);
                    }
                    music.start();

                    } catch(Exception ex) {
                        ex.printStackTrace();
                }
            }
            
            //ütközéshang
            if(midlet.soundOn()){
                try {
                    if(is2 == null && sound == null){
                        is2 = getClass().getResourceAsStream("/crash.wav");     
                        sound = Manager.createPlayer(is2, "audio/X-wav");
                    }
                    sound.realize();
                    sound.prefetch();

                    } catch(Exception ex) {
                        ex.printStackTrace();
                }
            }
        }
    }
    
    public void stopPlayer() {
            try {
                if (music!=null)
                    music.close();
                if (is1!=null)
                    is1.close();
                if (sound!=null)
                    sound.close();
                if (is2!=null)
                    is2.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
            }
        }
    
    private void saveScore(int s) {
        try {
            RecordStore rs = RecordStore.openRecordStore(midlet.DBNAME, true);
            byte[] tmpData = toBytes(s);
            rs.addRecord(tmpData, 0, tmpData.length);
            rs.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private byte[] toBytes(int i)
    {                
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i);

        return result;
    }
    
    public void setBest(int b){
        best = b;
    }
    
    private class gameThread extends Thread {
        public void run() {
            while(gameLooping){  
                //sebesség növelése 50 pontonként
                if(sleepTime > 3)
                    if(score % 50 == 0 && t % 100 == 0)
                        sleepTime--;
                
                //az út 100 pixelenként ismétlődik
                if(t >= 100){
                    t = 0;
                }
                //pontszám gyűjtése eltelt idő után, sebességtől függetlenül
                for(int i=1; i<=8; i++)
                    if(sleepTime == 11-i)
                        if(t % (i*10) == 0)
                            score++;
                
                //tehenek tömbjén végigmenetel
                Random r = new Random(System.currentTimeMillis());
                for (int j=0; j<cows.size(); j++) {
                    Cow temp = (Cow) cows.elementAt(j);
                        
                    //ütközés
                    if((temp.getY()+temp.getHeight()-15) >= car.getY() &&
                            (temp.getY()+25) <= (car.getY()+car.getHeight())) {
                        if(temp.getLane() == lane)
                        {
                            if(midlet.soundOn()){
                                try {
                                    sound.start();
                                } catch (MediaException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            if(midlet.vibrationOn())
                                Display.getDisplay(midlet).vibrate(500);
                            damage = true;
                            gameOver = true;
                            repaint();
                            if(midlet.soundOn()) {
                                try {
                                Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            stopGame();
                            saveScore(score);
                        }
                    }
                        
                    // ha leért egy tehén, akkor fel lesz pakolva
                    if (temp.getY() >= getHeight()){
                        temp.setLane(r.nextInt(3));
                        temp.setX(getWidth()/2 - 90 + temp.getLane()*60); 
                        temp.setY(-60-(120*j));
                    }

                    //tehenek "mennek az úttal együtt"
                    temp.setY(temp.getY()+speed);
                }
                
                // hogy ne lehessen 3 tehén egy sorban
                for (int j=0; j<cows.size(); j++) {
                        Cow temp = (Cow) cows.elementAt(j);
                        boolean a, b;
                        a = b = false;
                    for (int k=0; k<cows.size(); k++) {
                            Cow temp2 = (Cow) cows.elementAt(k);
                            if(temp2.getY() + temp2.getHeight() >= (temp.getY()-20) && temp2.getY() <= (temp.getY() + temp.getHeight()+20)) {
                                if(temp2.getX() < temp.getX())
                                    a=true; 
                                if(temp2.getX() > temp.getX())
                                    b=true;
                            }
                            if(a && b)
                                temp.setY(-120);
                        }  
                }
                    
                //dekoráció: fák
                Random d = new Random(System.currentTimeMillis());
                for (int j=0; j<dec.size(); j++) {
                    Decoration temp = (Decoration) dec.elementAt(j);
                    if (temp.getY() >= getHeight()){      
                        temp.setX(getWidth()/2 - 160 + d.nextInt(2)*260 - d.nextInt(2)*20);
                        temp.setY(-200+(-100*d.nextInt(3)));
                    }
            
                    temp.setY(temp.getY()+speed);    
                }
                
                //kirajzolás
                if(gameLooping)
                    repaint();
                  
                //út "haladása"
                t+=speed; 
                
                //várakozás sleepTime ideig, haladási sebesség meghatározása
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
                
        }   
    }

    public void paint(Graphics g) {
        // fekete alapháttér
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());

        // út
        for(int i=-1; i<tiles; i++){
            g.drawImage(road, getWidth() / 2 -100, t+i*100, Graphics.TOP | Graphics.LEFT);
        }
        
        //szélek
        for(int i=-1; i<tiles; i++){
            g.drawImage(sides, getWidth() / 2 - 85, t+i*100, Graphics.TOP | Graphics.RIGHT);
            g.drawImage(sides, getWidth() / 2 + 85, t+i*100, Graphics.TOP | Graphics.LEFT);
        }
        
        //tehenek kirajzolása
        for (int i=0; i<cows.size(); i++) {
            Cow temp = (Cow) cows.elementAt(i);
            temp.drawMe(g);
        };
        
        // player kirajzolása
        car.drawMe(g, damage);
        
        // dekoráció
        for (int i=0; i<dec.size(); i++) {
            Decoration temp = (Decoration) dec.elementAt(i);
            temp.drawMe(g);
        };

        //menu, pause
        g.setColor(255, 255, 255);
        g.drawString("Menu", 8, getHeight()-5, Graphics.BOTTOM | Graphics.LEFT);
        g.drawString("Pause", getWidth()-8, getHeight()-5, Graphics.BOTTOM | Graphics.RIGHT);
        
        //pontszám
        g.drawString("Score", 8, 6, Graphics.TOP | Graphics.LEFT);
        g.drawString("" +score, 8, 25, Graphics.TOP | Graphics.LEFT);
        
        //legjobb pontszám
        g.drawString("Best", getWidth()-8, 6, Graphics.TOP | Graphics.RIGHT);
        g.drawString("" +best, getWidth()-8, 25, Graphics.TOP | Graphics.RIGHT);
        
        //pause
        if(paused){
            g.setColor(0, 0, 0);
            g.fillRoundRect(getWidth()/2 - 35, getHeight()/2-2, 70, 23, 7, 5);
            g.setColor(21, 79, 166);
            g.drawRoundRect(getWidth()/2 - 35, getHeight()/2-2, 70, 23, 7, 5);
            
            g.setColor(250, 250, 250);
            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            g.drawString("Paused", getWidth()/2, getHeight()/2, Graphics.TOP | Graphics.HCENTER);
        }  
        
        //game over
        if(gameOver){
            g.setColor(0, 0, 0);
            g.fillRoundRect(getWidth()/2 - 70, getHeight()/2 - 30, 140, 50, 12, 5);
            g.setColor(21, 79, 166);
            g.drawRoundRect(getWidth()/2 - 70, getHeight()/2 - 30, 140, 50, 12, 5);

            g.setColor(240, 50, 50);
            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.drawString("GAME OVER", getWidth()/2, getHeight()/2-5, Graphics.BOTTOM | Graphics.HCENTER);
            
            g.setColor(250, 250, 250);
            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            g.drawString("Score: "+score, getWidth()/2, getHeight()/2, Graphics.TOP | Graphics.HCENTER);
            
            if(score > best){
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("New high score!", getWidth()/2, getHeight()/2-40, Graphics.BOTTOM | Graphics.HCENTER);
            }
        } 
        
    }
  
    protected void keyPressed(int keyCode) {
        switch (keyCode) {
            case -3: // balra
                if (lane > 0 && gameLooping) {
                    car.setX(car.getX() - 60);
                    repaint();
                    lane--;
                }
                break;
            case -4: // jobbra
                if (lane < 2 && gameLooping) {
                    car.setX(car.getX() + 60);
                    repaint();
                    lane++;
                }
                break;
            case -6: // bal menu - > menu
                stopGame();
                midlet.showMainMenu();
                break;
            case 49: // 1-es gomb - > menu
                stopGame();
                midlet.showMainMenu();
                break;
            case -7: // jobb menu -> pause
                if(!paused && gameLooping){
                    pauseGame();
                    repaint();
                }
                else if(paused){
                    startGame();
                }
                break;
            case 51: // 3-as gomb -> pause
                if(!paused && gameLooping){
                    pauseGame();
                    repaint();
                }
                else if(paused){
                    startGame();
                }
                break;
        }
    }
    
    protected void pointerPressed(int x, int y) {
        //balra
        if(x < getWidth()/2 && y < getHeight()-30 && lane > 0 && gameLooping) {
            car.setX(car.getX() - 60);
            repaint();
            lane--;
        }
        
        // jobbra
        if(x > getWidth()/2 && y < getHeight()-30 && lane < 2 && gameLooping) {
            car.setX(car.getX() + 60);
            repaint();
            lane++;
        }
        
        //menu
        if(x < getWidth()/2-60 && y > getHeight()-30) {
            stopGame();
            midlet.showMainMenu();
        }
        
        //pause
        if(x > getWidth()/2+60 && y > getHeight()-30) {
            if(!paused && gameLooping){
                    pauseGame();
                    repaint();
                }
                else if(paused){
                    startGame();
                }
        }           
    } 
    
}