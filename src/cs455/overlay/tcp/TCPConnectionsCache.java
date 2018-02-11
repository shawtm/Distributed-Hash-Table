package cs455.overlay.tcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cs455.overlay.wireFormats.Event;

public class TCPConnectionsCache extends Thread {
	protected ArrayList<TCPConnection> receivers;
	protected ArrayList<TCPConnection> senders;
	protected TCPConnection registryConn;
	protected TCPServerThread server;
	//blocking queue?
	protected BlockingQueue<Event> events;
	
	public TCPConnectionsCache(TCPServerThread server, BlockingQueue<Event> events){
		this.server = server;
		this.events = events;
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
				receivers.add(new TCPConnection(this.server.getSocket(), this.events, TCPConnection.Type.RECEIVER));
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
		//shouldnt need this as the connections should close when the senders close
//		for(TCPConnection conn: receivers) {
//			conn.close();
//		}
	}
	
}
