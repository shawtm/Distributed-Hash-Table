package cs455.overlay.node;

import java.util.ArrayList;
import java.util.Random;

import cs455.overlay.routing.RegistryTable;
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.util.RegistryParser;

public class Registry extends Node {
	private ArrayList<RoutingTable> rts;
	private ArrayList<Integer> registeredIDs;
	private RegistryTable table;
	private Random rand;
	
	public Registry(){
		super();
		this.table = new RegistryTable();
		this.parser = new RegistryParser();
		this.registeredIDs = new ArrayList<>();
		this.rand = new Random();
		this.rts = new ArrayList<>();
	}
	
	public void register(int port, byte[] ipAddress){
		int id = addID();
		table.registerNode(id, port, ipAddress);
		//send back id
	}
	
	public void degregister(int id){
		//remove id form registeredIDs
		table.deregisterNode(id);
		//TODO close connection
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
	public void printRoutingTables() {
		System.out.println(this.rts);
	}
}
