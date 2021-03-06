package cs455.overlay.routing;

import java.util.ArrayList;

import cs455.overlay.tcp.TCPConnection;

public class RegistryTable {
	private ArrayList<RegistryEntry> table;

	public RegistryTable() {
		this.table = new ArrayList<>();
	}
	
	public TCPConnection getConnection(int id) {
		for (RegistryEntry re: table) {
			if(re.getId() == id) {
				return re.getConnection();
			}
		}
		return null;
	}
	public RegistryEntry getEntry(int id) {
		for (RegistryEntry re: table) {
			if(re.getId() == id) {
				return re;
			}
		}
		return null;
	}
	public void registerNode(int id, int port, byte[] ipAddress, TCPConnection conn) {
		table.add(new RegistryEntry(id, port, ipAddress, conn));
	}
	
	public void deregisterNode(int id) {
		for(RegistryEntry re: table) {
			if(re.getId() == id) {
				table.remove(re);
				re.close();
				return;
			}
		}
	}
}
