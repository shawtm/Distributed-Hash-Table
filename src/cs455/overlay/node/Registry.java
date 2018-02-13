package cs455.overlay.node;

import java.util.ArrayList;
import java.util.Random;

import cs455.overlay.routing.RegistryTable;
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.tcp.TCPConnection;
import cs455.overlay.util.RegistryParser;

public class Registry extends Node {
	private ArrayList<RoutingTable> rts;
	private ArrayList<Integer> registeredIDs;
	private RegistryTable table;
	private Random rand;
	//private boolean run = true;
	
	public Registry(){
		super();
		this.table = new RegistryTable();
		RegistryParser parser = new RegistryParser(this);
		parser.start();
		this.connections.start();
		//this.registeredIDs = new ArrayList<>();
		//this.rand = new Random();
		//this.rts = new ArrayList<>();
		//run();
	}
	public void run() {
		System.out.println("Exiting now");
	}
	public void register(int port, byte[] ipAddress, TCPConnection conn){
		int id = addID();
		//Register Node
		table.registerNode(id, port, ipAddress, conn);
		// TODO send back id
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
		//this.run = false;
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
	}
}
