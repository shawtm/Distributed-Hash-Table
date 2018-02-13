package cs455.overlay.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import cs455.overlay.wireFormats.Event;

public class TCPSender extends Thread {
	//wrapper class for the sending socket for each messaging node and registry
	private Socket socket;
	private DataOutputStream dout;
	private LinkedBlockingQueue<Event> toSend;
	
	public TCPSender(Socket socket) throws IOException{
		this.socket = socket;
		this.dout = new DataOutputStream(this.socket.getOutputStream());
		this.toSend = new LinkedBlockingQueue<Event>();
	}

	@Override
	public void run() {
		while(!interrupted()) {
			try {
				//maybe do a wait notify here
				dout.write(toSend.take().getBytes());
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addToSend(Event event) {
		try {
			//maybe do a wait notify here
			toSend.put(event);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
