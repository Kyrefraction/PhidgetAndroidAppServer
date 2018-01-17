import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.phidget22.*;

public class SensorToServer  {
		String sname = "";
		String value = "";
    
    VoltageRatioInput slider = new VoltageRatioInput();
    VoltageRatioInput force = new VoltageRatioInput();
	static RCServo ch;
	static int motorDegree = 0;

    
    int lastSensorValue = 0;

    // address of server which will receive sensor data
    public static String sensorServerURL = "http://localhost:8080/PhidgetServer/sensorToDB";
     public static void main(String[] args) throws PhidgetException {

        new SensorToServer();
    }

    public SensorToServer() throws PhidgetException {
    	
    	int serialNumber = 316694;
        ch = new RCServo();
        
    	
        // This is the id of your PhidgetInterfaceKit (on back of device)
        slider.setDeviceSerialNumber(serialNumber);
        // This is the channel your slider is connected to on the interface kit
        slider.setChannel(0);
        slider.open(8080);

        slider.addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
  			public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent e) {
  				double sensorReading = e.getVoltageRatio();
  				//System.out.printf("Slider Voltage Ratio Changed: %.3g\n", sensorReading);
  				int scaledSensorReading = (int) (100 * sensorReading);
  				motorDegree = scaledSensorReading;
  				String properReading = "" + scaledSensorReading;
  				if(scaledSensorReading != lastSensorValue && properReading != null) {
  					lastSensorValue = scaledSensorReading;
  					System.out.println("Sending new SLIDER value : " + scaledSensorReading);
  					sname = "slider";
  					sendToServer(sname,properReading);
  		            try {
						ch.setTargetPosition(motorDegree);
					} catch (PhidgetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
  		            try {
						ch.setEngaged(true);
					} catch (PhidgetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
  				} else {
  					System.out.println("Sensor reading unchanged");
  				}
  			}
         });
        force.setDeviceSerialNumber(serialNumber);
        // This is the channel your slider is connected to on the interface kit
        force.setChannel(1);
        force.open(8080);
        force.addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
  			public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent e) {
  				double sensorReading = e.getVoltageRatio();
  				//System.out.printf("Slider Voltage Ratio Changed: %.3g\n", sensorReading);
  				int scaledSensorReading = (int) (1000 * sensorReading);
  				String properReading = "" + scaledSensorReading;
  				
  				if(scaledSensorReading != lastSensorValue && properReading != null) {
  					lastSensorValue = scaledSensorReading;
  					System.out.println("Sending new FORCE value : " + scaledSensorReading);
  					sname = "force";
  					sendToServer(sname,properReading);
  				} else {
  					System.out.println("Sensor reading unchanged");
  				}
  			}
         });
        ch.open(8080);
 
         // attach to the sensor and start reading
        try {      
                            
            System.out.println("\n\nGathering data for 5 seconds\n\n"); 
            pause(5);
            slider.close();
            force.close();
            ch.close();
            System.out.println("\nClosed Motor Servo");
            //sendToServer(sname,value);
            System.out.println("\nClosed slider Voltage Ratio Input");
            System.out.println("\nClosed force Voltage Ratio Input");

            
        } catch (PhidgetException ex) {
            System.out.println(ex.getDescription());
        }
 }

    public String sendToServer(String sensorName, String sensorValue){
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String fullURL = sensorServerURL + "?sensorname="+sensorName + "&sensorvalue=" + sensorValue;
        System.out.println("Sending data to: "+fullURL);
        String line;
        String result = "";
        try {
           url = new URL(fullURL);
           conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
           while ((line = rd.readLine()) != null) {
              result += line;
           }
           rd.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }
	private void pause(int secs){
        try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}