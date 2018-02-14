package cs455.overlay.routing;

import java.util.ArrayList;

import cs455.overlay.tcp.TCPConnection;
import cs455.overlay.wireFormats.Event;

public class RoutingTable {
	private ArrayList<RoutingEntry> table;
	private RoutingEntry registry;

	public RoutingTable(){
		this.table = new ArrayList<RoutingEntry>();
		this.registry = null;
	}
	public void addEntry(RoutingEntry newEntry){
		table.add(newEntry);
	}
	public int[] getEntries(){
		return null;
	}
	public void sendData(Event event, int destination) {
		for(RoutingEntry re: table) {
			if (re.getID() == destination) {
				re.getConn().sendData(event);
				return;
			}
		}
		//hasnt sent packet yet if here
		int closest = -1;
		for (int i = 0; i < table.size(); i++) {
			if(table.get(i).getID() <= destination)
				closest = i;
		}
		this.table.get(closest).getConn().sendData(event);
		return;
	}
	public void sendDataToRegistry(Event event) {
		this.registry.getConn().sendData(event);
	}
	public void addRegistryConn(TCPConnection re) {
		this.registry = new RoutingEntry(-1, re);
	}
}
