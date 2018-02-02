package cs455.overlay.node;

import cs455.overlay.routing.RoutingTable;

public class MessagingNode extends Node {
	private RoutingTable rt;
	
	public MessagingNode(){
		super();
		register();
		rt = new RoutingTable();
	}
	
	public void quit(){
		//deregister
		//close connections
	}
	
	private void register(){
		
	}
}
