package nfc.app.order.endpoint;

import nfc.app.order.Order;

public class OrderRouter {
	public String redirectOrderChannel(Order order) {
		
		return (order.getOrderDetails().size()>1) ? "pos" : "paid";
	}
}
