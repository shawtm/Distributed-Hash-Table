package cs455.overlay.tcp;

import java.net.Socket;

public class TCPConnection {
	//get id?
	private TCPReceiver rec;
	private TCPSender send;
	
	public TCPConnection(TCPReceiver rec, TCPSender send){
		this.rec = rec;
		this.send = send;
	}
	
	public byte[] getData(){
		return null;
	}
	public void sendData(){
		
	}
}
