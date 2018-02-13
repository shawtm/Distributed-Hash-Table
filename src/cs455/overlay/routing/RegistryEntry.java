package cs455.overlay.routing;

import cs455.overlay.tcp.TCPConnection;

public class RegistryEntry {
	private int id;
	private int port;
	private byte[] ipAddress;
	private TCPConnection conn;
	
	public RegistryEntry(int id, int port, byte[] ipAddress, TCPConnection conn) {
		this.id = id;
		this.port = port;
		this.ipAddress = ipAddress;
		this.conn = conn;
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
	
	public void close() {
		conn.close();
	}
	public TCPConnection getConnection() {
		return this.conn;
	}
}
