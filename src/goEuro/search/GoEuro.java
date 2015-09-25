package goEuro.search;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class GoEuro {
	
	public static final String COMMA_SEPARATOR=",";
	public static final String NEW_LINE="\n";
	String city="";
	String url="http://api.goeuro.com/api/v2/position/suggest/en/";
	URL apiURL;
	HttpURLConnection urlConnection;
	JSONParser parser;
	JSONArray searchResultJsonArray;
	JSONObject jsonObject;
	Scanner in;
	JSONObject geoPositonJsonObject;
	
	public GoEuro() {
		
		in= new Scanner(System.in);
		parser = new JSONParser();
	}
	public void searchConnection() throws IOException, ParseException{
				
		city=in.nextLine();
		apiURL=new URL(url+city);
		
		urlConnection= (HttpURLConnection) apiURL.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("Content-Type","application/json");
		BufferedReader responseReader= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		FileWriter csvFileWriter= new FileWriter("src/searchResult/searchResult.csv");
		StringBuilder stringbuilder=new StringBuilder();
		String searchResultString="";
		while((searchResultString=responseReader.readLine())!=null)
		{			
			stringbuilder.append(searchResultString);
		}
		searchResultString=stringbuilder.toString();
		
		searchResultJsonArray=(JSONArray) parser.parse(searchResultString);
		for(int i=0;i<searchResultJsonArray.size();i++){
			jsonObject= (JSONObject) searchResultJsonArray.get(i);
			csvFileWriter.append(jsonObject.get("_id").toString());
			csvFileWriter.append(COMMA_SEPARATOR);
			csvFileWriter.append(jsonObject.get("name").toString());
			csvFileWriter.append(COMMA_SEPARATOR);
			csvFileWriter.append(jsonObject.get("type").toString());
			csvFileWriter.append(COMMA_SEPARATOR);
					    
			geoPositonJsonObject= (JSONObject) jsonObject.get("geo_position");
			csvFileWriter.append(geoPositonJsonObject.get("latitude").toString());
			csvFileWriter.append(COMMA_SEPARATOR);
			csvFileWriter.append(geoPositonJsonObject.get("longitude").toString());
			csvFileWriter.append(NEW_LINE);
		}

		csvFileWriter.flush();
		csvFileWriter.close();
		
	}

	
	public static void main(String[] args) throws IOException, ParseException {
		
		
		GoEuro goEuro= new GoEuro();
		goEuro.searchConnection();
		
		}
			
	}


