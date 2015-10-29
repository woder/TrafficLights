package me.woder.trafficserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrafficManager {
	public int scenes = 5; //the amount of different "scenes we have" is basically just the different ways to light the lights
	public int[] durations = {4000, 4000, 3000, 9000, 4000, 3000};
	public long previousCars = 0;
	public int currentScene = 0; //the scene number we are currently on
	private TrafficServer tServer;
	public int jacob = 0;

	public TrafficManager(TrafficServer trafficServer) {
		this.tServer = trafficServer;
	}

	public void tick() { //this method is called every millisecond
		long currentTime = System.currentTimeMillis();
		if(currentTime - previousCars < durations[currentScene]) { //if the current time minus the last time we changed scene is less than the required duration to change scene, then just tick the scene in question
		    situation(currentScene); //tick the scene
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
	public void situation(int sceneNum){
       String lights = ""; //the settings for the lights
       
	   switch(sceneNum) {
	    case 0:
	      lights = "10000110"; // gyr1, gyr2, ped1, ped2 ; green on, red on, pedestrians for the road thats red go
	      break;            // 1 is ON and 0 is OFF
	    case 1:
	      lights = "10000120"; // gyr1, gyr2, ped1, ped2 ; yellow on, red on, pedestrians for the road thats red caution
	      break;            // 1 is ON and 0 is OFF
	    case 2:
	      lights = "00100100"; // gyr1, gyr2, ped1, ped2 ; red on, red on, pedestrians for the road thats red stop
	      break;
	    case 3:
	      lights = "00110001"; // gyr1, gyr2, ped1, ped2 ; red on, green on, pedestrians for the road thats red go
	      break;
	    case 4:
	      lights = "10000102"; // gyr1, gyr2, ped1, ped2 ; red on, yellow on, pedestrians for the road thats red caution
	      break;            // 1 is ON and 0 is OFF
	    case 5:
	      lights = "01000100"; // gyr1, gyr2, ped1, ped2 ; red on, red on, pedestrians for the road thats red stop
	      break;
	   }
	   
	   if(jacob > 100000000){
		System.out.println("Triggered");
	    List<ClientCon> players = (LinkedList<ClientCon>) tServer.players.clone();
	    for(ClientCon p : players){ //loop over all the currently open connections looking for the clienttype of 1 (the arduino link module)
            if(p.clientType == 1){
                p.sendSet(lights);
            }
        }
	    jacob = 0;
	   }else{
		   jacob++;
	   }
	}

}
