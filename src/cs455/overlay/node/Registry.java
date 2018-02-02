package cs455.overlay.node;

import java.util.ArrayList;
import java.util.Random;

public class Registry extends Node {
	
	private ArrayList<Integer> registeredIDs;
	private Random rand;
	
	public Registry(){
		super();
		this.registeredIDs = new ArrayList<Integer>();
		this.rand = new Random();
	}
	
	public void register(){
		int id = addID();
		//send back id
	}
	
	public void degregister(){
		//get id from messaging node
		//remove id form registeredIDs
		//close connection
	}
	
	private int addID(){
		int id = getID();
		registeredIDs.add(id);
		return id;
	}
	
	private int getID(){
		int newID = rand.nextInt(128);
		while (registeredIDs.contains(newID))
			newID = rand.nextInt(128);
		return newID;
	}
}
