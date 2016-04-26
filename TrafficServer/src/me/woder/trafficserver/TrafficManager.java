package me.woder.trafficserver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TrafficManager implements Runnable{
	public int scenes = 7; //the amount of different "scenes we have" is basically just the different ways to light the lights
	public int[] defaultDurations = {10000, 4000, 3000, 10000, 4000, 3000}; //the array that stays static for the time of each scene
	public int[] durations = {10000, 4000, 4000, 3000, 10000, 4000, 4000, 3000}; //the array that can change based on sensor data :o
	public long previousCars = 0;
	public int currentScene = 0; //the scene number we are currently on
	private TrafficServer tServer;
	public long lastPacketTime = 0; //the time of the last packet

	public TrafficManager(TrafficServer trafficServer) {
		this.tServer = trafficServer;
	}

	public void tick() { //this method is called every millisecond 
	   long currentTime = System.currentTimeMillis();
		if(currentTime - previousCars < durations[currentScene]) { //if the current time minus the last time we changed scene is less than the required duration to change scene, then just tick the scene in question
		    try {
				situation(currentScene);
			} catch (IOException e) {
				e.printStackTrace();
			} //tick the scene
		} else {
		    previousCars = currentTime; //reset the time to the current time, since we have just changed the scene
		    if(currentScene >= scenes) { //if we passed the maximum amount of scenes, then reset to 0
		      currentScene = 0;
		    } else {
		      currentScene++; //increase the scene count
		    }
		}
	}
	
	@SuppressWarnings("unchecked")
	public void situation(int sceneNum) throws IOException{
       String lights = ""; //the settings for the lights
       long currentTime = System.currentTimeMillis();
	   
	   if(currentTime - lastPacketTime > 2000){
       byte setone = 0;
       byte settwo = 0;
       
     //000 000 00 L'ordre est: blank blank vert; jaune rouge bleu; orange, flash on/off;
	   switch(sceneNum) {
	    case 0:
	      lights = "10000110\n"; // gyr1, gyr2, ped1, ped2 ; green on, red on, pedestrians for the road thats red go
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      break;            // 1 is ON and 0 is OFF
	    case 1:
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      lights = "10000120\n";
	    case 2:
	      lights = "01000100\n"; // gyr1, gyr2, ped1, ped2 ; yellow on, red on, pedestrians for the road thats red caution
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      break;            // 1 is ON and 0 is OFF
	    case 3:
	      lights = "00100100\n"; // gyr1, gyr2, ped1, ped2 ; red on, red on, pedestrians for the road thats red stop
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      break;
	    case 4:
	      lights = "00110001\n"; // gyr1, gyr2, ped1, ped2 ; red on, green on, pedestrians for the road thats red go
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00);
	      break;
	    case 5:
	      lights = "00110002\n";
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01);
	    case 6:
	      lights = "00101002\n"; // gyr1, gyr2, ped1, ped2 ; green on, yellow on, pedestrians for the road thats red caution
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00);
	      break;            // 1 is ON and 0 is OFF
	    case 7:
	      lights = "00100100\n"; // gyr1, gyr2, ped1, ped2 ; red on, red on, pedestrians for the road thats red stop
	      setone = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      settwo = getSetByte((byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00);
	      break; 
	   }
	  
	    if(tServer.trunning){
		 //System.out.println("The lights are: " + lights);
	     List<ClientCon> players = (LinkedList<ClientCon>) tServer.players.clone();
	     for(ClientCon p : players){ //loop over all the currently open connections looking for the clienttype of 1 (the arduino link module)
             if(p.clientType == 0){
                 p.sendSetByte(setone);
             }else{
            	 p.sendSetByte(settwo);
             }
         }
	    }
	    lastPacketTime = currentTime;
	   }
	}
	
	public void reduceTime(int type){
		if(type == 0){
			
		}else if(type == 1){
			
		}
	}

    public void run() {
        while(true){         
            tick();
        }
    }
    
    public byte getSetByte(byte l8, byte l7, byte l6, byte l5, byte l4, byte l3, byte l2, byte l1) throws IOException{ //modify this to include some sort of ID if we want multiple lights on this server *ugh*		
    	byte send = (byte) (((l8 << 7) & 0x80) | ((l7 << 6) & 0x40) | ((l6 << 5) & 0x20) | ((l5 << 4) & 0x10) | ((l4 << 3) & 0x08) |
    			((l3 << 2) & 0x04) | ((l2 << 1) & 0x02) | (l1 & 0x01));
    	return send;
    }

}
