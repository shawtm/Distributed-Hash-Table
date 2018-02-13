package cs455.overlay.node;

import java.io.IOException;
import java.util.LinkedList;

import cs455.overlay.tcp.TCPConnectionsCache;
import cs455.overlay.tcp.TCPServerThread;
import cs455.overlay.wireFormats.Event;

public class Node {
	//server
	protected TCPServerThread server;
	//connections cache
	protected TCPConnectionsCache connections;
	//queue of events
	protected LinkedList<Event> events;
	
	public Node(){
		try {
			this.server = new TCPServerThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.events = new LinkedList<Event>();
		this.connections = new TCPConnectionsCache(this.server, this.events);
	}
}
