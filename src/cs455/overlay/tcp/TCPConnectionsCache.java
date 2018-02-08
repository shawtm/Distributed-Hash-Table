package cs455.overlay.tcp;

import java.io.IOException;
import java.util.ArrayList;

public class TCPConnectionsCache extends Thread {
	protected ArrayList<TCPConnection> receivers;
	protected ArrayList<TCPConnection> senders;
	protected TCPServerThread server;
	//blocking queue?
	public TCPConnectionsCache(TCPServerThread server){
		this.server = server;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// start server thread
		server.start();
		// start sending sockets
		
		// poll server for receiver
		while(!interrupted()) {
			try {
				receivers.add(new TCPConnection(server.getSocket()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//stop server
		server.interrupt();
		//close sockets
		for(TCPConnection conn: senders) {
			conn.close();
		}
		for(TCPConnection conn: receivers) {
			conn.close();
		}
	}
}
