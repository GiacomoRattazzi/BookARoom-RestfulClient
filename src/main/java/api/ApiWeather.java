package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ApiWeather {
    public static void main(String[] args) {
    try {
        String urlString = "https://api.open-meteo.com/v1/forecast?"
                + "latitude=40.71&longitude=-74.01"
                + "&daily=temperature_2m_max,temperature_2m_min,sunrise,"
                + "sunset&timezone=America%2FNew_York&start_date=2022-12-18&end_date=2022-12-22";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        System.out.println(status);
        System.out.println(connection.getContentType());
        

        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        
        System.out.println(content);
        
        JSONParser parse = new JSONParser();
        JSONObject dataObject = (JSONObject) parse.parse(String.valueOf(content));
        JSONObject data = (JSONObject) dataObject.get("daily");
        JSONArray dataTime = (JSONArray) data.get("time");
        JSONArray dataTempMax = (JSONArray) data.get("temperature_2m_max");
        JSONArray dataTempMin = (JSONArray) data.get("temperature_2m_min");
        
        System.out.println("TEST   Latitude        " + dataObject.get("latitude"));
        System.out.println("TEST   all daily          " + dataObject.get("daily"));
        System.out.println("All Time   " + data.get("time"));
        System.out.println("Temperature Max"+ data.get("temperature_2m_max"));
        System.out.println("Time day 1"+ dataTime.get(0));
        System.out.println("--------------------------------------");
        System.out.println("Zone     " + dataObject.get("timezone"));       
        System.out.println("Time day 1 : "+ dataTime.get(0));
        System.out.println("Temperature Max day 1 : "+dataTempMax.get(0)+" C°");
        System.out.println("Temperature Min day 1 : "+dataTempMin.get(0)+" C°");
        System.out.println("Time day 2 : "+ dataTime.get(1));
        System.out.println("Temperature Max day 2 : "+dataTempMax.get(1)+" C°");
        System.out.println("Temperature Min day 2 : "+dataTempMin.get(1)+" C°");
        
        
    } catch (Exception ex) {
      System.out.print("exception:" + ex.getMessage());
    }
    
    
  }
}
