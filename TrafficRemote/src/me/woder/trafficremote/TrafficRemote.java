package me.woder.trafficremote;

import java.io.IOException;

public class TrafficRemote {
	public TrafficGui tgui;
	public TNetwork tnet;
	public CommandHandler chandle;
	public NetworkHandler nethandle;
	boolean networkReady = false;
	
	public static void main(String[] args){
		new TrafficRemote();
	}
	
	public TrafficRemote(){
		tgui = new TrafficGui(this);
		tnet = new TNetwork(this);
		chandle = new CommandHandler(this);
		//aserial = new ArduinoSerial(this);
		nethandle = new NetworkHandler(this);
		//networkReady = aserial.initialize();
		startClient();
	}
	
	public void startClient(){
		while(true){
			//proccess some stuff here
			tgui.tick();
			nethandle.tick();
		}
	}
	

}
