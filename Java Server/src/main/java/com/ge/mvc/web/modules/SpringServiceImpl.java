package com.ge.mvc.web.modules;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/*
 * Implementation of the serivce interface to create the connection to Predix
 * @author Kyle Duckworth (212326570)
 * @version 20.1.2017
 */
@Service(value = "SpringService")
public class SpringServiceImpl implements SpringService {
	// variable to hold the access token
	private String accessToken = "";
	/*
	 * Get the access token for time series connection
	 * @return the user authentication token
	 */
	private String getAccessToken(){
		// Return the auth token
		return accessToken;
	}
	
	/*
	 * Get the authentication token from the UAA service
	 * @param URL - the UAA URL to invoke the HTTP post request to
	 * @return - The user authentication token
	 */
	public String getAuthToken(String url) throws IOException, JSONException {
		// Reset the token if method has been previously invoked
		accessToken = "";

		// Specify we are using client credentials 
		url += "?grant_type=client_credentials";
		// Step 2: Send request to AWS proxy
		// Instantiate a URL object to make the POST request

		// Instantiate the rest template
		RestTemplate restTemplate = new RestTemplate();
		
		// Instantiate the headers object to house the request headers
		HttpHeaders headers = new HttpHeaders();
		
		// Add the request headers
		headers.set("Authorization", "Basic YXBwX2NsaWVudF9pZDpzZWNyZXQ=");
		headers.set("Accept", "application/json, application/x-www-form-urlencoded");
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		
		// Create the HTTP entity
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
		// OPTION 1: Use JSON Object to read response body
		JSONObject test = new JSONObject(response.getBody());
		accessToken = test.getString("access_token");

		// Return the token to the method caller
		return accessToken;
		

	}

	/*
	 * Get data from the Predix time series store
	 * @param URL - the time series URL to query
	 * @return - Strigified JSON object of the time series table's contents
	 */
	public String getData(String url) throws IOException, JSONException {

		// Instantiate the rest template
		RestTemplate restTemplate = new RestTemplate();
				
		// Instantiate the headers object to house the request headers
		HttpHeaders headers = new HttpHeaders();
		
		//add request headers
		headers.set("predix-zone-id", "e3fba85e-d334-409e-87ce-3a17e71b4946");
		headers.set("Authorization", "Bearer " + this.getAccessToken());
		headers.set("Content-Type", "application/json");

		// JSONObject for the outer tags
		JSONObject jsonBody = new JSONObject();
		// JSONObject for the tags
		JSONObject tags=new JSONObject();
		JSONArray parent=new JSONArray();
		// Name of table to query
		tags.put("name", "Engine Speed");
		// Order for sorted data
		tags.put("order", "desc");
		// Limit tag for limited data points
		tags.put("limit", "2");
		// Add the tags to an array (needed for query)
		parent.put(tags);
		// Add array to JSON Body
		jsonBody.put("tags", parent);
		// Add start date
		jsonBody.put("start","1y-ago");
				
		// Create the HTTP entity
		HttpEntity<String> entity = new HttpEntity<String>(jsonBody.toString(), headers);

		// Send the post request and record the response
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	
		// Return the response body to the controller
		return response.getBody();
	}

	

}
