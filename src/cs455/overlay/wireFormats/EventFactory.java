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
	public synchronized static Event getEvent(byte[] bytes) {
		System.out.println("[EventFatory] type is " + bytes[0]);
		switch (bytes[0]) {
		case Protocol.OVERLAY_NODE_SENDS_REGISTRATION:
			return new OverlayNodeSendsRegistration(bytes);
		case Protocol.REGISTRY_REPORTS_REGISTRATION_STATUS:
			return new RegistryReportsRegistrationStatus(bytes);
		case Protocol.OVERLAY_NODE_SENDS_DEREGISTRATION:
			return new OverlayNodeSendsDeregistration(bytes);
		case Protocol.REGISTRY_REPORTS_DEREGISTRATION_STATUS:
			return new RegistryReportsDeregistrationStatus(bytes);
		case Protocol.REGISTRY_SENDS_NODE_MANIFEST:
			return new RegistrySendsNodeManifest(bytes);
		case Protocol.NODE_REPORTS_OVERLAY_SETUP_STATUS:
			return new NodeReportsOverlaySetupStatus(bytes);
		case Protocol.REGISTRY_REQUESTS_TASK_INITIATE:
			return new RegistryRequestsTaskInitiate(bytes);
		case Protocol.OVERLAY_NODE_SENDS_DATA:
			return new OverlayNodeSendsData(bytes);
		case Protocol.OVERLAY_NODE_REPORTS_TASK_FINISHED:
			return new OverlayNodeReportsTaskFinished(bytes);
		case Protocol.REGISTRY_REQUESTS_TRAFFIC_SUMMARY:
			return new RegistryRequestsTrafficSummary();
		case Protocol.OVERLAY_NODE_REPORTS_TRAFFIC_SUMMARY:
			return new OverlayNodeReportsTrafficSummary(bytes);
		}
		return null;
	}
}
