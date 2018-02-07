package cs455.overlay.util;

import java.util.Scanner;

public abstract class InteractiveCommandParser extends Thread {
	private Scanner scanner;
	
	public InteractiveCommandParser() {
		scanner = new Scanner(System.in);
	}
	
	public abstract void run();
}
