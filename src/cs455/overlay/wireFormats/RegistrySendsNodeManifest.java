package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistrySendsNodeManifest extends Protocol {
	private byte type;
	private int size;
	private int[] ids;
	private byte[][] ips;
	private int[] ports;
	private int[] nodes;
	
	public RegistrySendsNodeManifest(byte type, int size, int[] ids, byte[][] ips, int[] ports, int[] nodes) {
		this.type = type;
		this.size = size;
		this.ids = ids;
		this.ips = ips;
		this.ports = ports;
		this.nodes = nodes;
	}
	public RegistrySendsNodeManifest(byte[] bytes) {
		this.unmarshallBytes(bytes);
	}
	@Override
	public byte getType() {
		return this.type;
	}
	public int[] getIds() {
		return ids;
	}
	public byte[][] getIps() {
		return ips;
	}
	public int[] getPorts() {
		return ports;
	}
	public int[] getNodes() {
		return nodes;
	}
	@Override
	public byte[] getBytes() {
		return this.marshallBytes();
	}

	@Override
	public byte[] marshallBytes() {
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		try {
			dout.write(this.type);
			dout.writeInt(this.size);
			for (int i = 0; i < this.size; i++) {
				dout.writeInt(this.ids[i]);
				dout.writeInt(this.ips[i].length);
				dout.write(this.ips[i],0, ips[i].length);
				dout.writeInt(this.ports[i]);
			}
			dout.write(this.nodes.length);
			for (int i = 0; i < this.nodes.length; i++)
				dout.writeInt(this.nodes[i]);
			dout.flush();
			marshalledBytes = baOutputStream.toByteArray();
			baOutputStream.close();
			dout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return marshalledBytes;
	}

	@Override
	public Event unmarshallBytes(byte[] bytes) {
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(bytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		try {
			type = (byte) din.read();
			size = (byte) din.readInt();
			this.ids = new int[size];
			this.ips = new byte[size][];
			this.ports = new int[size];
			int tempsize = 0;
			byte[] temp;
			for (int i = 0; i < this.size; i++) {
				this.ids[i] = din.readInt();
				tempsize = din.read();
				temp = new byte[tempsize];
				din.readFully(temp);
				this.ips[i] = temp;
				this.ports[i] = din.readInt();
			}
			tempsize = din.readInt();
			this.nodes = new int[tempsize];
			for (int i = 0; i < this.nodes.length; i++)
				this.nodes[i] = din.readInt();
			baInputStream.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
