package cs455.overlay.tcp;

import java.util.ArrayList;

public class TCPConnectionsCache implements Runnable {
	protected ArrayList<TCPConnection> connections;
	protected TCPServerThread server;
	//blocking queue?
	public TCPConnectionsCache(TCPServerThread server){
		this.server = server;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// poll server for receiver
	}
}
