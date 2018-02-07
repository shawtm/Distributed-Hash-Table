package cs455.overlay.util;

public class MessagingParser extends InteractiveCommandParser {
	
	
	public MessagingParser() {
		super();
	}

	@Override
	public void run() {
		while(!interrupted()) {
			this.parse();
		}
		//close all sockets
	}

	@Override
	public void parse() {
		// TODO Auto-generated method stub
		String line = scanner.next();
		if(line.equals("exit")) {
			this.interrupt();
		}
	}
}
