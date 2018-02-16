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
	private boolean exit = false;
	
	public TCPSender(Socket socket) throws IOException{
		this.socket = socket;
		this.dout = new DataOutputStream(this.socket.getOutputStream());
		this.toSend = new LinkedBlockingQueue<Event>();
	}

	@Override
	public void run() {
		while(!interrupted() && !exit) {
			try {
				//maybe do a wait notify here
				Event e = toSend.take();
				synchronized(this) {
					System.out.println("[Sender] Type is " + e.getType());
					dout.writeInt(e.getBytes().length);
					dout.write(e.getBytes(), 0 , e.getBytes().length);
					dout.flush();
				}
				System.out.println("[Sender] successfully sent!");
			} catch (IOException e) {
				exit = true;
				break;
			} catch (InterruptedException e1) {
				exit = true;
				break;
				//e1.printStackTrace();
			}
		}
		if(socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("[Sender] Sender is exiting!");
	}
	
	public synchronized void close() {
		exit = true;
		try {
			if (!socket.isClosed()) {
				socket.close();
				dout.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public synchronized void addToSend(Event event) {
		try {
			//maybe do a wait notify here
			toSend.put(event);
		} catch (InterruptedException e) {
		}

	}
}
