package me.woder.trafficremote;

import java.io.IOException;

public class TrafficRemote {
	public TrafficGui tgui;
	public TNetwork tnet;
	public CommandHandler chandle;
	public ArduinoSerial aserial;
	boolean networkReady = false;
	
	public static void main(String[] args){
		new TrafficRemote();
	}
	
	public TrafficRemote(){
		tgui = new TrafficGui(this);
		tnet = new TNetwork(this);
		chandle = new CommandHandler(this);
		aserial = new ArduinoSerial(this);
		networkReady = aserial.initialize();
		if(networkReady){
		   startClient();
		}else{
			try {
				throw new IOException("Something went wrong");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startClient(){
		while(true){
			//proccess some stuff here
			tgui.tick();
			tnet.tick();
		}
	}
	

}
