package me.woder.trafficremote;

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
	            	   String snd = args[1]+args[2]+args[3]+args[4]+args[5]+args[5];
	            	   traffic.aserial.sendByte((byte)0x1);
	            	   traffic.aserial.sendInt(snd.length());
	            	   traffic.aserial.sendData(snd);
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
