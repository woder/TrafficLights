package me.woder.trafficserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientCon implements Runnable {
@SuppressWarnings("unused")
private Socket client;
private TrafficServer global;
private DataInputStream in;
private DataOutputStream out;
@SuppressWarnings("unused")
private ConnectionManager connection;

public ClientCon(Socket client, TrafficServer global, ConnectionManager connection) {
    this.client = client;
    this.global = global;
    this.connection = connection;
    try {
        /* obtain an input stream to this client ... */
    	 in = new DataInputStream(client.getInputStream());
         out = new DataOutputStream(client.getOutputStream());
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
            if(id == 0x01){
           		global.log.info("We just granted someone access!");
           	    out.writeByte(0x01);
           	    out.writeByte(0x02);
           	    out.flush();
           	    global.players.add(this);       	 
            }else if(id == 0x02){
               int length = in.readShort();
               String password = global.readString(in, length-1);
           	    length = in.readShort();
           	    String message = global.readString(in, length-1);
            }else if(id == 0x04){
            	int length = in.readShort();
                String password = global.readString(in, length-1);
                length = in.readShort();
                String message = global.readString(in, length-1);
            }else if(id == 0x05){
            	int length = in.readShort();
                String password = global.readString(in, length-1);
                length = in.readShort();
                String message = global.readString(in, length-1);
            }
        }
    } catch (IOException e) {
        System.err.println(e);
    }
}

public void sendMsg(String message) {
	try {
     out.writeByte(0x03);
     out.writeShort(message.length());
     out.writeChars(message);
     out.flush();
    } catch (IOException e) {
	e.printStackTrace();
    }
 }

 public void sendNormal(String message){
	 try {
	     out.writeByte(0x06);
	     out.writeShort(message.length());
	     out.writeChars(message);
	     out.flush();
	    } catch (IOException e) {
		e.printStackTrace();
	 }
 }
}
