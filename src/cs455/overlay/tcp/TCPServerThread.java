package cs455.overlay.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TCPServerThread extends Thread {
	//wrapper class for the server socket of each messaging node and registry
	private ServerSocket server;
	private BlockingQueue<Socket> sockets;
	
	public TCPServerThread() throws IOException{
		this.server = new ServerSocket();
		sockets = new ArrayBlockingQueue<Socket>(10);
	}
	
	@Override
	public void run() {
		while (!interrupted()) {
			try {
				Socket newSocket = server.accept();
				sockets.put(newSocket);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Socket getSocket() {
		try {
			return sockets.take();
		} catch (InterruptedException e) {
			return null;
		}
	}
}
