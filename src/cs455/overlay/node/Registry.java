package cs455.overlay.node;

import java.util.ArrayList;
import java.util.Random;

import cs455.overlay.routing.RegistryTable;
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.tcp.TCPConnection;
import cs455.overlay.tcp.TCPConnectionsCache;
import cs455.overlay.util.RegistryParser;
import cs455.overlay.wireFormats.Event;
import cs455.overlay.wireFormats.OverlayNodeSendsRegistration;
import cs455.overlay.wireFormats.Protocol;
import cs455.overlay.wireFormats.RegistryReportsRegistrationStatus;

public class Registry extends Node {
	private ArrayList<RoutingTable> rts;
	private ArrayList<Integer> registeredIDs;
	private RegistryTable table;
	private Random rand;
	private boolean run = true;
	
	public Registry(){
		super();
		this.connections = new TCPConnectionsCache(this.server, this.events, TCPConnectionsCache.Type.REGISTRY);
		this.table = new RegistryTable();
		RegistryParser parser = new RegistryParser(this);
		parser.start();
		this.connections.start();
		this.registeredIDs = new ArrayList<>();
		this.rand = new Random();
		this.rts = new ArrayList<>();
	}
	public void run() {
		Event ev;
		while(run) {
			try {
				ev = this.events.take();
				switch (ev.getType()) {
				case Protocol.NODE_REPORTS_OVERLAY_SETUP_STATUS:
					OverlayNodeSendsRegistration on = new OverlayNodeSendsRegistration(ev.getBytes());
					this.register(on.getPort(), on.getIP());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Exiting now");
	}
	
	public void register(int port, byte[] ipAddress){
		int id = addID();
		//find connection
		TCPConnection conn = null;
		ArrayList<TCPConnection> coons = this.connections.getNewConns();
		for (TCPConnection con: coons) {
			if (con.getPort() == port) {
				byte[] bytes = con.getIP();
				boolean toggle = true;
				try {
				for(int i = 0; i < ipAddress.length; i++) {
					if (ipAddress[i] != bytes[i])
						toggle = false;
				}
				}catch(ArrayIndexOutOfBoundsException e) {
					toggle = false;
				}
				if(toggle) {
					conn = con;
					break;
				}
			}
		}
		//Register Node
		table.registerNode(id, port, ipAddress, conn);
		// TODO send back id
		RegistryReportsRegistrationStatus re = new RegistryReportsRegistrationStatus
				(Protocol.REGISTRY_REPORTS_REGISTRATION_STATUS, id, ("Registration Successful! " + this.registeredIDs.size() + " nodes registered!"));
		conn.sendData(re);
	}
	
	public void degregister(int id){
		//remove id form registeredIDs
		table.deregisterNode(id);
	}
	public void printRoutingTables() {
		System.out.println(this.rts);
	}
	public void shutdown() {
		System.out.println("Shutting Down...");
		this.connections.interrupt();
		this.run = false;
	}
	public void printNodes() {
		// TODO Auto-generated method stub
		
	}

	public void setupOverlay(int size) {
		// TODO Auto-generated method stub
		
	}

	public void start(int rounds) {
		// TODO Auto-generated method stub
		
	}
	private int addID(){
		int id = getID();
		registeredIDs.add(id);
		return id;
	}
	
	private int getID(){
		int newID = rand.nextInt(128);
		while (registeredIDs.contains(newID))
			newID = rand.nextInt(128);
		return newID;
	}
	public static void main(String[] args) {
		Registry r = new Registry();
		r.run();
	}
}
