package cs455.overlay.wireFormats;

public abstract class Protocol implements Event{
	private byte messageType;
	
	public abstract byte[] marshallBytes();
	
}
