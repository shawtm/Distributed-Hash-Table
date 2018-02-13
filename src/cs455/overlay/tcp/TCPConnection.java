package cs455.overlay.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import cs455.overlay.wireFormats.Event;

public class TCPConnection {
	private TCPReceiver rec;
	private TCPSender send;
	private Thread receiver;
	private Type type;
	
	public enum Type {SENDER, RECEIVER, MESSAGING_TO_REGISTRY, REGISTRY_TO_MESSAGING};
	
	public TCPConnection(Socket socket, LinkedList<Event> events, Type type) throws IOException{
		this.type = type;
		this.rec = new TCPReceiver(socket, events);
		if (this.type != Type.RECEIVER) {
			this.send = new TCPSender(socket);
			this.send.start();
		}
		if (this.type != Type.SENDER) {
			this.receiver = new Thread(this.rec);
			receiver.start();
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
	public void close() {
		// receivers will close when senders close (maybe?)
		this.send.interrupt();
	}
}
