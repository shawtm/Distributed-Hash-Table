package cs455.overlay.wireFormats;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class RegistryReportsRegistrationStatus extends Protocol {
	private byte type;
	private int id;
	private int length;
	@SuppressWarnings("unused")
	private byte[] information;
	private String info;
	
	public RegistryReportsRegistrationStatus(byte type, int id, String info) {
		this.type = type;
		this.id = id;
		this.info = info;
	}
	@Override
	public byte getType() {
		return this.type;
	}
	public int getID() {
		return this.id;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event unmarshallBytes(byte[] bytes) {
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(bytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		try {
			type = (byte) din.readInt();
			id = din.readInt();
			length = din.readInt();
			information = new byte[length];
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
