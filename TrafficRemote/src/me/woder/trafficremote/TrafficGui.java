package me.woder.trafficremote;

import javax.swing.JFrame;


public class TrafficGui {
	
	public TrafficGui() {
        JFrame window = new JFrame("TrafficControl");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TrafficContentPane gui = new TrafficContentPane(window);
        window.setContentPane(gui);       
    }

}
