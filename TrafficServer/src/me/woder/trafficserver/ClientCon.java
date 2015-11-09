package me.woder.trafficserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientCon implements Runnable {
@SuppressWarnings("unused")
private Socket client;
private TrafficServer global;
private DataInputStream in;
private DataOutputStream out;
@SuppressWarnings("unused")
private ConnectionManager connection;
public byte clientType = 0x00;

public ClientCon(Socket client, TrafficServer global, ConnectionManager connection) {
    this.client = client;
    this.global = global;
    this.connection = connection;
    try {
        /* obtain an input stream to this client ... */
    	 in = new DataInputStream(client.getInputStream());
    	 clientType = in.readByte();
         out = new DataOutputStream(client.getOutputStream());
         out.writeByte(clientType);
         global.players.add(this);   
    } catch (IOException e) {
        System.err.println(e);
        return;
    }
}

public void run() { //TODO make this relevant to the network code of the client
    try {
        /* loop reading lines from the client which are processed 
         * according to our protocol and the resulting response is 
         * sent back to the client */
        while (global.running) {
            int id = in.readByte();
            global.log.info("" + id);
            if(clientType == 1){
              if(id == 0x01){
           		String stats = in.readUTF();
           		int sensor = in.readInt();   	 
              }
            }else if(id == 0x03){
              global.trunning = false;
              global.log.info("Tmanager stopped");
            }else if(id == 0x04){
              global.trunning = true;
              global.log.info("Tmanager started");
            }else{
                if(id == 0x01){
                    String stats = in.readUTF();      
                    System.out.println("Set command received: " + stats);
                    List<ClientCon> players = (LinkedList<ClientCon>) global.players.clone();
                    for(ClientCon p : players){ //loop over all the currently open connections looking for the clienttype of 1 (the arduino link module)
                            p.sendSet(stats);
                    }
                }else if(id == 0x02){
                	List<ClientCon> players = (LinkedList<ClientCon>) global.players.clone();
                    for(ClientCon p : players){ //loop over all the currently open connections looking for the clienttype of 1 (the arduino link module)
                            p.sendReset();
                    }
                }
            }
        }
    } catch (IOException e) {
        System.err.println(e);
       //opps, lets cancel our relation with this client, since we lost them
        global.players.remove(this);
    }
}

public void sendSet(String lights) { //method to send the set command on to arduinolink modules
	try {
     out.writeByte(0x01);
     out.writeUTF(lights);
     out.flush();
    } catch (IOException e) {
	e.printStackTrace();
    }
 }

 public void sendReset(){
	 try {
	     out.writeByte(0x02);
	     out.flush();
	    } catch (IOException e) {
		e.printStackTrace();
	 }
 }
}
