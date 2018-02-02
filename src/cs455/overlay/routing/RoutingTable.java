package cs455.overlay.routing;

import java.util.ArrayList;

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
}
