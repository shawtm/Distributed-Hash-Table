package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeSendsData extends Protocol {
	private byte type;
	private int sourceID;
	private int destID;
	private int payload;
	private int length;
	private int[] hops;
	
	public OverlayNodeSendsData(byte type, int sourceID, int destID, int payload, int[] hops) {
		this.type = type;
		this.sourceID = sourceID;
		this.destID = destID;
		this.payload = payload;
		this.hops = hops;
	}
	public OverlayNodeSendsData(byte[] bytes) {
		this.unmarshallBytes(bytes);
	}

	@Override
	public byte getType() {
		return this.type;
	}
	public int getSource() {
		return this.sourceID;
	}
	public int getDest() {
		return this.destID;
	}
	public int getPayload() {
		return this.payload;
	}
	public int[] getHops() {
		return this.hops;
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
			dout.writeInt(this.destID);
			dout.writeInt(this.sourceID);
			dout.writeInt(this.payload);
			dout.writeInt(this.hops.length);
			if (hops.length > 0) {
				for (int i : this.hops)
					dout.writeInt(i);
			}
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
			this.destID = din.readInt();
			this.sourceID = din.readInt();
			this.payload = din.readInt();
			this.length = din.readInt();
			length = din.readInt();
			this.hops = new int[length];
			if (hops.length > 0) {
				for (int i = 0; i < length; i++)
					hops[i] = din.readInt();
			}
			baInputStream.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
