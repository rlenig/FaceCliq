package com.example.facecliq;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import android.util.Log;


public class jsonParser {

	public jsonParser(){
	}

	 public JSONObject getJSONfromURL (String url){
		//initialize
		 JSONObject object = null;
	        //HTTP call
	        try{
	            URLConnection connection = new URL(url).openConnection();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 2048 * 16);
	            StringBuffer builder = new StringBuffer();
	            String line;

	            while ((line = reader.readLine()) != null) {
	              builder.append(line).append("\n");
	              
	              Log.e("VALUE",line);
	            }
	            String blah = builder.toString();

	            //Parsing string into JSONArray
	            object = new JSONObject ( new String(builder.toString()) );
	           

	            } catch(Exception e){
	                Log.e("Http Error","Error in http connection " + e.toString());
	            }


	        return object;
	    }
}
