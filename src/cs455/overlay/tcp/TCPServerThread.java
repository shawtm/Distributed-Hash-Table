package cs455.overlay.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TCPServerThread extends Thread {
	//wrapper class for the server socket of each messaging node and registry
	private ServerSocket server;
	private BlockingQueue<Socket> sockets;
	private Random r;
	private int port;
	
	public TCPServerThread() throws IOException{
		this.r = new Random();
		this.startSocket();
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
	
	public int getPort() {
		return this.port;
	}
	
	private void startSocket() {
		boolean success = false;
		while (!success) {
			this.port = this.generatePort();
			try {
				this.server = new ServerSocket(this.port);
				success = true;
			} catch (IOException e) { //port cannot be opened on that port
			}
		}
	}
	private int generatePort() {
		return (r.nextInt(64511) + 1024);
	}
}
