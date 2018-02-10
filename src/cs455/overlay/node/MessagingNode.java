package cs455.overlay.node;

import cs455.overlay.routing.RoutingTable;
import cs455.overlay.util.MessagingParser;

public class MessagingNode extends Node {
	private RoutingTable rt;
	
	
	public MessagingNode(){
		super();
		MessagingParser parser = new MessagingParser(this);
		register();
		rt = new RoutingTable();
		parser.start();
		this.quit();
	}
	
	public void quit(){
		//deregister
		//close connections
	}
	
	private void register(){
		
	}
	
	//first argument is the ip address of the registry and second is the port
	public static void main(String[] args) {
		
	}
}
