package me.woder.trafficremote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

import javax.swing.JOptionPane;

public class NetworkHandler {
	Socket sslSocket = null;
	DataOutputStream out;
	DataInputStream in;
	TrafficRemote link;
	Timer time;
	
	public NetworkHandler(TrafficRemote link){
		this.link = link;
		connect();
	}
	
	public void connect(){
	    try {
            sslSocket = new Socket("localhost", 25455);   //not an ssl socket, just pretend since my god ssl is a project of its own
            out = new DataOutputStream(sslSocket.getOutputStream());
            out.writeByte(0x02); //write out a byte to tell the server that we are a link module
            in = new DataInputStream(sslSocket.getInputStream());
            int response = in.read();
            System.out.println("Response: " + response); //the response should be 1 (the server mirrors what we send)
            link.networkReady = true;
        } catch (IOException e) {
            link.tgui.insertText("An error occured! Tell the developers this: " + e.getMessage(), "red");
            time = new java.util.Timer();
            time.schedule( 
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            connect();
                        }
                    }, 
                    10000 
            );
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
	   if(link.networkReady){
		try {
		    byte packetid = in.readByte();
            if(packetid == 1){ //set traffic lights
                String data = in.readUTF();
                System.out.println("Data was: " + data);
                link.tgui.insertText("Lights set to: " + data, "blue");
            }else if(packetid == 2){ //sensor data
                String data = in.readUTF();
                int sensor = in.read();
                System.out.println("Data was: " + data + " " + sensor);
            }
		} catch (IOException e) {
			e.printStackTrace();
			link.networkReady = false;
			link.tgui.insertText("A connection error occured! Retrying in 10 seconds.", "red");
			time = new java.util.Timer();
			time.schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			                time.cancel();
			                connect();
			            }
			        }, 
			        10000 
			);
		}
	   }
	}
	
	public void tick(){
		this.readDataStream();
	}


    public void sendPause() throws IOException {
        out.writeByte(0x02); //write out that our packet id is 1
    }
    
    public void sendResume() throws IOException {
        out.writeByte(0x03); //write out that our packet id is 1
    }

}