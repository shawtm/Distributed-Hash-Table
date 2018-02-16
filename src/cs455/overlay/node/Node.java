package cs455.overlay.node;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.tcp.TCPConnectionsCache;
import cs455.overlay.tcp.TCPServerThread;
import cs455.overlay.wireFormats.Event;

public class Node extends Thread {
	//server
	protected TCPServerThread server;
	//connections cache
	protected TCPConnectionsCache connections;
	//queue of events
	protected LinkedBlockingQueue<Event> events;
	
	public Node(){
		try {
			this.server = new TCPServerThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.events = new LinkedBlockingQueue<Event>();
	}
	public Node(int port){
		this.server = new TCPServerThread(port);
		this.events = new LinkedBlockingQueue<Event>();
	}
}
