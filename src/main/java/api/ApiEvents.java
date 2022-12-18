package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ApiEvents {

  public static void main(String[] args) {
    try {
        String urlString = "https://partner-api.nyc.gov/calendar/search?startDate=01%2F01%2F2022%2012:00%20AM&endDate=01%2F07%2F2022%2012:00%20AM&sort=DATE";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        //Request headers
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", "d0da99a707ed4903a5e94f402dd63f6b");
        
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        System.out.println(status);
        System.out.println(connection.getContentType());
        
        Map<String, List<String> > header = connection.getHeaderFields();
//        Map<String, List<String>> co = connection.getRequestProperties();
  
            // Printing all the fields along with their
            // value
        for (Map.Entry<String, List<String> > mp : header.entrySet()) {
            System.out.print(mp.getKey() + " : ");
            System.out.println(
            mp.getValue().toString());
        }
        

        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        System.out.println("---------------------------------");
        System.out.println(connection.getContent());
        System.out.println("---------------------------------");
        System.out.println("---------------------------------");
        System.out.println(connection.getHeaderFields());
        System.out.println("---------------------------------");
        System.out.println("---------------------------------");
        System.out.println("---------------------------------");
        
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        
        System.out.println(content);
        
        JSONParser parse = new JSONParser();
        JSONObject dataObject = (JSONObject) parse.parse(String.valueOf(content));
        JSONArray items = (JSONArray) dataObject.get("items");
        JSONObject item1 = (JSONObject) items.get(1);
//        JSONArray nameI1 = (JSONArray) item1.get(0);
//        
////        
//        JSONArray dataTime = (JSONArray) data.get("time");
//        JSONArray dataTempMax = (JSONArray) data.get("temperature_2m_max");
        


        System.out.println("TEST             " + dataObject);
        System.out.println("TEST             " + dataObject.get("items"));
        System.out.println("TEST             " + items.get(1));
        System.out.println("---------------------------------");
        System.out.println("---------------------------------");
        System.out.println("---------------------------------");
        System.out.println("The Name of the event: " + item1.get("name"));
        System.out.println("Starts : " + item1.get("startDate")+ " Ends : "+item1.get("endDate"));
        System.out.println("Day : " + item1.get("datePart"));
        System.out.println("From " + item1.get("timePart"));
        System.out.println("The address : " + item1.get("address"));
        System.out.println("The description : " + item1.get("shortDesc"));
        System.out.println("The link : " + item1.get("permalink"));
        System.out.println("The urlImage : " + item1.get("imageUrl"));
        
        
    } catch (Exception ex) {
      System.out.print("exception:" + ex.getMessage());
    }
  }
}
