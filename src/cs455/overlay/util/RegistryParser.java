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
		String[] strs = line.split(" ");
		if (strs.length > 2) {
			System.out.println("Command not recognized");
		}else {
			switch (strs[0]) {
			case "list-messaging-nodes":
				registry.printNodes();
				break;
			case "setup-overlay":
				registry.setupOverlay(Integer.parseInt(strs[1]));
				break;
			case "list-routing-tables":
				registry.printRoutingTables();
			case "start":
				registry.start(Integer.parseInt(strs[1]));
				break;
			case "exit":
				registry.shutdown();
				registry.interrupt();
				this.interrupt();
				break;
			default:
				System.out.println("Command not recognized");
				break;
			}
		}
	}
}
