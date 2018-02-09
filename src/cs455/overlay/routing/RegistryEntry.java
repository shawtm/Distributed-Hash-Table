package cs455.overlay.routing;

public class RegistryEntry {
	private int id;
	private int port;
	private byte[] ipAddress;
	
	public RegistryEntry(int id, int port, byte[] ipAddress) {
		this.id = id;
		this.port = port;
		this.ipAddress = ipAddress;
	}
	
	public int getId() {
		return id;
	}

	public int getPort() {
		return port;
	}

	public byte[] getIpAddress() {
		return ipAddress;
	}
	
}
