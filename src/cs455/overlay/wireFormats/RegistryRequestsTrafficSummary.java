package cs455.overlay.wireFormats;

public class RegistryRequestsTrafficSummary extends Protocol {
	private byte type = Protocol.REGISTRY_REQUESTS_TRAFFIC_SUMMARY;
	public RegistryRequestsTrafficSummary() {
		
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
		byte[] ret = new byte[1];
		ret[0] = this.type;
		return ret;
	}

	@Override
	public Event unmarshallBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

}
