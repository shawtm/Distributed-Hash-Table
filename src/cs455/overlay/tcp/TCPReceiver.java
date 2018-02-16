package cs455.overlay.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.wireFormats.Event;
import cs455.overlay.wireFormats.EventFactory;
import cs455.overlay.wireFormats.OverlayNodeSendsRegistration;
import cs455.overlay.wireFormats.Protocol;

public class TCPReceiver extends Thread {
	//Wrapper class for the receiving socket for each messaging
	private Socket socket;
	private DataInputStream din;
	private LinkedBlockingQueue<Event> events;
	private int port = -1;
	private byte[] ip = null;
	private boolean exit = false;

	public TCPReceiver(Socket socket, LinkedBlockingQueue<Event> events) throws IOException {
		this.socket = socket;
		din = new DataInputStream(socket.getInputStream());
		this.events = events;
	}
	
	public void run() {
		int dataLength;
		while (socket != null && !interrupted() && !exit) {//socket != null && 
			try {
				dataLength = din.readInt();
				//System.out.println("[Receiver] Size is " + dataLength);
				byte[] data = new byte[dataLength];
				din.readFully(data);
				try {
					Event event = EventFactory.getEvent(data);
					//System.out.println("[Receiver] type is " + event.getType());
					if(event.getType() == Protocol.OVERLAY_NODE_SENDS_REGISTRATION) {
						OverlayNodeSendsRegistration on = new OverlayNodeSendsRegistration(event.getBytes());
						this.port = on.getPort();
						this.ip = on.getIP();
					}
					events.put(event);
				} catch (InterruptedException e) {
					exit = true;
				}
			} catch (SocketException se) {
				//exit = true;
				//System.out.println(se.getMessage());
			} catch (IOException ioe) {
				//exit = true;
				//System.out.println(ioe.getMessage()) ;
			}
		}
		System.out.println("[Receiver] Receiver is exiting!");
	}
	public byte[] getIP() {
		return this.ip;
	}
	public int getPort() {
		return this.port;
	}
	public void close() {
		try {
			din.close();
			if (!socket.isClosed()) {
				socket.close();
			}
			exit = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			exit = true;
			e.printStackTrace();
		}
	}
}
