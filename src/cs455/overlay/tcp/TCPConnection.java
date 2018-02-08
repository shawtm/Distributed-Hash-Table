package cs455.overlay.tcp;

import java.io.IOException;
import java.net.Socket;

public class TCPConnection {
	//get id?
	private TCPReceiver rec;
	private TCPSender send;
	
	public TCPConnection(Socket socket) throws IOException{
		this.rec = new TCPReceiver(socket);
		this.send = new TCPSender(socket);
	}
	
	public byte[] getData(){
		return null;
	}
	public void sendData(){
		
	}
	public void close() {
		
	}
}
