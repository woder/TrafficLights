package me.woder.trafficremote;

public class TrafficRemote {
	public TrafficGui tgui;
	public TNetwork tnet;
	public CommandHandler chandle;
	
	public static void main(String[] args){
		new TrafficRemote();
	}
	
	public TrafficRemote(){
		tgui = new TrafficGui(this);
		tnet = new TNetwork(this);
		chandle = new CommandHandler(this);
		startClient();
	}
	
	public void startClient(){
		while(true){
			//proccess some stuff here
			tgui.tick();
			tnet.tick();
		}
	}
	

}
