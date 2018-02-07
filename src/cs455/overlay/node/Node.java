package cs455.overlay.node;

import java.io.IOException;
import java.util.ArrayList;

import cs455.overlay.tcp.*;
import cs455.overlay.util.InteractiveCommandParser;

public class Node {
	//parser
	InteractiveCommandParser parser;
	//server
	TCPServerThread server;
	//connections cache
	TCPConnectionsCache connections;
	
	public Node(){
		try {
			this.server = new TCPServerThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connections = new TCPConnectionsCache(this.server);
	}
}
