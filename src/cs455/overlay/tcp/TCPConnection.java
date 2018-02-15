package cs455.overlay.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.wireFormats.Event;

public class TCPConnection {
	private TCPReceiver rec;
	private TCPSender send;
	private Type type;
	private boolean closed = false;
	
	public enum Type {SENDER, RECEIVER, MESSAGING_TO_REGISTRY, REGISTRY_TO_MESSAGING};
	
	public TCPConnection(Socket socket, LinkedBlockingQueue<Event> events, Type type) throws IOException{
		this.type = type;
		if (this.type != Type.RECEIVER) {
			this.send = new TCPSender(socket);
			this.send.start();
		}
		if (this.type != Type.SENDER) {
			this.rec = new TCPReceiver(socket, events);
			this.rec.start();
		}
	}
	public TCPConnection(Socket socket) throws IOException {
		this.type = Type.SENDER;
		this.send = new TCPSender(socket);
		this.send.start();
	}
	
	public void sendData(Event event){
		//TODO add checks for this not being a receiving thread
		this.send.addToSend(event);
	}
	public synchronized void close() {
		// receivers will close when senders close (maybe?)
		if (closed == false) {
			if(this.send.isAlive()) {
				System.out.println("[Conn] Closing Sender");
				this.send.interrupt();
			}
			if(this.rec.isAlive()) {
				System.out.println("[Conn] Closing Receiver");
				this.rec.interrupt();
				//this.rec.close();
			}
			closed = true;
		}
	}
	public int getPort() {
		return this.rec.getPort();
	}
	public byte[] getIP() {
		return this.rec.getIP();
	}
}
