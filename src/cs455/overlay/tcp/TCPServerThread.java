package cs455.overlay.tcp;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServerThread {
	//wrapper class for the server socket of each messaging node and registry
	private ServerSocket serve;
	
	public TCPServerThread() throws IOException{
		this.serve = new ServerSocket();
	}
}
