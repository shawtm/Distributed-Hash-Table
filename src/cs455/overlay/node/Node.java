package cs455.overlay.node;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cs455.overlay.tcp.TCPConnectionsCache;
import cs455.overlay.tcp.TCPServerThread;
import cs455.overlay.util.InteractiveCommandParser;
import cs455.overlay.wireFormats.Event;

public class Node {
	//parser
	protected InteractiveCommandParser parser;
	//server
	protected TCPServerThread server;
	//connections cache
	protected TCPConnectionsCache connections;
	//queue of events
	protected BlockingQueue<Event> events;
	
	public Node(){
		try {
			this.server = new TCPServerThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.events = new ArrayBlockingQueue<Event>(1024);
		this.connections = new TCPConnectionsCache(this.server, this.events);
	}
}
