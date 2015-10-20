package me.woder.trafficremote;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TrafficContentPane extends JPanel{
	Image image; //the background image

	public TrafficContentPane(JFrame window) {
		try{
			image = ImageIO.read(new File("background.jpg"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}
