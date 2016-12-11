package main.ClientServerCommunication;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** ServerWorkerRunnable - Handles the server connection between and one client.
 * Each thread will run a server worker and will process the clients requests/
 * @author Aviad Cohen
 * @since 2016-12-10*/

public class ServerWorkerRunnable implements Runnable{

	public static final Logger LOGGER = Logger.getLogger(ServerWorkerRunnable.class.getName());
	
    private Socket clientSocket;
    private ProcessRequest processRequest;

    /**
     * Create a ServerWorkerRunnable.
     * @param clientSocket - the socket which the client is connected to.
     * @param processRequest - the object which processes the clients requests (also send
	 * responds and receive requests.
     */
    public ServerWorkerRunnable(Socket clientSocket, ProcessRequest processRequest) {
    	if (clientSocket == null || processRequest == null)
			throw new NullPointerException();
    	
        this.clientSocket = clientSocket;
        this.processRequest = processRequest;
    }

    /**
     * The main function which will always run when the server is processing the client requests.
     */
    public void run() {
        LOGGER.log(Level.FINE, "Server started to process request from port " + clientSocket.getPort());
		
		processRequest.process(clientSocket);
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Server failed to close client socket", e);
		}
		
		LOGGER.log(Level.FINE, "Server process request from port " + clientSocket.getPort() + " finished");
    }
}
