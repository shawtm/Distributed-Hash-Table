package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeReportsTaskFinished extends Protocol {
	private byte type;
	private int length;
	private byte[] ip;
	private int port;
	private int id;
	
	public OverlayNodeReportsTaskFinished(byte type, byte[] ip, int port, int id) {
		this.type = type;
		this.length = ip.length;
		this.ip = ip;
		this.port = port;
		this.id = id;
	}
	public OverlayNodeReportsTaskFinished(byte[] bytes) {
		this.unmarshallBytes(bytes);
	}
	
	@Override
	public byte getType() {
		return this.type;
	}
	
	public byte[] getIP() {
		return this.ip;
	}
	public int getLength() {
		return this.length;
	}
	public int getPort() {
		return this.port;
	}
	public int getID() {
		return this.id;
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
			dout.writeInt(this.type);
			dout.writeInt(length);
			dout.write(ip);
			dout.writeInt(this.port);
			dout.writeInt(this.id);
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
			type = (byte) din.readInt();
			length = din.readInt();
			ip = new byte[length];
			din.readFully(ip);
			port = din.readInt();
			id = din.readInt();
			baInputStream.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
}
