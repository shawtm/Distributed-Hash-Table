package cs455.overlay.routing;

import cs455.overlay.tcp.TCPConnection;

public class RoutingEntry {
	private int id;
	private TCPConnection conn;
	
	public RoutingEntry(int id, TCPConnection conn){
		this.id = id;
		this.conn = conn;
	}
	public int getID(){
		return this.id;
	}
}
