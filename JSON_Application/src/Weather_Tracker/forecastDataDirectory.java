package Weather_Tracker;

import java.util.*;

import org.json.simple.JSONArray;


public class forecastDataDirectory {

    String weather;
    String temperature;
    String min_temp;
    String max_temp;
    String humidity;
    String pressure;
    String feels_like;
    String wind_speed;
    String wind_direction;
    String date;
    String time;

    public ArrayList<forecastDataDirectory> forecasts = new ArrayList<forecastDataDirectory>();

    public forecastDataDirectory(Object forecast) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> weatherData = (HashMap<String, Object>) forecast;
        JSONArray weatherArray = (JSONArray) weatherData.get("weather");
        @SuppressWarnings("unchecked")
        HashMap<String,String> main = (HashMap<String, String>) weatherData.get("main");
        @SuppressWarnings("unchecked")
        HashMap<String,String> wind = (HashMap<String, String>) weatherData.get("wind");
        String dt = (String) weatherData.get("dt_txt");
        String[] dateTime = dt.split(" ");
        weather = (String) ((HashMap<?, ?>) weatherArray.get(0)).get("description");
        temperature = String.valueOf(main.get("temp"));
        min_temp = String.valueOf(main.get("temp_min"));
        max_temp = String.valueOf(main.get("temp_max"));
        humidity = String.valueOf(main.get("humidity"));
        pressure = String.valueOf(main.get("pressure"));
        feels_like = String.valueOf(main.get("feels_like"));
        wind_speed = String.valueOf(wind.get("speed"));
        wind_direction = String.valueOf(wind.get("deg"));
        date = dateTime[0];
        time = dateTime[1];
    }
}
