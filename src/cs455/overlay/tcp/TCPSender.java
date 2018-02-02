package cs455.overlay.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSender implements Runnable{
	//wrapper class for the sending socket for each messaging node and registry
	private Socket socket;
	private DataOutputStream dout;
	
	public TCPSender(Socket socket) throws IOException{
		this.socket = socket;
		this.dout = new DataOutputStream(this.socket.getOutputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
