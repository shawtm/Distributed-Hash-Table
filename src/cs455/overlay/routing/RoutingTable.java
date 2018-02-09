package cs455.overlay.routing;

import java.util.ArrayList;

import cs455.overlay.wireFormats.Event;

public class RoutingTable {
	private ArrayList<RoutingEntry> table;
	
	public RoutingTable(){
		this.table = new ArrayList<RoutingEntry>();
	}
	public void addEntry(RoutingEntry newEntry){
		table.add(newEntry);
	}
	public int[] getEntries(){
		return null;
	}
	public void sendData(Event event, int destination) {
		
	}
}
