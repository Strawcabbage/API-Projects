package ISS_Tracker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class ISS_Tracker_Over_Time {


    static ArrayList<LongLat> longLat = new ArrayList<LongLat>();
    static ArrayList<Double> timeTracker = new ArrayList<Double>();
    static double distance = 0;
    //Main Method

    public static void main(String[] args) {

        //inputs
        System.out.print("How large of a gap would you like to have in between ISS location data entries (seconds): ");
        Scanner dataGap = new Scanner(System.in);
        int desiredDataGapSecs = dataGap.nextInt();
        System.out.print("How many entries of ISS location data would you like to have: ");
        Scanner dataOutput = new Scanner(System.in);
        int amountOfDataOutputs = dataOutput.nextInt();


        int dataOutputs = amountOfDataOutputs + 1;
        int desiredDataGapMillis = desiredDataGapSecs*1000;
        double desiredDataGapMins = desiredDataGapSecs / 60.0;
        String currentTime = getClock();
        timeTracker.add(0, getClockMillis());
        URL astros = null;
        try {
            astros = new URL("http://api.open-notify.org/astros.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject astronautsISS = null;
        try {
            astronautsISS = (JSONObject) parser.parse(new InputStreamReader(astros.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray peopleArray = (JSONArray)astronautsISS.get("people");

        //Printables

        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Astronauts in orbit of Earth on the ISS:");
        for (int i = 0; i < peopleArray.size(); i++) {
            System.out.println(peopleArray.get(i));
        }
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        for (int i = 0; i < dataOutputs; i++) {

            currentTime = getClock();
            System.out.print("Current Time: " + currentTime);
            if (i != 0) {
                System.out.print("   :   Time between data entries is " + desiredDataGapSecs + "s");
            }
            JSONObject locationISS = getLocationISS();
            System.out.println("   :   Location of the ISS: "+locationISS);
            LongLat ll = new LongLat(getLongitude(), getLatitude());
            longLat.add(ll);
            if (i > 0) {
                timeTracker.add(1, getClockMillis());
                distance = longLat.get(i-1).calcDistance(ll);
                System.out.println("Distance travelled by the ISS between data points: " + distance + " miles (" + milesToKilo() + " km)");
                System.out.println("Current velocity of the ISS: " + LongLat.calcVel(timeTracker.get(0), timeTracker.get(1), milesToKilo()) + " m/s");
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------");
            timeTracker.add(0, getClockMillis());
            if (i == amountOfDataOutputs) {
                System.out.println("Total distance travelled by the ISS after the " + i + ", " + desiredDataGapMins + " minute intervals of data points is " + longLat.get(i-amountOfDataOutputs).calcDistance(ll) + " miles (" + (longLat.get(i-amountOfDataOutputs).calcDistance(ll) * 1.60934) + " km)");
            } else {
                try {
                    Thread.sleep(desiredDataGapMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
    }

    //Methods

    public static JSONObject getLocationISS() {
        URL location = null;
        try {
            location = new URL("http://api.open-notify.org/iss-now.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject locationISS = null;
        try {
            locationISS = (JSONObject) parser.parse(new InputStreamReader(location.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (JSONObject) locationISS.get("iss_position");
    }

    public static String getClock() {
        Clock baseClock = Clock.systemDefaultZone();
        Clock clock = Clock.offset(baseClock, Duration.ofHours(5));
        Instant instant = clock.instant();
        return instant.toString();
    }

    public static double getLatitude() {
        @SuppressWarnings("unchecked")
        HashMap<String,String> longLat = new HashMap<>(getLocationISS());
        return Double.valueOf(longLat.get("latitude"));
    }

    public static double getLongitude() {
        @SuppressWarnings("unchecked")
        HashMap<String,String> longLat = new HashMap<>(getLocationISS());
        return Double.valueOf(longLat.get("longitude"));
    }

    public static double getClockMillis() {
        return System.currentTimeMillis();
    }

    public static double milesToKilo() {
        return distance * 1.60934;
    }
}

