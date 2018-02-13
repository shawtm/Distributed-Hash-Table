package cs455.overlay.util;

import cs455.overlay.node.Registry;

public class RegistryParser extends InteractiveCommandParser {
	private Registry registry;
	
	public RegistryParser(Registry registry) {
		super();
		this.registry = registry;
	}

	@Override
	public void run() {
		while(!interrupted()) {
			String line = scanner.nextLine();
			parse(line);
		}
		System.out.println("Parser shutdown successfully");
	}

	@Override
	public void parse(String line) {
		//System.out.println(line);
		switch (line) {
		case "list-messaging-nodes":
			registry.printNodes();
			break;
		case "setup-overlay":
			registry.setupOverlay(Integer.parseInt(scanner.next()));
			break;
		case "list-routing-tables":
			registry.printRoutingTables();
		case "start":
			registry.start(Integer.parseInt(scanner.next()));
			break;
		case "exit":
			registry.interrupt();
			registry.shutdown();
			this.interrupt();
			break;
		default:
			System.out.println("Command not recognized");
			break;
		}
	}
}
