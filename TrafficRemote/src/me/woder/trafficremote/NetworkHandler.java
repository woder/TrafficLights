package me.woder.trafficremote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class NetworkHandler {
	Socket sslSocket = null;
	DataOutputStream out;
	DataInputStream in;
	TrafficRemote link;
	
	public NetworkHandler(TrafficRemote link){
		this.link = link;
		try {
			sslSocket = new Socket("wltd.org", 25455);	 //not an ssl socket, just pretend since my god ssl is a project of its own
			out = new DataOutputStream(sslSocket.getOutputStream());
			out.writeByte(0x02); //write out a byte to tell the server that we are a link module
			in = new DataInputStream(sslSocket.getInputStream());
			int response = in.read();
			System.out.println("Response: " + response); //the response should be 1 (the server mirrors what we send)
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "An error occured! Tell the developers this: " + e.getMessage());
		}
	}
	

	public void sendSet(String values) throws IOException{ //modify this to include some sort of ID if we want multiple lights on this server *ugh*		
		out.writeByte(0x01); //write out that our packet id is 1
		out.writeUTF(values); //write out the light status
		
		int result = in.readByte(); //we expect a result, otherwise we assume something went wrong
		if(result == 0x01){
		   System.out.println("Command transmited successfully");
		}else if(result == 0x02){
		   System.out.println("Could not verify origin, please check server log");
		   JOptionPane.showMessageDialog(null, "Could not verify origin, please check server log");
		}
		System.out.println("Server says: " + result);
	}
	
	public void sendReset() throws IOException{
	    out.writeByte(0x02); //write out that our packet id is 1
        
        int result = in.readByte(); //we expect a result, otherwise we assume something went wrong
        if(result == 0x01){
           System.out.println("Command transmited successfully");
        }else if(result == 0x02){
           System.out.println("Could not verify origin, please check server log");
           JOptionPane.showMessageDialog(null, "Could not verify origin, please check server log");
        }
        System.out.println("Server says: " + result);
	}
	
	public void readDataStream(){
		try {
			byte packetid = in.readByte();
			if(packetid == 1){
				String data = in.readUTF();
				int sensor = in.read();
				System.out.println("Data was: " + data + " " + sensor);
			}else if(packetid == 2){
				//idk what this packet is
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tick(){
		this.readDataStream();
	}

}