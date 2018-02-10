package cs455.overlay.util;

import cs455.overlay.node.MessagingNode;

public class MessagingParser extends InteractiveCommandParser {
	private MessagingNode node;
	
	public MessagingParser(MessagingNode node) {
		super();
		this.node = node;
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
