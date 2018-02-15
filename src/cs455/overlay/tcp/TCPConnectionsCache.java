package cs455.overlay.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.wireFormats.Event;

public class TCPConnectionsCache extends Thread {
	protected ArrayList<TCPConnection> receivers;
	protected ArrayList<TCPConnection> senders;
	private ArrayList<TCPConnection> newConns;
	protected TCPConnection registryConn = null;
	protected TCPServerThread server;
	private Type type;
	//blocking queue?
	protected LinkedBlockingQueue<Event> events;
	private int port;
	private byte[] ip;
	
	public enum Type {REGISTRY, MESSAGINGNODE}
	
	public TCPConnectionsCache(TCPServerThread server, LinkedBlockingQueue<Event> events, Type type){
		this.receivers = new ArrayList<>();
		this.senders = new ArrayList<>();
		this.newConns = new ArrayList<>();
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
				Socket s = this.server.getSocket();
				if(this.type == Type.MESSAGINGNODE && !interrupted()) {
					receivers.add(new TCPConnection(s , this.events, TCPConnection.Type.RECEIVER));
				}
				if(this.type == Type.REGISTRY && !interrupted()) {
					TCPConnection newconn =new TCPConnection(s, this.events, TCPConnection.Type.REGISTRY_TO_MESSAGING);
					senders.add(newconn);
					this.newConns.add(newconn);
					System.out.println("created new Connection to node");
				}
			} catch (IOException e) {
			} catch (InterruptedException e) {
				break;
			}
		}
		//stop server
		//System.out.println("Shutting down server..");
		server.interrupt();
		server.shutdown();
		System.out.println("Server Shutdown successfully");
		//close sockets
		if (this.type != Type.REGISTRY && this.registryConn != null) {
			System.out.println("[Cache] Closing Registry Connection!");
			this.registryConn.close();
		}
		System.out.println("[Cache] Closing senders!");
		for(TCPConnection conn: senders) {
			conn.close();
		}
		System.out.println("[Cache] Closing Receivers!");
		//shouldnt need this as the connections should close when the senders close
		for(TCPConnection conn: receivers) {
			conn.close();
		}
	}
	public byte[] getIP() {
		return this.ip;
	}
	public int getPort() {
		return this.port;
	}
	public ArrayList<TCPConnection> getNewConns(){
		return this.newConns;
	}
	public synchronized void removeConnection(TCPConnection conn) {
		conn.close();
		this.senders.remove(conn);
	}
}
