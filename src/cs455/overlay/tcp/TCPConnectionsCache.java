package cs455.overlay.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.wireFormats.Event;

public class TCPConnectionsCache extends Thread {
	protected ArrayList<TCPConnection> receivers;
	protected ArrayList<TCPConnection> senders;
	protected TCPConnection registryConn;
	protected TCPServerThread server;
	private Type type;
	//blocking queue?
	protected LinkedBlockingQueue<Event> events;
	private int port;
	private byte[] ip;
	
	public enum Type {REGISTRY, MESSAGINGNODE}
	
	public TCPConnectionsCache(TCPServerThread server, LinkedBlockingQueue<Event> events, Type type){
		this.server = server;
		this.events = events;
		this.type = type;
	}
	public void setRegistryConn(TCPConnection registryConn) {
		this.registryConn = registryConn;
	}
	@Override
	public void run() {
		// start server thread
		server.start();
		// start sending sockets
		this.port = server.getPort();
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			this.ip = addr.getAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// poll server for receiver
		while(!interrupted()) {
			try {
				if(this.type == Type.MESSAGINGNODE)
					receivers.add(new TCPConnection(this.server.getSocket(), this.events, TCPConnection.Type.RECEIVER));
				if(this.type == Type.REGISTRY)
					senders.add(new TCPConnection(this.server.getSocket(), this.events, TCPConnection.Type.REGISTRY_TO_MESSAGING));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//stop server
		System.out.println("Shutting down server..");
		server.interrupt();
		server.shutdown();
		System.out.println("Server Shutdown successfully");
		//close sockets
//		for(TCPConnection conn: senders) {
//			conn.close();
//		}
		//shouldnt need this as the connections should close when the senders close
//		for(TCPConnection conn: receivers) {
//			conn.close();
//		}
	}
	public byte[] getIP() {
		return this.ip;
	}
	public int getPort() {
		return this.port;
	}
}
