package me.woder.trafficarduinolink;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author ericjbruno
 */
public class ArduinoSerial implements SerialPortEventListener {
    SerialPort serialPort = null;
    TrafficArduinoLink link;

    private static final String PORT_NAMES[] = { 
        //"/dev/tty.usbmodem", // Mac OS X
//        "/dev/usbdev", // Linux
//        "/dev/tty", // Linux
//        "/dev/serial", // Linux
        "COM1",
        "COM3",
        "COM4", // Windows
        "COM5", //windows
    };
    
    private String appName;
    private BufferedReader input;
    public OutputStream output;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 115200; // Arduino serial port

    public boolean initialize() {
        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            // Enumerate system ports and try connecting to Arduino over each
            //
            System.out.println( "Trying:");
            while (portId == null && portEnum.hasMoreElements()) {
                // Iterate through your host computer's serial port IDs
                //
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                System.out.println( "   port" + currPortId.getName());
                if(currPortId.getName().contains("COM4")){
                    	//int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to use " + currPortId.getName(),"Warning", JOptionPane.YES_NO_OPTION);
                        //if(dialogResult == 0){ //they said yes to use this port, try to connect
                         // Try to connect to the Arduino on this port
                         //
                         // Open serial port
                         serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
                         portId = currPortId;
                         System.out.println( "Connected on port" + currPortId.getName() );
                         break;
                       // }
                }
                                   
            }
        
            if (portId == null || serialPort == null) {
                System.out.println("Oops... Could not connect to Arduino");
                return false;
            }
        
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            
            output = serialPort.getOutputStream();

            // Give the Arduino some time
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            
            return true;
        }
        catch ( Exception e ) { 
            e.printStackTrace();
        }
        return false;
    }
    
    void sendData(String data) {
        try {
            System.out.println("Sending data: '" + data +"'");
            
            // open the streams and send the "y" character
            output.write( data.getBytes() );
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }
    
    void sendByte(int data) {
        try {
            System.out.println("Sending data: '" + data +"'");
            
            // open the streams and send the "y" character
            output.write(data);
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }
    
    void sendInt(int data){
    	try {
            System.out.println("Sending data: '" + data +"'");
            
            // open the streams and send the "y" character
            output.write(data);
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }

    //
    // This should be called when you stop using the port
    //
    public synchronized void close() {
        if ( serialPort != null ) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    //
    // Handle serial port event
    //
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        //System.out.println("Event received: " + oEvent.toString());
        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE: 
                    if ( input == null ) {
                        input = new BufferedReader(
                            new InputStreamReader(
                                    serialPort.getInputStream()));
                    }
                    String lol = input.readLine();
                    System.out.println(lol);
                    
                    int one = Integer.parseInt(lol);
                    
                    link.nethandle.sendOne(one);
                  
                    /*int id = input.read();
                    System.out.println(id);
                    if(id == 2){
                     //String inputLine = input.readLine(); //00000000 green yellow red green yellow red p1 p2
                     int sensor = input.read();
                     System.out.println("WOAHHHH 1 " + sensor);
                    }else if(id == 3){
                     int sensor = input.read();
                     System.out.println("WOAHHHH 2 " + sensor);
                    }else if(id == 4){
                     int sensor = input.read();
                     System.out.println("WOAHHHH 3 " + sensor);
                    }else if(id == 5){
                     int sensor = input.read();
                     System.out.println("WOAHHHH " + sensor);
                    }*/
                    break;

                default:
                    break;
            }
        } 
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

	public ArduinoSerial(TrafficArduinoLink link) {
		this.link = link;
		appName = getClass().getName();
	}

}



