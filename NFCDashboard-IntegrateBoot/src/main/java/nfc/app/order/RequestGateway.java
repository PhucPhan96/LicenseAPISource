package nfc.app.order;

import org.springframework.integration.annotation.Gateway;

public interface RequestGateway {
	@Gateway(requestChannel="requestChannel")
	String echo(String request);
}
