package cs455.overlay.wireFormats;

public class EventFactory {
	//needs to be singleton
	//reads first byte
	//unmarshalls based on the type (switch)
	private final static EventFactory factory = new EventFactory();
	
	private EventFactory() {}
	
	public static EventFactory getInstance() {
		return factory;
	}
	public static Event getEvent(byte[] bytes) {
		// TODO 
		return null;
	}
}
