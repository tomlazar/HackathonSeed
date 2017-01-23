import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;

/*
 * Helper class for a directory
 */
public class Directory {
	private static String location;
	
	public Directory(){
		this.location = null;
	}
	
	public Directory(String location){
		this.location = location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public static String getLocation(){
		return location;
	}
	// Watch the directory for a new file
	public Path watchDirectoryPath() throws Exception {
		Path path = Paths.get(getLocation());
			// Sanity check - Check if path is a folder
			try {
				Boolean isFolder = (Boolean) Files.getAttribute(path,
						"basic:isDirectory", NOFOLLOW_LINKS);
				if (!isFolder) {
					throw new IllegalArgumentException("Path: " + path + " is not a folder");
				}
			} catch (IOException ioe) {
				// Folder does not exists
				ioe.printStackTrace();
			}
			// Display what path we're waiting for an event to be triggered
			System.out.println("Watching path: " + path);
			
			// We obtain the file system of the Path
			FileSystem fs = path.getFileSystem ();
			
			// We create the new WatchService using the new try() block
			try(WatchService service = fs.newWatchService()) {
				
				// We register the path to the service
				// We watch for creation events
				path.register(service, ENTRY_CREATE);
				
				// Start the infinite polling loop
				WatchKey key = null;
				while(true) {
					key = service.take();
					
					// Dequeueing events
					Kind<?> kind = null;
					for(WatchEvent<?> watchEvent : key.pollEvents()) {
						// Get the type of the event
						kind = watchEvent.kind();
						if (OVERFLOW == kind) {
							continue; //loop
						} else if (ENTRY_CREATE == kind) {
							// A new Path was created 
							Path newPath = ((WatchEvent<Path>) watchEvent).context();
							// Output
							System.out.println("New path created: " + newPath);
							return newPath;

						}
					}
					
					if(!key.reset()) {
						break; //loop
					}
				}
				
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			return path;
			
		}
}
