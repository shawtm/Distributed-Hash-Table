package cs455.overlay.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import cs455.overlay.wireFormats.Event;

public class TCPConnection {
	//get id?
	private TCPReceiver rec;
	private TCPSender send;
	private Thread receiver;
	
	public TCPConnection(Socket socket, BlockingQueue<Event> events) throws IOException{
		this.rec = new TCPReceiver(socket, events);
		this.send = new TCPSender(socket);
		this.receiver = new Thread(this.rec);
		receiver.start();
		this.send.start();
	}
	public void sendData(Event event){
		this.send.addToSend(event);
	}
	public void close() {
		//TODO
		this.send.interrupt();
	}
}
