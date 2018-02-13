package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class OverlayNodeSendsRegistration extends Protocol {
	private byte type;
	private int length;
	private byte[] ip;
	private int port;
	
	public OverlayNodeSendsRegistration(byte type, int length, byte[] ip, int port) {
		this.type = type;
		this.length = length;
		this.ip = ip;
		this.port = port;
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
	@Override
	public byte[] getBytes() {
		return this.marshallBytes();
	}

	@Override
	public byte[] marshallBytes() {
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		InetAddress addr;
		byte[] identifierBytes = null;
		try {
			addr = InetAddress.getLocalHost();
			identifierBytes = addr.getAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int elementLength = identifierBytes.length;
		try {
			dout.writeInt(this.type);
			dout.writeInt(elementLength);
			dout.write(identifierBytes);
			dout.writeInt(this.port);
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
			byte[] ip = new byte[length];
			din.readFully(ip);
			port = din.readInt();
			baInputStream.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
