package me.woder.trafficremote;

import java.io.IOException;
import java.util.HashMap;

interface Command {
    void runCommand(TrafficRemote traffic, String command, String[] args);
}

public class CommandHandler {
	public TrafficRemote traffic;
	public HashMap<String, Command> commands = new HashMap<String, Command>();
	
	public CommandHandler(TrafficRemote traffic){
		this.traffic = traffic;
		
		commands.put("set", new Command() {
	           @Override
	        public void runCommand(TrafficRemote traffic, String command, String[] args) { 
	               if(args.length > 5){
	                   //in this case we would send the arduino the code for changing the lights
	            	   String snd = args[1]+args[2]+args[3]+args[4]+args[5]+args[6]+args[7]+args[8]+"\n";
	            	   try {
                        traffic.nethandle.sendSet(snd);
                       } catch (IOException e) {
                        e.printStackTrace();
                       }
	            	   traffic.tgui.insertText("Set traffic lights to: " , "black");
	            	   traffic.tgui.insertText("green: " + args[1], "green");
	            	   traffic.tgui.insertText(" yellow: " + args[2], "gold");
	            	   traffic.tgui.insertText(" red: " + args[3], "red");
	            	   traffic.tgui.insertText(" second corner: " , "black");
	            	   traffic.tgui.insertText(" green: " + args[4] , "green");
	            	   traffic.tgui.insertText(" yellow: " + args[5] , "gold");
	            	   traffic.tgui.insertText(" red: " + args[6] + "\n", "red");
	               }
	           };
	       });
		
		commands.put("reset", new Command() {
            @Override
         public void runCommand(TrafficRemote traffic, String command, String[] args) { 
                    //in this case we would send the arduino the code for changing the lights
                   try {
                    traffic.nethandle.sendReset();
                   } catch (IOException e) {
                    e.printStackTrace();
                   }
                    traffic.tgui.insertText("Reset lights" , "black");
            };
        });
				
		commands.put("pause", new Command() {
            @Override
         public void runCommand(TrafficRemote traffic, String command, String[] args) { 
                    //in this case we would send the arduino the code for changing the lights
                   try {
                    traffic.nethandle.sendPause();
                   } catch (IOException e) {
                    e.printStackTrace();
                   }
                    traffic.tgui.insertText("Lights paused" , "black");
            };
        });
		
		commands.put("resume", new Command() {
            @Override
         public void runCommand(TrafficRemote traffic, String command, String[] args) { 
                    //in this case we would send the arduino the code for changing the lights
                   try {
                    traffic.nethandle.sendResume();
                   } catch (IOException e) {
                    e.printStackTrace();
                   }
                    traffic.tgui.insertText("Lights resumed" , "black");
            };
        });
	}
	
	public void processCommand(String command){
		int d = command.length();
        if(command.indexOf(" ") != -1){
          d = command.indexOf(" ");
        }
        String commande = command.substring(0, d);
      
        commande.trim();
        if(commands.containsKey(commande)){
        	   System.out.println("Output: " + commande);
               commands.get(commande).runCommand(traffic, commande, command.substring(d).split(" "));
        }
    }

}
