package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import cs455.overlay.routing.RoutingTable;
import cs455.overlay.tcp.TCPConnection;
import cs455.overlay.util.MessagingParser;
import cs455.overlay.wireFormats.Event;
import cs455.overlay.wireFormats.OverlayNodeSendsRegistration;
import cs455.overlay.wireFormats.Protocol;

public class MessagingNode extends Node {
	private RoutingTable rt;
	
	
	public MessagingNode(String ip, int port){
		super();
		MessagingParser parser = new MessagingParser(this);
		register(ip, port);
		rt = new RoutingTable();
		parser.start();
		this.quit();
	}
	
	public void quit(){
		//deregister
		//close connections
		this.connections.interrupt();
		System.out.println("Exiting now");
	}
	public void printCounters() {
		//TODO
		return;
	}
	private void register(String ip, int port){
		System.out.println("Attempting to connect to Registry at " + ip + " on port " + port + "...");
		try {
			Socket s = new Socket(InetAddress.getLocalHost(), port);
			System.out.println("Connected Successfully");
			//Create Connection to store socket
			TCPConnection registryConn = new TCPConnection(s, this.events, TCPConnection.Type.MESSAGING_TO_REGISTRY);
			this.connections.setRegistryConn(registryConn);
			this.rt.addRegistryConn(registryConn);
			//create and send event to register with registry\
			int thisPort = this.connections.getPort();
			byte[] thisIP = this.connections.getIP();
			Event e = new OverlayNodeSendsRegistration(Protocol.OVERLAY_NODE_SENDS_REGISTRATION, thisIP.length, thisIP, thisPort);
			rt.sendDataToRegistry(e);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//first argument is the ip address of the registry and second is the port
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("incorrect number of arguments");
		}
		MessagingNode m = new MessagingNode(args[0], Integer.parseInt(args[1]));
	}
}
