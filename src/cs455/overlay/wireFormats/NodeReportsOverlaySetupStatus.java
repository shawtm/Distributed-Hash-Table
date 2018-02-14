package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NodeReportsOverlaySetupStatus extends Protocol {

	private byte type;
	private int id;
	private int length;
	private String info;
	
	public NodeReportsOverlaySetupStatus(byte type, int id, String info) {
		this.type = type;
		this.length = info.getBytes().length;
		this.id = id;
		this.info = info;
	}
	public NodeReportsOverlaySetupStatus(byte[] bytes) {
		this.unmarshallBytes(bytes);
	}
	
	@Override
	public byte getType() {
		return this.type;
	}
	
	public int getID() {
		return this.id;
	}
	public int getLength() {
		return this.length;
	}
	public String getInfo() {
		return this.info;
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
			dout.writeInt(this.id);
			dout.writeInt(this.length);
			dout.write(info.getBytes());
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
			id = (byte) din.readInt();
			length = din.readInt();
			byte[] information = new byte[length];
			din.readFully(information);
			info = new String(information);
			baInputStream.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
