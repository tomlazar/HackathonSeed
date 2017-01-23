
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
 
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
 /*
  * Class to read an incoming data file from the pi 0 on the train
  * @author 212326570
  * @version 08.12.16
  */
public class readFile {
	// Hard code folder path name on the pi, I used my local one to test
	public final static String pathName = "C:\\Users\\212326570.LOGON\\Desktop\\data";
	public static List<String> records = new ArrayList<String>();
	
	/*
	 * Main executable to watch for changes
	 */
	public static void main(String[] args) throws Exception {
		// Folder we are going to watch
		Directory myDirectory = new Directory(pathName);
		// Authentication object used to get token
		Auth tokenConnector = new Auth();
		// Web socket object used to push data
		WebSocket mySocket = new WebSocket();
		while(true){
			// Read the file that was created in the path to get the payload
			readFile(pathName + "\\" + myDirectory.watchDirectoryPath());
			tokenConnector.getToken("https://df1e4f24-d699-4fef-a783-916e727ea2a5.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token");
			// connect to the Predix WSS
			mySocket.connectToPredix();
			mySocket.pushDataToPredix(records, tokenConnector.getToken());
		}

       
	}
	
	/*
	 * Read the newly created file
	 * @param filename - the name of the file that was created
	 */
	private static List<String> readFile(String filename)
	{
	  // A list of the contens of the file
	  List<String> records = new ArrayList<String>();
	  try
	  {
		// Read the file
	    BufferedReader reader = new BufferedReader(new FileReader(filename));
	    String line;
	    // Loop through file content
	    while ((line = reader.readLine()) != null)
	    {
	      // Add contants to our list - will use this as our payload (TODO)
	      records.add(line);
	      // Debug to make sure we read the file
	      System.out.println(line);
	    }
	    // Close the BR
	    reader.close();
	    return records;
	  }
	  catch (Exception e)
	  {
	    System.err.format("Exception occurred trying to read '%s'.", filename);
	    e.printStackTrace();
	    return null;
	  }
	}

				
}