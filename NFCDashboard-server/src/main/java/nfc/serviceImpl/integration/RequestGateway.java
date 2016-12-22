package nfc.serviceImpl.integration;

import org.springframework.integration.annotation.Gateway;

import nfc.model.ViewModel.OrderView;

public interface RequestGateway {
	@Gateway(requestChannel="requestChannel")
	String echo(OrderView request);
}
