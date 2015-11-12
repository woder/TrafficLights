package me.woder.trafficarduinolink;

public class TrafficArduinoLink {
	ArduinoSerial aserial;
	NetworkHandler nethandle;
	boolean networkReady = false;
	
	public static void main(String[] args){
		new TrafficArduinoLink();
	}
	
	public TrafficArduinoLink(){
		aserial = new ArduinoSerial(this);
		nethandle = new NetworkHandler(this);
		aserial.initialize();

		startLink();
	}
	
	public void startLink(){
		while(true){
			nethandle.tick();
		}
	}

}
