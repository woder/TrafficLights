package me.woder.trafficremote;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TrafficContentPane extends JPanel{
	private static final long serialVersionUID = 1L;
	Image image; //la photo d'arriere plan
	Image foreground; //la photo de premier plan
	long lastTime = 0; //le dernier temps enregister pour les clingnotant (lumiere 1)
	long lastTime2 = 0; //le dernier temps enregister pour les clingnotant (lumiere 2)
	int imgheight = 694;
	int imgwidth = 411;
	public boolean redon = false;
	public boolean yellowon = false;
	public boolean greenon = false;
	public int ped1orange = 0;
	public boolean ped1blue = false;
	public boolean red2on = false;
	public boolean yellow2on = false;
	public boolean green2on = false;
	public int ped2orange = 0;
	public boolean ped2blue = false;
	boolean ped1 = false; //variable pour clingnoter les lumiere
	boolean ped2 = false; //variable pour clingnoter les lumiere

	public TrafficContentPane() {
		try{
			image = ImageIO.read(new File("background.jpg"));
			foreground = ImageIO.read(new File("foreground.png"));
			this.addMouseListener(new MouseAdapter() { 
		          public void mousePressed(MouseEvent me) { 
		              int screenX = me.getX();
		              int screenY = me.getY();
		              System.out.println("screen(X,Y) = " + screenX + "," + screenY);
		            } 
		          }); 
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
	      return new Dimension(imgheight, imgwidth);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters  
        //nos variable pour d�crire l'�tas des lumi�re graphiquemen
        long currentTime = System.currentTimeMillis();       
        if(redon){
            //161, 45
            g.setColor(new Color(255,0,0));
            this.drawCenteredCircle(g, 168, 47, 45);
        }
        if(yellowon){
            //107, 45
            g.setColor(new Color(255,205,0));
            this.drawCenteredCircle(g, 107, 47, 45);
        }
        if(greenon){
            //42, 45
            g.setColor(new Color(0,255,0));
            this.drawCenteredCircle(g, 42, 47, 45);
        }
        if(ped1blue){
            //186, 116
            g.setColor(new Color(0,255,0));
            this.drawCenteredCircle(g, 186, 116, 25);
        }
        if(ped1orange == 1){
            //149, 116
            g.setColor(new Color(255,0,0));
            this.drawCenteredCircle(g, 149, 116, 25);
        }else if(ped1orange == 2){ //alors nous devon clingnoter
            if(currentTime - lastTime > 1000) { //si il y a plus d'une seconde qu'on a changer t'etas, change le encore
                lastTime = currentTime;
                if(ped1){
                    ped1 = false;
                }else{
                    ped1 = true;
                    g.setColor(new Color(255,0,0));
                    this.drawCenteredCircle(g, 149, 116, 25);
                }
            }
            if(!ped1){
                g.setColor(new Color(255,0,0));
                this.drawCenteredCircle(g, 149, 116, 25);
            }
        }
        if(red2on){
            //653, 51
            g.setColor(new Color(255,0,0));
            this.drawCenteredCircle(g, 653, 51, 45);
        }
        if(yellow2on){
            //592, 51
            g.setColor(new Color(255,205,0));
            this.drawCenteredCircle(g, 592, 51, 45);
        }
        if(green2on){
            //527, 51
            g.setColor(new Color(0,255,0));
            this.drawCenteredCircle(g, 527, 51, 45);
        }
        if(ped2orange == 1){
            //505, 116
            g.setColor(new Color(255,0,0));
            this.drawCenteredCircle(g, 505, 116, 25);
        }else if(ped2orange == 2){ //alors nous devon clingnoter
            if(currentTime - lastTime2 > 1000) { //si il y a plus d'une seconde qu'on a changer t'etas, change le encore
                lastTime2 = currentTime;
                if(ped2){
                    ped2 = false;
                }else{
                    ped2 = true;
                    g.setColor(new Color(255,0,0));
                    this.drawCenteredCircle(g, 505, 116, 25);
                }
            }
            if(!ped2){
                g.setColor(new Color(255,0,0));
                this.drawCenteredCircle(g, 505, 116, 25);
            }
        }
        if(ped2blue){
            //542, 116
            g.setColor(new Color(0,255,0));
            this.drawCenteredCircle(g, 542, 116, 25);
        }
        
        g.drawImage(foreground, 0, 0, null); //ajoute notre image transparente pour faire une belle effet
    }
	
	public void drawCenteredCircle(Graphics g, int x, int y, int r) {
	    x = x-(r/2);
	    y = y-(r/2);
	    g.fillOval(x,y,r,r);
	  }

}
