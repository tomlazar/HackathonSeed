import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class WebSocket {
	public void connectToPredix(){
		 String destUri = "ws://echo.websocket.org";
	
	     WebSocketClient client = new WebSocketClient();
	     SimpleEchoSocket socket = new SimpleEchoSocket();
	     try
	     {
	         client.start();
	
	         URI echoUri = new URI(destUri);
	         ClientUpgradeRequest request = new ClientUpgradeRequest();
	         client.connect(socket,echoUri,request);
	         System.out.printf("Connecting to : %s%n",echoUri);
	
	         // wait for closed socket connection.
	         socket.awaitClose(5,TimeUnit.SECONDS);
	     }
	     catch (Throwable t)
	     {
	         t.printStackTrace();
	     }
	     finally
	     {
	         try
	         {
	             client.stop();
	         }
	         catch (Exception e)
	         {
	             e.printStackTrace();
	         }
	     }
    }
	public void pushDataToPredix(List<String> data, String token){
		System.out.println("Pushing data to Predix");
		System.out.println("Using token:" + token);
	}
     
}
