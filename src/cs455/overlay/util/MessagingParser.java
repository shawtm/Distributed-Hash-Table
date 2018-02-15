package cs455.overlay.util;

import cs455.overlay.node.MessagingNode;

public class MessagingParser extends InteractiveCommandParser {
	private MessagingNode node;
	private boolean exit = false;
	
	public MessagingParser(MessagingNode node) {
		super();
		this.node = node;
	}

	@Override
	public void run() {
		while(!interrupted() && !exit) {
			String line = scanner.next();
			parse(line);
		}
		//close all sockets
		System.out.println("[Parser] Exiting!");
	}

	@Override
	public void parse(String line) {
		switch (line) {
		case "print-counters-and-diagnostics":
			node.printCounters();
			break;
		case "exit-overlay":
			node.quit();
			exit = true;
			break;
		default:
			System.out.println("Command not recognized");
			break;
		}
	}
}
