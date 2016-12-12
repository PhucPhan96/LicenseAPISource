package nfc.serviceImpl.integration.endpoint;

import nfc.model.Order;

public class OrderRouter {
	public String redirectOrderChannel(Order order) {
		return "";
		//return (order.getOrderDetails().size()>1) ? "pos" : "paid";
	}
}
