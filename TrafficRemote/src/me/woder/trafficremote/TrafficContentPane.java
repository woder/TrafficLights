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
	Image image; //the background image
	int imgheight = 694;
	int imgwidth = 411;
	public boolean redon = false;
	public boolean yellowon = false;
	public boolean greenon = false;
	public boolean ped1orange = false;
	public boolean ped1blue = false;
	public boolean red2on = false;
	public boolean yellow2on = false;
	public boolean green2on = false;
	public boolean ped2orange = false;
	public boolean ped2blue = false;

	public TrafficContentPane() {
		try{
			image = ImageIO.read(new File("background.jpg"));
			System.out.println("Yeahhhhh");
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
        //nos variable pour décrire l'étas des lumière graphiquemen
        if(redon){
            //161, 45
            g.setColor(new Color(255,0,0));
            this.drawCenteredCircle(g, 163, 45, 45);
        }
        if(yellowon){
            //98, 45
            g.setColor(new Color(255,205,0));
            this.drawCenteredCircle(g, 98, 45, 45);
        }
        if(greenon){
            //37, 45
            g.setColor(new Color(0,255,0));
            this.drawCenteredCircle(g, 37, 45, 45);
        }
        if(ped1blue){
            
        }
        if(ped1orange){
            
        }
        if(red2on){
            //644, 51
            g.setColor(new Color(255,0,0));
            this.drawCenteredCircle(g, 644, 51, 45);
        }
        if(yellow2on){
            //582, 51
            g.setColor(new Color(255,205,0));
            this.drawCenteredCircle(g, 582, 51, 45);
        }
        if(green2on){
            //523, 51
            g.setColor(new Color(0,255,0));
            this.drawCenteredCircle(g, 523, 51, 45);
        }
        if(ped2orange){
            
        }
        if(ped2blue){
            
        }          
    }
	
	public void drawCenteredCircle(Graphics g, int x, int y, int r) {
	    x = x-(r/2);
	    y = y-(r/2);
	    g.fillOval(x,y,r,r);
	  }

}
