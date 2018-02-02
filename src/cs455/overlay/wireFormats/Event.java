package cs455.overlay.wireFormats;

public interface Event {
	
	public byte getType();
	
	public byte[] getBytes();
}
