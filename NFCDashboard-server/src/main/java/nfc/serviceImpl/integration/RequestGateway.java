package nfc.serviceImpl.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;

public interface RequestGateway {
	@Gateway(requestChannel="requestChannel")
	String echo(String request);
}
