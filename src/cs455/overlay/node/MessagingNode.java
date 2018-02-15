package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import cs455.overlay.routing.RoutingEntry;
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.tcp.TCPConnection;
import cs455.overlay.tcp.TCPConnectionsCache;
import cs455.overlay.util.MessagingParser;
import cs455.overlay.wireFormats.Event;
import cs455.overlay.wireFormats.NodeReportsOverlaySetupStatus;
import cs455.overlay.wireFormats.OverlayNodeReportsTaskFinished;
import cs455.overlay.wireFormats.OverlayNodeReportsTrafficSummary;
import cs455.overlay.wireFormats.OverlayNodeSendsData;
import cs455.overlay.wireFormats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireFormats.OverlayNodeSendsRegistration;
import cs455.overlay.wireFormats.Protocol;
import cs455.overlay.wireFormats.RegistryReportsRegistrationStatus;
import cs455.overlay.wireFormats.RegistryRequestsTaskInitiate;
import cs455.overlay.wireFormats.RegistrySendsNodeManifest;

public class MessagingNode extends Node {
	private RoutingTable rt;
	private int[] nodes;
	private int id;
	private long sentData;
	private long receivedData;
	private int packetsSent;
	private int packetsRouted;
	private int packetsReceived;
	private boolean exit = false;
	
