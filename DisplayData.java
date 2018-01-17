package uk.ac.mmu.a15072935.phidgetvisualizer;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayData extends AppCompatActivity { // activity for displaying the data

    public static String sensorServerURL = "http://10.0.2.2:8080/PhidgetServer/sensorToDB"; // the base URL, this part of the URL is common to all the URLs needed
    String sensorNameFromIntent = ""; // the name of the sensor from the previous activity to be put all to lower case
    String sensorNameFromIntentCap = ""; // the name of the sensor from the previous activity but with a capital first letter
    Button btnCommit;
    Button actMotor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaydata);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = getIntent().getExtras();
        String newString;
        if (savedInstanceState == null) { //This gets the intent from the mainActivity
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED"); // STRING_I_NEED is the key for the String that is being passed between activities
        }
        sensorNameFromIntent = newString.toLowerCase();
        sensorNameFromIntentCap = newString;
        TextView sliderValue = (TextView) findViewById(R.id.sensorValue1ID);
        TextView sliderValueTime = (TextView) findViewById(R.id.sensor1TimeID);
        TextView sensorNameID = (TextView) findViewById(R.id.sensor1TypeID);
        final EditText inputID = (EditText) findViewById(R.id.inputFieldID);
        btnCommit = (Button) findViewById(R.id.commitButtonID); // declare all the features of the activity for accessing
        btnCommit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String numberToInput = inputID.getText().toString(); // when the "send to server" button is clicked
                sendToServer(sensorNameFromIntent, numberToInput); // send the info to the server with the sendToServer method
            }
        });

        actMotor = (Button) findViewById(R.id.actuateID);
        actMotor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String numberToInput = inputID.getText().toString(); // when the "send to server" button is clicked
            }
        });


        String firstParam = getSensorData((sensorNameFromIntent));
        //System.out.println(firstParam); This is the complete JSONString
        try {
            JSONObject sensorObject = new JSONObject(firstParam); // get the JSON string
            JSONObject sensObj = sensorObject.getJSONObject("sensor"); // get the object
            String sensName = sensObj.getString("name"); // find the value with the key: "name"
            String sensVal = sensObj.getString("value"); // find the value associated with the key: "value"
            String sensTime = sensObj.getString("time"); // find the value associated with the key: "time"

            sensorNameID.setText("Sensor Type: " + sensorNameFromIntentCap); //Might as well use the sensor name given from the intent as they will be the same. If you would like to use the JSON name use sensName variable
            sliderValue.setText("Sensor Value: " + sensVal);
            sliderValueTime.setText("Time added: " + sensTime);


        } catch (JSONException e) {
            Log.e("PhidgetVisualizer","unexpected JSON exception",e); // in case the JSON doesn't work
        }
    }
    public String getSensorData(String sensorName) {
        String sensorServerURL = "http://10.0.2.2:8080/PhidgetServer/sensorToDB?getdata=true&"; // this part of the URL is always present when getting data
        //http://localhost:8080/PhidgetServer/sensorToDB?getdata=true&sensorname=slider
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String fullURL = sensorServerURL + "sensorname="+sensorName;
        System.out.println("Requesting data from: "+fullURL);
        String line;
        String result = "";
        try {
            //System.out.println("I have entered the try");
            url = new URL(fullURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //System.out.println("I am just before the while loop rn");
            while ((line = rd.readLine()) != null) {
                System.out.println(result);
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("I'm at the end of the get sensor data function. The result is: " + result);
        return result; // returns the data from the URL which is in JSON format
    }
    public String sendToServer(String sensorName, String sensorValue){ // method for sending data from the client to the database via the URL
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String fullURL = sensorServerURL + "?sensorname="+sensorName + "&sensorvalue=" + sensorValue; // create the URL to send data
        System.out.println("Sending data to: "+fullURL); // debug message
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
}
