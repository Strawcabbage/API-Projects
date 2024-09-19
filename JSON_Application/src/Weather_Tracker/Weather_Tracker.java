package Weather_Tracker;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Weather_Tracker {

    public static Scanner input = new Scanner(System.in);
    public static String stateCode = "00";
    public static String cityName = "";
    public static String countryCode = "";
    public static String cnt = "0";
    public int valid = 0;
    public static ArrayList<forecastDataDirectory> forecasts = new ArrayList<forecastDataDirectory>();
    public static void main(String[] args) throws IOException{

        System.out.print("Enter a country name: ");
        String country = input.nextLine();
        if ((country.toUpperCase()).equals("UNITED STATES") || (country.toUpperCase()).equals("UNITED STATES OF AMERICA") || (country.toUpperCase()).equals("USA") || (country.toUpperCase()).equals("US")) {
            System.out.print("Enter a US state: ");
            while (!validState(stateCode = input.nextLine())) {
                System.out.print("Invalid state, please enter another state name: ");
            }
            System.out.print("Enter the name of a city in that state: ");
            cityName = input.nextLine();
        } else {
            countryCode = countryCodes(country);
            while (countryCode == null) {
                System.out.print("Invalid country, please enter another country name: ");
                countryCode = countryCodes(input.nextLine());
            }
            System.out.print("Enter the name of a city in that country: ");
            cityName = input.nextLine();
        }
        HashMap<String, String> currentData = new HashMap<String, String>(dataDirectory_CurrentData());
        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println();
        System.out.println("Categories for current weather:");
        System.out.println();
        System.out.println("Description: " + currentData.get("description"));
        System.out.println("Temperature: " + currentData.get("temperature") + " degrees Fahrenheit");
        System.out.println("Maximum Temperature: " + currentData.get("maximum temperature") + " degrees Fahrenheit");
        System.out.println("Minimum Temperature: " + currentData.get("minimum temperature") + " degrees Fahrenheit");
        System.out.println("Feels Like: " + currentData.get("feels like") + " degrees Fahrenheit");
        System.out.println("Humidity: " + currentData.get("humidity"));
        System.out.println("Pressure: " + currentData.get("pressure"));
        System.out.println("Visibility: " + currentData.get("visibility"));
        System.out.println("Wind speed: " + currentData.get("wind speed"));
        System.out.println("Wind direction: " + currentData.get("wind direction"));
        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println();
        System.out.print("How many intervals of 3 hour forecasts do you want? (max 40): ");
        int valid = input.nextInt();
        while (valid < 1 || valid > 40) {
            System.out.print("Invalid amount of forecast intervals, please enter again (between 0-41): ");
            valid = input.nextInt();
        }
        cnt = String.valueOf(valid);
        dataDirectory_FiveDayThreeHour();
        String descriptions = "";
        String dates = "";
        String temperatures = "";
        String times = "";
        int spacer = 1;
        int counter = 16;
        for (int i = 0; i < valid; i++) {

            if (spacer < 9) {
                spacer += 1;

                descriptions = descriptions.concat(forecasts.get(i).weather);
                String spaces = "";
                for (int j = descriptions.length(); j < counter; j++) {
                    spaces = spaces.concat(" ");
                }
                descriptions = descriptions.concat(spaces);
                dates = dates.concat(forecasts.get(i).date);
                spaces = "";
                for (int j = dates.length(); j < counter; j++) {
                    spaces = spaces.concat(" ");
                }
                dates = dates.concat(spaces);
                spaces = "";
                times = times.concat(forecasts.get(i).time);
                for (int j = times.length(); j < counter; j++) {
                    spaces = spaces.concat(" ");
                }
                times = times.concat(spaces);
                spaces = "";
                temperatures = temperatures.concat(forecasts.get(i).temperature);
                temperatures = temperatures.concat(" F");
                for (int j = temperatures.length(); j < counter; j++) {
                    spaces = spaces.concat(" ");
                }
                temperatures = temperatures.concat(spaces);
                descriptions = descriptions.concat("|");
                dates = dates.concat("|");
                times = times.concat("|");
                temperatures = temperatures.concat("|");
                counter += 17;
            } else {
                System.out.println(dates);
                System.out.println(times);
                System.out.println(descriptions);
                System.out.println(temperatures);
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
                spacer = 1;
                counter = 16;
                dates = "";
                times = "";
                descriptions = "";
                temperatures = "";
            }
        }
        System.out.println(dates);
        System.out.println(times);
        System.out.println(descriptions);
        System.out.println(temperatures);
    }

    @SuppressWarnings("unchecked")
    public static JSONObject getLocationData() {
        URL location = null;
        String locationURL = "";
        if (countryCode.equals("US")) {
            String firstPartURL = "http://api.openweathermap.org/geo/1.0/direct?q=";
            String secondPartURL = ","+stateCode+",US&appid=7f76e988e886b42ef672a684c7c926e1";
            locationURL = firstPartURL + cityName + secondPartURL;
        } else {
            String firstPartURL = "http://api.openweathermap.org/geo/1.0/direct?q=";
            String secondPartURL = "," + countryCode + "&appid=7f76e988e886b42ef672a684c7c926e1";
            locationURL = firstPartURL + cityName + secondPartURL;
        }

        try {
            location = new URL(locationURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(location.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(br);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray();
        array.add(obj);
        ArrayList<?> weatherArray = (ArrayList<?>) array.get(0);
        JSONObject getter = (JSONObject) weatherArray.get(0);
        return getter;
    }

    public static JSONObject currentWeatherData() {
        URL currentWeatherURL = null;
        try {
            currentWeatherURL = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + (double) getLocationData().get("lat") + "&lon=" + ((double) getLocationData().get("lon")) + "&appid=7f76e988e886b42ef672a684c7c926e1&units=imperial");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(currentWeatherURL.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject currentWeatherData = null;
        try {
            currentWeatherData = (JSONObject) parser.parse(br);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentWeatherData;
    }

    public static HashMap<String, String> dataDirectory_CurrentData() {
        HashMap<String, String> dataDirectory = new HashMap<String, String>();
        dataDirectory.put("temperature", String.valueOf(((JSONObject) currentWeatherData().get("main")).get("temp")));
        dataDirectory.put("humidity", String.valueOf(((JSONObject) currentWeatherData().get("main")).get("humidity")));
        dataDirectory.put("pressure", String.valueOf(((JSONObject) currentWeatherData().get("main")).get("pressure")));
        dataDirectory.put("feels like", String.valueOf(((JSONObject) currentWeatherData().get("main")).get("feels_like")));
        dataDirectory.put("minimum temperature", String.valueOf(((JSONObject) currentWeatherData().get("main")).get("temp_min")));
        dataDirectory.put("maximum temperature", String.valueOf(((JSONObject) currentWeatherData().get("main")).get("temp_max")));
        dataDirectory.put("visibility", ((currentWeatherData().get("visibility"))).toString());
        JSONArray weatherArray = (JSONArray) currentWeatherData().get("weather");
        dataDirectory.put("description", (String)  ((HashMap<?, ?>) weatherArray.get(0)).get("description"));
        dataDirectory.put("wind speed", String.valueOf(((JSONObject) currentWeatherData().get("wind")).get("speed")));
        dataDirectory.put("wind direction", String.valueOf(((JSONObject) currentWeatherData().get("wind")).get("deg")));
        return dataDirectory;
    }

	/* public static JSONObject hourlyForecast() {
		URL hourlyWeatherURL = null;
		try {
			hourlyWeatherURL = new URL("https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=" + (double) getLocationData().get("lat") + "&lon=" + ((double) getLocationData().get("lon")) + "&appid=c8d72d4f2b6577c79954d629ee7ad7e5");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		BufferedReader br2 = null;
		try {
			br2 = new BufferedReader(new InputStreamReader(hourlyWeatherURL.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser();
		JSONObject hourlyWeatherData = null;
		try {
			hourlyWeatherData = (JSONObject) parser.parse(br2);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hourlyWeatherData;
	}*/

    public static JSONObject fiveDayThreeHourForecast() {
        URL threeHourWeatherURL = null;
        try {
            threeHourWeatherURL = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + (double) getLocationData().get("lat") + "&lon=" + (double) getLocationData().get("lon") + "&cnt=" + cnt + "&appid=bd44d3c59f19a4fc0ce69d8b27b739e0&units=imperial");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(threeHourWeatherURL.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject threeHourWeatherData = null;
        try {
            threeHourWeatherData = (JSONObject) parser.parse(br);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return threeHourWeatherData;
    }

    public static void dataDirectory_FiveDayThreeHour() {
        JSONArray list = (JSONArray) fiveDayThreeHourForecast().get("list");
        for (int i = 0; i < list.size(); i++) {
            forecastDataDirectory forecast = new forecastDataDirectory(list.get(i));
            forecasts.add(forecast);
        }
    }

    @SuppressWarnings("resource")
    public static String countryCodes(String country) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("ISO3166CountryCodes.txt"));
        country = country.toUpperCase();
        String checker = "";
        while ((checker = file.readLine()) != null) {
            if (country.equals(checker)) {
                break;
            }
        }
        String countryCode = file.readLine();
        if (countryCode != null) {
            return countryCode;
        } else {
            return null;
        }
    }

    public static boolean validState(String state) {
        HashMap<String, String> states = new HashMap<String, String>();
        states.put("alabama","AL");
        states.put("alaska","AK");
        states.put("arizona","AZ");
        states.put("arkansas","AR");
        states.put("california","CA");
        states.put("colorado","CO");
        states.put("connecticut","CT");
        states.put("delaware","DE");
        states.put("district Of Columbia","DC");
        states.put("florida","FL");
        states.put("georgia","GA");
        states.put("hawaii","HI");
        states.put("idaho","ID");
        states.put("illinois","IL");
        states.put("indiana","IN");
        states.put("iowa","IA");
        states.put("kansas","KS");
        states.put("kentucky","KY");
        states.put("louisiana","LA");
        states.put("maine","ME");
        states.put("maryland","MD");
        states.put("massachusetts","MA");
        states.put("michigan","MI");
        states.put("minnesota","MN");
        states.put("mississippi","MS");
        states.put("missouri","MO");
        states.put("montana","MT");
        states.put("nebraska","NE");
        states.put("nevada","NV");
        states.put("new Hampshire","NH");
        states.put("new Jersey","NJ");
        states.put("new Mexico","NM");
        states.put("new York","NY");
        states.put("north Carolina","NC");
        states.put("north Dakota","ND");
        states.put("ohio","OH");
        states.put("oklahoma","OK");
        states.put("oregon","OR");
        states.put("pennsylvania","PA");
        states.put("puerto Rico","PR");
        states.put("rhode Island","RI");
        states.put("south Carolina","SC");
        states.put("south Dakota","SD");
        states.put("tennessee","TN");
        states.put("texas","TX");
        states.put("utah","UT");
        states.put("vermont","VT");
        states.put("virgin Islands","VI");
        states.put("virginia","VA");
        states.put("washington","WA");
        states.put("west Virginia","WV");
        states.put("wisconsin","WI");
        states.put("wyoming","WY");
        return (states.containsValue(state) || states.containsKey(state));
    }

}
