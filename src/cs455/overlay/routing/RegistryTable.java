package cs455.overlay.routing;

import java.util.ArrayList;

public class RegistryTable {
	private ArrayList<RegistryEntry> table;

	public RegistryTable() {
		this.table = new ArrayList<>();
	}
	
	public void registerNode(int id, int port, byte[] ipAddress) {
		table.add(new RegistryEntry(id, port, ipAddress));
	}
	
	public void deregisterNode(int id) {
		//TODO
	}
}
