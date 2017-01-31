package com.johnathanmarksmith.mvc.web.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

/*
 * Implementation of the serivce interface to create the connection to Predix
 * @author Kyle Duckworth (212326570)
 * @version 20.1.2017
 */
@Service(value = "SpringService")
public class SpringServiceImpl implements SpringService {
	// variable to hold the access token
	private String access_token = "";
	
	/*
	 * Get the access token for time series connection
	 * @return the user authentication token
	 */
	private String getAccessToken(){
		// Return the auth token
		return access_token;
	}
	
	/*
	 * Get the authentication token from the UAA service
	 * @param URL - the UAA URL to invoke the HTTP post request to
	 * @return - The user authentication token
	 */
	public String getAuthToken(String url) throws IOException {
		// Reset the token if method has been previously invoked
		access_token = "";
		// Add the UAA username as a parameter 'client_id'
		url += "?grant_type=client_credentials";
		// Create a URL Object from the concatenated UAA URL
		URL obj = new URL(url);
		// Create the connection object for the URL
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
							
		// POST command to UAA to get the token
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Accept", "application/json, application/x-www-form-urlencoded");
		con.setRequestProperty("Authorization", "Basic YXBwX2NsaWVudF9pZDpzZWNyZXQ=");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							
		// Make sure it was a success
		int responseCode = con.getResponseCode();
				
		// Create a buffered reader to read the http response body
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		// Variable will be used to parse response
		String inputLine;
			
		// We're going to look for the phrase "access_token":"
		// This will tell us where the token exsits in the body
		// Instead of this we couldve mapped the entire response to objects using a JSON library
		String searchPhrase = "\"access_token\":\"";
				
		// Read until end of file is reached
		while ((inputLine = in.readLine()) != null) {
			// Loop through response starting after the phrase "access_token":"
			for(int i = inputLine.indexOf(searchPhrase)+searchPhrase.length(); i < inputLine.length(); i++){
				// Once we reach another quote, we know the token has been read
				if(inputLine.substring(i,i+1).equals("\"")){
					// Break the loop
					break;
				}
				// Add characters to the token one by one as we loop through them
				access_token += inputLine.substring(i,i+1);
			}
		}
		// Close the buffered reader
		in.close();
		// Close the connection object
		con.disconnect();
		// Return the token to the method caller
		return access_token;
	}

	/*
	 * Get data from the Predix time series store
	 * @param URL - the time series URL to query
	 * @return - Strigified JSON object of the time series table's contents
	 */
	public String getData(String url) throws IOException {
		// Create a URL object for the incoming time series URL
		URL obj = new URL(url);
		// Create a connection object for the URL
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//add request headers
		con.setRequestProperty("predix-zone-id", "e3fba85e-d334-409e-87ce-3a17e71b4946");
		con.setRequestProperty("Authorization", "Bearer " + this.getAccessToken());
		con.setRequestProperty("Content-Type", "application/json");
		// Set input and output to true to allow reading of response
		con.setDoOutput(true);
		con.setDoInput(true);
		// POST command to UAA to get the time series data
		con.setRequestMethod("POST");
		
		// Create an OutputStreamWriter for our connect to post a message body
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		// The message body to be posted
		String message = "{\"start\": \"1y-ago\", \"tags\": [ { \"name\": \"Engine Speed\",\"order\": \"desc\", \"limit\": 100} ]}";
		// Write the message to the output stream
		wr.write(message);
		// Flush the buffer
		wr.flush();
	
		// Create a string builder object to parse the response
		StringBuilder sb = new StringBuilder();  
		// Get the connection response
		int HttpResult = con.getResponseCode(); 
		// Check for a 200 response (success)
		if (HttpResult == HttpURLConnection.HTTP_OK) {
			// Read the response
		    BufferedReader br = new BufferedReader(
		            new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    // Loop through entire repsonse
		    while ((line = br.readLine()) != null) {  
		    	// Append the string builder object with the response
		        sb.append(line + "\n");  
		    }
		    // Close the buffered reader
		    br.close();
		    // Display contents for debuggins
		    System.out.println("" + sb.toString());  
		} else {
			// Display error message for debugging
		    System.out.println("HttpResult: " + HttpResult + " " + con.getResponseMessage());  
		}  
		// Close the output writer
		wr.close();
		// Close the connection object
		con.disconnect();
		// Return the strigified JSON Object
		return sb.toString();
	}

		
	

}
