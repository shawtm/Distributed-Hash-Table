package cs455.overlay.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPServerThread extends Thread {
	//wrapper class for the server socket of each messaging node and registry
	private ServerSocket server;
	private LinkedBlockingQueue<Socket> sockets;
	private Random r;
	private int port;
	
	public TCPServerThread() throws IOException{
		this.r = new Random();
		this.startSocket();
		sockets = new LinkedBlockingQueue<Socket>();
	}
	
	@Override
	public void run() {
		System.out.println("Starting server on port: "+ this.port);
		while (!interrupted()) {
			Socket newSocket;
			try {
				newSocket = server.accept();
				sockets.put(newSocket);
				System.out.println("connected to a node");
			} catch (IOException e) {
			} catch (InterruptedException e) {
			}
		}
		System.out.println("Server exiting...");
	}
	
	public Socket getSocket() throws InterruptedException {
		return sockets.take();
	}
	
	public int getPort() {
		return this.port;
	}
	
	private void startSocket() {
		boolean success = false;
		while (!success) {
			this.port = this.generatePort();
			try {
				this.server = new ServerSocket(this.port, 0, InetAddress.getLocalHost());
				success = true;
			} catch (IOException e) { //port cannot be opened on that port
			}
		}
	}
	private int generatePort() {
		return (r.nextInt(64511) + 1024);
	}

	public void shutdown() {
		try {
			server.close();
		} catch (IOException e) {
		}
	}
}
