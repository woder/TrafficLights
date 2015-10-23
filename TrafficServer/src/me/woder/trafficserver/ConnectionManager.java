package me.woder.trafficserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager implements Runnable {
	int port;
	TrafficServer global;
	public static Socket sock;
	public DataInputStream reader;
	public static DataInputStream in;
	public static DataOutputStream out;
	
	public ConnectionManager(TrafficServer global, int port){
		this.port = port;
		this.global = global;
	}
	
	public void start(){
        ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(25455);
         while(global.running){
           Socket client = welcomeSocket.accept();
           Thread t = new Thread(new ClientCon(client, global, this));
           t.start();
         }
	  } catch (IOException e) {
		e.printStackTrace();
	 }
	}
	
	public void sendToAll(String message) {
		for (ClientCon player : global.players) {
			player.sendMsg(message);
		}
	}
	
	public void sendNormal(String message){
		for (ClientCon player : global.players) {
			player.sendNormal(message);
		}
	}

	@Override
	public void run() {
		start();		
	}

}
