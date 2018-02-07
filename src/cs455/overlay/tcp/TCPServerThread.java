package cs455.overlay.tcp;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServerThread extends Thread {
	//wrapper class for the server socket of each messaging node and registry
	private ServerSocket server;
	
	public TCPServerThread() throws IOException{
		this.server = new ServerSocket();
	}
	
	@Override
	public void run() {
		while (!interrupted()) {
			try {
				server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
