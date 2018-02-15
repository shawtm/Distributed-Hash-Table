package cs455.overlay.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cs455.overlay.routing.RegistryTable;
import cs455.overlay.tcp.TCPConnection;
import cs455.overlay.tcp.TCPConnectionsCache;
import cs455.overlay.util.RegistryParser;
import cs455.overlay.wireFormats.Event;
import cs455.overlay.wireFormats.NodeReportsOverlaySetupStatus;
import cs455.overlay.wireFormats.OverlayNodeReportsTaskFinished;
import cs455.overlay.wireFormats.OverlayNodeReportsTrafficSummary;
import cs455.overlay.wireFormats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireFormats.OverlayNodeSendsRegistration;
import cs455.overlay.wireFormats.Protocol;
import cs455.overlay.wireFormats.RegistryReportsDeregistrationStatus;
import cs455.overlay.wireFormats.RegistryReportsRegistrationStatus;
import cs455.overlay.wireFormats.RegistryRequestsTaskInitiate;
import cs455.overlay.wireFormats.RegistryRequestsTrafficSummary;
import cs455.overlay.wireFormats.RegistrySendsNodeManifest;

public class Registry extends Node {
	private ArrayList<int[]> rts;
	private ArrayList<Integer> registeredIDs;
	private ArrayList<Integer> finishedIDs;
	private ArrayList<TrafficSummary> summaries;
	private RegistryTable table;
	private Random rand;
	private int connected;
	private boolean run = true;
	private int time = 15;
	
