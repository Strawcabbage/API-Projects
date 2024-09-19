package ISS_Tracker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;

public class ISS_Tracker {

    public static void main(String[] args) {

        URL location = null;
        URL astros = null;
        try {
            location = new URL("http://api.open-notify.org/iss-now.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            astros = new URL("http://api.open-notify.org/astros.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject locationISS = null;
        JSONObject astronautsISS = null;
        try {
            locationISS = (JSONObject) parser.parse(new InputStreamReader(location.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            astronautsISS = (JSONObject) parser.parse(new InputStreamReader(astros.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray peopleArray = (JSONArray)astronautsISS.get("people");
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Astronauts in orbit of Earth:");
        for (int i = 0; i < peopleArray.size(); i++) {
            System.out.println(peopleArray.get(i));
        }
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.println();
        System.out.println("Location of the ISS: "+locationISS.get("iss_position"));
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        JSONObject longLat = (JSONObject) locationISS.get("iss_position");
        HashMap<String,String> longLat2 = new HashMap<>(longLat);

        System.out.println(longLat2.get("latitude"));
    }

}