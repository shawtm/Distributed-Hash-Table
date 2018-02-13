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
		switch (bytes[0]) {
		case Protocol.OVERLAY_NODE_SENDS_REGISTRATION:
			return null;
		case Protocol.REGISTRY_REPORTS_REGISTRATION_STATUS:
			return null;
		case Protocol.OVERLAY_NODE_SENDS_DEREGISTRATION:
			return null;
		case Protocol.REGISTRY_REPORTS_DEREGISTRATION_STATUS:
			return null;
		case Protocol.REGISTRY_SENDS_NODE_MANIFEST:
			return null;
		case Protocol.NODE_REPORTS_OVERLAY_SETUP_STATUS:
			return null;
		case Protocol.REGISTRY_REQUESTS_TASK_INITIATE:
			return null;
		case Protocol.OVERLAY_NODE_SENDS_DATA:
			return null;
		case Protocol.OVERLAY_NODE_REPORTS_TASK_FINISHED:
			return null;
		case Protocol.REGISTRY_REQUESTS_TRAFFIC_SUMMARY:
			return null;
		case Protocol.OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY:
			return null;
		}
		return null;
	}
}