	public Registry(){
		super();
		this.connections = new TCPConnectionsCache(this.server, this.events, TCPConnectionsCache.Type.REGISTRY);
		this.table = new RegistryTable();
		RegistryParser parser = new RegistryParser(this);
		parser.start();
		this.connections.start();
		this.registeredIDs = new ArrayList<>();
		this.rand = new Random();
		this.rts = new ArrayList<>();
	}
	public void run() {
		Event ev;
		while(run) {
			try {
				ev = this.events.take();
				switch (ev.getType()) {
				case Protocol.OVERLAY_NODE_SENDS_REGISTRATION:
					OverlayNodeSendsRegistration on = new OverlayNodeSendsRegistration(ev.getBytes());
					this.register(on.getPort(), on.getIP());
					break;
				case Protocol.OVERLAY_NODE_SENDS_DEREGISTRATION:
					OverlayNodeSendsDeregistration on1 = new OverlayNodeSendsDeregistration(ev.getBytes());
					this.deregister(on1);
					break;
				case Protocol.NODE_REPORTS_OVERLAY_SETUP_STATUS:
					this.overlaySetup(ev);
					break;
				case Protocol.OVERLAY_NODE_REPORTS_TASK_FINISHED:
					this.taskFinish(ev);
					break;
				case Protocol.OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY:
					this.traffic(ev);
				default:
					System.out.println("[ERROR] Message Not Recognized! bytecode : " + ev.getType() + " data bytes: " + ev.getBytes());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Exiting now");
	}
	
	private void traffic(Event event) {
		OverlayNodeReportsTrafficSummary sum = new OverlayNodeReportsTrafficSummary(event.getBytes());
		TrafficSummary summ = new TrafficSummary(sum.getId(), sum.getPacketsSent(), 
				sum.getPacketsRelayed(), sum.getPacketsReceived(), sum.getSumSent(), sum.getSumReceived());
		System.out.println(summ);
		this.summaries.add(summ);
		if (summaries.size() == registeredIDs.size())
			this.printSummedTraffic();
	}
	private void printSummedTraffic() {
		int sent = 0;
		int routed = 0;
		int received = 0;
		long sumSent = 0;
		long sumReceived = 0;
		for (TrafficSummary ts: summaries) {
			sent += ts.sent;
			routed += ts.routed;
			received += ts.received;
			sumSent += ts.sumSent;
			sumReceived += ts.sumreceived;
		}
		System.out.println("Total Summaries for all Nodes:");
		System.out.println("Packets Sent: " + sent + "Packets Routed: " + routed + 
				"Packets Received: " + received + "Sum of Packets Sent: " + sumSent + "Sum of Packets Received: " + sumReceived);
	}
	private void taskFinish(Event event) {
		OverlayNodeReportsTaskFinished fin = new OverlayNodeReportsTaskFinished(event.getBytes());
		finishedIDs.remove((Integer) fin.getID());
		System.out.println("Node: " + fin.getID() + "finished sending " + finishedIDs.size() + " more nodes need to finish");
		if (this.finishedIDs.size() == 0) {
			this.allTaskFinished();
		}
	}
	
	private void allTaskFinished() {
		System.out.println("All nodes finished sending! Waiting " + this.time + " seconds for nodes to finish routing packets...");
		try {
			Thread.sleep((this.time*1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sending traffic requests...");
		this.summaries = new ArrayList<>();
		RegistryRequestsTrafficSummary summ = new RegistryRequestsTrafficSummary();
		for (int i = 0; i < this.registeredIDs.size(); i++) {
			table.getConnection(this.registeredIDs.get(i)).sendData(summ);
		}
	}
	
	private void overlaySetup(Event event) {
		NodeReportsOverlaySetupStatus stat = new NodeReportsOverlaySetupStatus(event.getBytes());
		System.out.println(stat.getInfo());
		if (stat.getID() != -1) {
			System.out.println("Node: " + stat.getID() + " successfully setup its overlay");
			connected++;
		}else {
			System.out.println("[ERROR] A node failed to connect properly please retry setting up overlay");
		}
		
	}
	private void deregister(OverlayNodeSendsDeregistration on) {
		//TODO add checks
		System.out.println("Node: " + on.getID() + " requested to be deregistered!");
		this.registeredIDs.remove((Integer) on.getID());
		TCPConnection conn = table.getConnection(on.getID());
		RegistryReportsDeregistrationStatus event = new RegistryReportsDeregistrationStatus
				(Protocol.REGISTRY_REPORTS_DEREGISTRATION_STATUS, on.getID(), "successfully deregistered node " + (this.registeredIDs.size() - 1) + " nodes left in overlay");
		conn.sendData(event);
		this.table.deregisterNode(on.getID());
		this.connections.removeConnection(conn);
	}
	private void register(int port, byte[] ipAddress){
		int id = addID();
		//find connection
		System.out.println("Registering a node!");
		TCPConnection conn = null;
		ArrayList<TCPConnection> coons = this.connections.getNewConns();
		for (TCPConnection con: coons) {
			if (con.getPort() == port) {
				byte[] bytes = con.getIP();
				boolean toggle = true;
				try {
				for(int i = 0; i < ipAddress.length; i++) {
					if (ipAddress[i] != bytes[i])
						toggle = false;
				}
				}catch(ArrayIndexOutOfBoundsException e) {
					toggle = false;
				}
				if(toggle) {
					conn = con;
					break;
				}
			}
		}
		//Register Node
		table.registerNode(id, port, ipAddress, conn);
		// TODO send back id
		System.out.println("Sending Registration Status");
		RegistryReportsRegistrationStatus re = new RegistryReportsRegistrationStatus
				(Protocol.REGISTRY_REPORTS_REGISTRATION_STATUS, id, ("Registration Successful! " + this.registeredIDs.size() + " nodes registered!"));
		conn.sendData(re);
	}
	
//	public void degregister(int id){
//		
//		//remove id form registeredIDs
//		table.deregisterNode(id);
//		
//	}
	public void printRoutingTables() {
		for (int i = 0; i < this.registeredIDs.size(); i++) {
			System.out.println("Node " + registeredIDs.get(i) + "has the following nodes in its routing table: ");
			for (int j = 0; j < rts.get(i).length; j++) {
				System.out.println("Node: " + this.registeredIDs.get(i) +
						"IP address: " + new String (this.table.getEntry(registeredIDs.get(i)).getIpAddress()) +
						"Port Number: " + this.table.getEntry(registeredIDs.get(i)).getPort());
			}
			System.out.println();
		}
	}
	public void shutdown() {
		System.out.println("Shutting Down...");
		this.connections.interrupt();
		this.run = false;
	}
	public void printNodes() {
		for (int i = 0; i < this.registeredIDs.size(); i++) {
			System.out.println("Node: " + this.registeredIDs.get(i) +
					"IP address: " + new String (this.table.getEntry(registeredIDs.get(i)).getIpAddress()) +
					"Port Number: " + this.table.getEntry(registeredIDs.get(i)).getPort());
		}
	}

	public void setupOverlay(int size) {
		this.connected = 0;
		rts = new ArrayList<>(size);
		Collections.sort(this.registeredIDs);
		for (int i = 0; i < this.registeredIDs.size(); i++) {
			rts.set(i, this.getPositions(i, size));
		}
		RegistrySendsNodeManifest man;
		int[] nodes = this.getNodes();
		for (int i = 0; i < this.registeredIDs.size(); i++) {
			man = new RegistrySendsNodeManifest(Protocol.REGISTRY_SENDS_NODE_MANIFEST, size, 
					this.getPositions(i, size), getIPs(this.getPositions(i, size)), getPorts(this.getPositions(i, size)), nodes);
			table.getConnection(this.registeredIDs.get(i)).sendData(man);
		}
	}
	private int[] getNodes() {
		int[] ret = new int[this.registeredIDs.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = this.registeredIDs.get(i);
		return ret;
	}
	private int[] getPorts(int[] positions) {
		int[] ret = new int[positions.length];
		for(int i = 0; i < ret.length; i++)
			ret[i] = table.getConnection(positions[i]).getPort();
		return ret;
	}
	private byte[][] getIPs(int[] positions) {
		byte[][] ret = new byte[positions.length][];
		for(int i = 0; i < ret.length; i++)
			ret[i] = table.getConnection(positions[i]).getIP();
		return ret;
	}
	private int[] getPositions(int start, int size) {
		int[] ret = new int[size];
		int[] positions = this.getTwos(size);
		for ( int i = 0; i < size; i++) {
			ret[i] = this.registeredIDs.get(((start+positions[i]) % this.registeredIDs.size()));
		}
		return ret;
	}
	private int[] getTwos(int size) {
		int[] ret = new int[size];
		ret[0] = 1;
		for(int i = 1; i < size; i++) {
			ret[i] = 2 * ret[i-1];
		}
		return ret;
	}
	public void start(int rounds) {
		if(connected != this.registeredIDs.size()) {
			System.out.println("[ERROR] cannot start experiement! Some nodes have failed connecting");
			return;
		}
		finishedIDs = new ArrayList<>(this.registeredIDs);
		RegistryRequestsTaskInitiate task;
		for (int i = 0; i < this.registeredIDs.size(); i++) {
			task = new RegistryRequestsTaskInitiate(Protocol.REGISTRY_REQUESTS_TASK_INITIATE, rounds);
			table.getConnection(this.registeredIDs.get(i)).sendData(task);
		}
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
	public void setTime(int minutes) {
		this.time = minutes;
	}
	public static void main(String[] args) {
		Registry r = new Registry();
		r.run();
	}
	private class TrafficSummary{
		private int id;
		private int sent;
		private int routed;
		private int received;
		private long sumSent;
		private long sumreceived;
		public TrafficSummary(int id, int sent, int routed, int received, long sumSent, long sumreceived) {
			this.id = id;
			this.sent = sent;
			this.routed = routed;
			this.received = received;
			this.sumSent = sumSent;
			this.sumreceived = sumreceived;
		}
		public int getId() {
			return id;
		}
		public int getSent() {
			return sent;
		}
		public int getRouted() {
			return routed;
		}
		public int getReceived() {
			return received;
		}
		public long getSumSent() {
			return sumSent;
		}
		public long getSumreceived() {
			return sumreceived;
		}
		public String toString() {
			return new String("Node: " + id + "Packets Sent: " + sent + "Packets Routed: " + routed + 
					"Packets Received: " + received + "Sum of Packets Sent: " + sumSent + "Sum of Packets Received: " + sumreceived);
		}
	}
}