	public MessagingNode(String ip, int port){
		super();
		this.connections = new TCPConnectionsCache(this.server, this.events, TCPConnectionsCache.Type.MESSAGINGNODE);
		this.connections.start();
		MessagingParser parser = new MessagingParser(this);
		rt = new RoutingTable();
		register(ip, port);
		parser.start();
	}
	@Override
	public void run() {
		while(!interrupted() && !exit ) {
			try {
				Event ev = this.events.take();
				switch (ev.getType()) {
				case Protocol.REGISTRY_REPORTS_REGISTRATION_STATUS:
					this.finishRegistration(ev);
					break;
				case Protocol.REGISTRY_REPORTS_DEREGISTRATION_STATUS:
					this.exit = true;
					break;
				case Protocol.REGISTRY_SENDS_NODE_MANIFEST:
					this.setupOverlay(ev);
					break;
				case Protocol.REGISTRY_REQUESTS_TASK_INITIATE:
					this.taskInitiate(ev);
					break;
				case Protocol.OVERLAY_NODE_SENDS_DATA:
					this.handleData(ev);
					break;
				case Protocol.REGISTRY_REQUESTS_TRAFFIC_SUMMARY:
					this.sendRegistryTraffic();
					break;
				default:
					System.out.println("[ERROR] Message Not Recognized! bytecode : " + ev.getType() + " data bytes: " + ev.getBytes());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Interrupting Connections!");
		//close connections
		this.connections.interrupt();
		System.out.println("[Node] Exiting!");
	}
	private void sendRegistryTraffic() {
		this.printCounters();
		OverlayNodeReportsTrafficSummary sum = new OverlayNodeReportsTrafficSummary(Protocol.OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY, this.id, 
				this.packetsSent, this.packetsRouted, this.sentData, this.packetsReceived, this.receivedData);
		rt.sendDataToRegistry(sum);
		this.resetCounters();
	}
	private void handleData(Event event) {
		OverlayNodeSendsData data = new OverlayNodeSendsData(event.getBytes());
		if(this.id == data.getDest()) {
			this.packetsReceived++;
			this.receivedData += data.getPayload();
		}else {
			this.packetsRouted++;
			int[] oldhops = data.getHops();
			int[] hops = new int[oldhops.length+1];
			hops[0] = this.id;
			if(hops.length > 1) {
				for(int i = 0; i< oldhops.length; i++) 
					hops[i+1] = oldhops[i];
			}
			OverlayNodeSendsData newData = new OverlayNodeSendsData(data.getType(), data.getSource(), data.getDest(), data.getPayload(), hops);
			rt.sendData(newData, data.getDest());
		}
	}
	private void resetCounters() {
		this.sentData = 0;
		this.receivedData = 0;
		this.packetsSent = 0;
		this.packetsRouted = 0;
		this.packetsReceived = 0;
	}
	private void taskInitiate(Event event) {
		this.resetCounters();
		RegistryRequestsTaskInitiate task = new RegistryRequestsTaskInitiate(event.getBytes());
		Random r = new Random();
		int count = task.getNumber();
		System.out.println("Starting Task with Size " + count);
		int index = r.nextInt(this.nodes.length);
		while (count > 0) {
			if (nodes[index] != this.id) {
				int payload = r.nextInt();
				sentData += payload;
				packetsSent++;
				OverlayNodeSendsData ov = new OverlayNodeSendsData(Protocol.OVERLAY_NODE_SENDS_DATA, nodes[index], this.id, payload, new int[0]);
				rt.sendData(ov, nodes[index]);
				count--;
			}
			index = r.nextInt(this.nodes.length);
		}
		//have sent all data
		//send task complete to registry
		int port = this.connections.getPort();
		byte[] ip = this.connections.getIP();
		OverlayNodeReportsTaskFinished fin = new OverlayNodeReportsTaskFinished(Protocol.OVERLAY_NODE_REPORTS_TASK_FINISHED, ip, port, this.id);
		rt.sendDataToRegistry(fin);
	}
	private void setupOverlay(Event event) {
		RegistrySendsNodeManifest man = new RegistrySendsNodeManifest(event.getBytes());
		this.nodes = man.getNodes();
		int[] ids = man.getIds();
		int[] ports = man.getPorts();
		byte[][] ips = man.getIps();
		Socket s;
		int retid = this.id;
		int socketCount = 0;
		for(int i = 0; i < ids.length; i++) {
			try {
				s = new Socket(InetAddress.getByAddress(ips[i]), ports[i]);
				rt.addEntry(new RoutingEntry(ids[i], new TCPConnection(s)));
				socketCount++;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				retid = -1;
				System.out.println("[ERROR] connecting to nodes!");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				retid = -1;
				System.out.println("[ERROR] connecting to nodes!");
				e.printStackTrace();
			}
		}
		NodeReportsOverlaySetupStatus reg = new NodeReportsOverlaySetupStatus(Protocol.NODE_REPORTS_OVERLAY_SETUP_STATUS, retid, ("successfully connected to " + socketCount + " nodes!"));
		rt.sendDataToRegistry(reg);
		if(retid != -1) {
			System.out.println("Successfully Connected to all nodes in Routing Table!");
		}
	}
	private void finishRegistration(Event event) {
		RegistryReportsRegistrationStatus re = new RegistryReportsRegistrationStatus(event.getBytes());
		this.id = re.getID();
		System.out.println(re.getInfo());
	}
	public void quit(){
		//deregister
		this.deregister();
		System.out.println("Sending Deregistration Request...");
	}
	public void printCounters() {
		System.out.println("Packets Sent: " + this.packetsSent + "Packets Routed: " + this.packetsRouted + 
				"Packets Received: " + this.packetsReceived + "Sum of Packets Sent: " + this.sentData + "Sum of Packets Received: " + this.receivedData);
	}
	private void deregister() {
		System.out.println("Deregistering now...");
		int thisPort = this.connections.getPort();
		byte[] thisIP = this.connections.getIP();
		Event e = new OverlayNodeSendsDeregistration(Protocol.OVERLAY_NODE_SENDS_DEREGISTRATION, thisIP.length, thisIP, thisPort, this.id);
		rt.sendDataToRegistry(e);
	}
	private void register(String ip, int port){
		System.out.println("Attempting to connect to Registry at " + ip + " on port " + port + "...");
		try {
			Socket s = new Socket(InetAddress.getLocalHost(), port);
			System.out.println("Connected Successfully");
			//Create Connection to store socket
			TCPConnection registryConn = new TCPConnection(s, this.events, TCPConnection.Type.MESSAGING_TO_REGISTRY);
			this.connections.setRegistryConn(registryConn);
			this.rt.addRegistryConn(registryConn);
			//create and send event to register with registry\
			int thisPort = this.connections.getPort();
			byte[] thisIP = this.connections.getIP();
			Event e = new OverlayNodeSendsRegistration(Protocol.OVERLAY_NODE_SENDS_REGISTRATION, thisIP, thisPort);
			rt.sendDataToRegistry(e);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//first argument is the ip address of the registry and second is the port
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("incorrect number of arguments");
		}
		MessagingNode m = new MessagingNode(args[0], Integer.parseInt(args[1]));
		m.run();
	}
}
