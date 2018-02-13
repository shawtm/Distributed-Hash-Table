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

	}
	public void sendDataToRegistry(Event event) {
		this.registry.getConn().sendData(event);
	}
	public void addRegistryConn(TCPConnection re) {
		this.registry = new RoutingEntry(-1, re);
	}
}
