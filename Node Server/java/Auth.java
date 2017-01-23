import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Auth {
	private String token;
	public Auth(){
		this.token = null;
	}
	
	public void getToken(String url) throws IOException{
		// Display the UAA URL
		System.out.println(url);
		url += "?client_id=tsWebApp&grant_type=client_credentials";
		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					
		// POST command to UAA to get the token
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Accept", "application/json, application/x-www-form-urlencoded");
		con.setRequestProperty("Authorization", "Basic dHNXZWJBcHA6cGFzc3dvcmQ=");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					
		// Make sure it was a success
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		// Create a buffered reader to read the http response body
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
					
		// We're going to look for the phrase "access_token":"
		// This will tell us where the token exsits in the body
		// Instead of this we couldve mapped the entire response to objects using a JSON library
		String searchPhrase = "\"access_token\":\"";
		// variable to hold the access token
		String access_token = "";
		// Read until end of file is reached
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
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
		in.close();
		// Return the token to the method caller
		token = access_token;
	}
	
	public String getToken(){
		return token;
	}
}

