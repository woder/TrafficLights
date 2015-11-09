package me.woder.trafficremote;

import java.io.IOException;

public class TrafficRemote {
	public TrafficGui tgui;
	public CommandHandler chandle;
	public NetworkHandler nethandle;
	boolean networkReady = false;
	Thread nethandlee;
	
	public static void main(String[] args){
		new TrafficRemote();
	}
	
	public TrafficRemote(){
		tgui = new TrafficGui(this);
		chandle = new CommandHandler(this);
		//aserial = new ArduinoSerial(this);
		nethandle = new NetworkHandler(this);
		nethandlee = new Thread(nethandle,"T1");
        nethandlee.start();
		//networkReady = aserial.initialize();
		startClient();
	}
	
	public void startClient(){
		while(true){
			//proccess some stuff here
			tgui.tick();
		}
	}
	

}
