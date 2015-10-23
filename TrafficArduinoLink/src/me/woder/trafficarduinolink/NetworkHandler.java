package me.woder.trafficarduinolink;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class NetworkHandler {
	Socket sslSocket = null;
	DataOutputStream out;
	DataInputStream in;
	TrafficArduinoLink link;
	
	public NetworkHandler(TrafficArduinoLink link){
		this.link = link;
		try {
			sslSocket = new Socket("localhost", 25455);	 //not an ssl socket, just pretend since my god ssl is a project of its own
			out = new DataOutputStream(sslSocket.getOutputStream());
			in = new DataInputStream(sslSocket.getInputStream());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "An error occured! Tell the developers this: " + e.getMessage());
		}
	}
	

	public void sendSensor(String statuse, int sensor) throws IOException{ //modify this to include some sort of ID if we want multiple lights on this server *ugh*		
		out.writeByte(0x01); //write out that our packet id is 1
		out.writeUTF(statuse); //write out the light status
		out.writeInt(sensor);
		
		int result = in.readByte(); //we expect a result, otherwise we assume something went wrong
		if(result == 0x01){
		   System.out.println("Sensor data transmited successfully");		   
		   out.close();
		   in.close();
		   sslSocket.close();
		}else if(result == 0x02){
		   System.out.println("Could not verify origin, please check server log");
		   JOptionPane.showMessageDialog(null, "Could not verify origin, please check server log");
		   out.close();
		   in.close();
		   sslSocket.close();
		}
		System.out.println("Server says: " + result);
	}
	
	public void readDataStream(){
		try {
			byte packetid = in.readByte();
			if(packetid == 1){
				String data = in.readUTF();
				link.aserial.sendByte((byte)0x1);
				link.aserial.sendData(data);
			}else if(packetid == 0){
				link.aserial.sendByte((byte)0x2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tick(){
		this.readDataStream();
	}

}