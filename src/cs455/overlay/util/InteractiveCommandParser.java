package cs455.overlay.util;

import java.util.Scanner;

public abstract class InteractiveCommandParser extends Thread {
	protected Scanner scanner;
	
	public InteractiveCommandParser() {
		scanner = new Scanner(System.in);
	}
	
	public abstract void run();
	
	public abstract void parse(String line);
}
