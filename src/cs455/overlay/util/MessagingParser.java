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
			String line = scanner.next();
			parse(line);
		}
		//close all sockets
	}

	@Override
	public void parse(String line) {
		switch (line) {
		case "print-counters-and-diagnostics":
			node.printCounters();
			break;
		case "exit-overlay":
			node.quit();
			this.interrupt();
			break;
		default:
			System.out.println("Command not recognized");
			break;
		}
	}
}
