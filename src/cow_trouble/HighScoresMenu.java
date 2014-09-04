package cow_trouble;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Balint
 */
public class HighScoresMenu extends Canvas {
    
    private Midlet midlet;
    private int menuMove;
    private Image bg, clearImg, clearImgS, backImg, backImgS;
    private int[] scores;
    
    public HighScoresMenu(Midlet aMidlet) {
        midlet = aMidlet;
        menuMove=2;
        
        try {
            bg = Image.createImage("/bg2.jpg");
            clearImg = Image.createImage("/ClearScores.png");
            clearImgS = Image.createImage("/sClearScores.png");
            backImg = Image.createImage("/Back.png");
            backImgS = Image.createImage("/sBack.png");
	} catch (IOException e) {
		e.printStackTrace();
	}
        
        //alaphelyzetben nulla a top10
        scores = new int[10];
        for(int i=0; i<10; i++)
            scores[i] = 0;
    }
    
    //csökkenő sorrendre rekord komparátor
    private class MyCompare implements RecordComparator {
        public int compare(byte[] b1, byte[] b2) {
            String s1 = new String(b1);
            String s2 = new String(b2);
            if (s1.compareTo(s2) > 0) {
                return RecordComparator.PRECEDES;
            } else if (s1.compareTo(s2) == 0) {
                return RecordComparator.EQUIVALENT;
            } else {
                return RecordComparator.FOLLOWS;
            }
        }
    }
    
    public void readScore() {
        int i=0;
        try {
            RecordStore rs = RecordStore.openRecordStore(midlet.DBNAME, false);
            if (rs != null) {
                RecordEnumeration en = rs.enumerateRecords(null, new MyCompare(), false);
                //a tömbbe beolvasás, amíg van, vagy 10-ig
                while (en.hasNextElement() && i < 10) {
                    byte[] tmp = en.nextRecord();
                    scores[i] = toInt(tmp);     
                    i++;
                }
                //a legjobb 10 alattiak törlése
                if(rs.getNumRecords() > 10)
                     while (en.hasNextElement())
                        rs.deleteRecord(en.nextRecordId());
                en.destroy();
                rs.closeRecordStore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clearScore(){
        try {
            RecordStore rs = RecordStore.openRecordStore(midlet.DBNAME, false);
            if(rs != null){
                rs.closeRecordStore();
                RecordStore.deleteRecordStore(midlet.DBNAME);
                for(int i=0; i<10; i++)
                    scores[i] = 0;
            }
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }
    
    public int bestScore() {
        int best = 0;
        try {
            RecordStore rs = RecordStore.openRecordStore(midlet.DBNAME, false);
            if (rs != null) {
                RecordEnumeration en = rs.enumerateRecords(
                        null, new HighScoresMenu.MyCompare(), false);
                if (en.hasNextElement())
                    best = toInt(en.nextRecord());
                en.destroy();
                rs.closeRecordStore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return best;
    }
    
    private int toInt(byte[] b)
    {     
        return (((int)b[0] << 24) | ((int)b[1] << 16) | ((int)b[2] << 8) | (int)b[3] & 0xFF);
    }
    
    public void paint(Graphics g) {
        //alap fekete háttér
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        //háttérkép
        g.drawImage(bg, getWidth()/2, getHeight()/2, Graphics.VCENTER | Graphics.HCENTER);
        
        //pontok
        for(int i= 0; i<10; i++)
            g.drawString(""+scores[i], getWidth()/2+10, getHeight()/2+40-(9-i)*16, Graphics.BOTTOM | Graphics.RIGHT);
        
        //cím
        g.setColor(255, 255, 255);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        g.drawString("Top 10 scores", getWidth()/2, getHeight()/2 - 130, Graphics.BOTTOM | Graphics.HCENTER);
        
        //clear scores
        if (menuMove == 1){
            g.drawImage(clearImgS, getWidth()/2-60, getHeight()/2+50, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(clearImg, getWidth()/2-60, getHeight()/2+50, Graphics.TOP | Graphics.LEFT);
        } 
        
        //back
        if (menuMove == 2){
            g.drawImage(backImgS, getWidth()/2-60, getHeight()/2+90, Graphics.TOP | Graphics.LEFT);
        }    
        else {
            g.drawImage(backImg, getWidth()/2-60, getHeight()/2+90, Graphics.TOP | Graphics.LEFT);
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
                if(menuMove < 2){
                    menuMove++;
                    repaint();
                }
                break;
        }
    }
        
    protected void keyReleased(int keyCode) {
        if (keyCode == -5 || keyCode == 50) { //OK gomb
                switch (menuMove){
                    case 1: //Clear
                        clearScore();
                        repaint();
                        break;
                    case 2: //Back to MainMenu
                        midlet.showMainMenu();
                        break;                        
                }
        }
    }
    
    protected void pointerReleased(int x, int y) {
        //Clear
        if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+50) && y <= (getHeight()/2+80))
                {
                    clearScore();
                    repaint();
                }
        
        //Back to MainMenu
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+90) && y <= (getHeight()/2+120))
                midlet.showMainMenu();
    }
    
    protected void pointerPressed(int x, int y) {
        //Clear
        if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+50) && y <= (getHeight()/2+80))
                {
                    menuMove = 1;
                    repaint();
                }
        //Back to MainMenu
        else if(x >= (getWidth()/2-60) && x <= (getWidth()/2+60) &&
                y >= (getHeight()/2+90) && y <= (getHeight()/2+120))
                {
                    menuMove = 2;
                    repaint();
                }
    }
}
