package me.woder.trafficremote;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TrafficContentPane extends JPanel{
	private static final long serialVersionUID = 1L;
	Image image; //the background image
	int imgheight = 694;
	int imgwidth = 411;

	public TrafficContentPane() {
		try{
			image = ImageIO.read(new File("background.jpg"));
			System.out.println("Yeahhhhh");
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
    }

}
