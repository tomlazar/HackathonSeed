package com.ge.mvc.web.modules;

import java.io.IOException;

import org.json.JSONException;



/*
 * Service interface to be invoked by the controller
 * @author Kyle Duckworth (212326570)
 * @version 20.1.2017
 */
public interface SpringService {
	/*
	 * Get the authentication token from the UAA URL
	 * @param url - The URL to invoke the HTTP request to
	 * @return String - the authentication token
	 */
	public String getAuthToken(String url) throws IOException, JSONException;
	
	/*
	 * Get the data from the time series service
	 * @param URL - the URL to invoke the HTTP request to
	 * @return String - The stringified JSON object
	 */
	public String getData(String url) throws IOException, JSONException;


}

