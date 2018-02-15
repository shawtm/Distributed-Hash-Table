package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class OverlayNodeReportsTrafficSummary extends Protocol {
	private byte type;
	private int id;
	private int packetsSent;
	private int packetsRelayed;
	private long sumSent;
	private int packetsReceived;
	private long sumReceived;
	
	public OverlayNodeReportsTrafficSummary(byte type, int id, int packetsSent, int packetsRelayed, long sumSent,
			int packetsReceived, long sumReceived) {
		this.type = type;
		this.id = id;
		this.packetsSent = packetsSent;
		this.packetsRelayed = packetsRelayed;
		this.sumSent = sumSent;
		this.packetsReceived = packetsReceived;
		this.sumReceived = sumReceived;
	}
	
	public OverlayNodeReportsTrafficSummary(byte[] bytes) {
		this.unmarshallBytes(bytes);
	}
	public int getId() {
		return id;
	}

	public int getPacketsSent() {
		return packetsSent;
	}

	public int getPacketsRelayed() {
		return packetsRelayed;
	}

	public long getSumSent() {
		return sumSent;
	}

	public int getPacketsReceived() {
		return packetsReceived;
	}

	public long getSumReceived() {
		return sumReceived;
	}

	@Override
	public byte getType() {
		return this.type;
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
			dout.writeInt(id);
			dout.writeInt(packetsSent);
			dout.writeInt(packetsRelayed);
			dout.writeLong(this.sumSent);
			dout.writeInt(packetsReceived);
			dout.writeLong(this.sumReceived);
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
			id = din.readInt();
			packetsSent = din.readInt();
			packetsRelayed = din.readInt();
			sumSent = din.readLong();
			packetsReceived = din.readInt();
			sumReceived = din.readLong();
			baInputStream.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
