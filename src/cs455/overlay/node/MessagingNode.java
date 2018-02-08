package cs455.overlay.node;

import cs455.overlay.routing.RoutingTable;
import cs455.overlay.util.MessagingParser;

public class MessagingNode extends Node {
	private RoutingTable rt;
	
	public MessagingNode(){
		super();
		this.parser = new MessagingParser();
		register();
		rt = new RoutingTable();
		this.parser.start();
		this.quit();
	}
	
	public void quit(){
		//deregister
		//close connections
	}
	
	private void register(){
		
	}
}
