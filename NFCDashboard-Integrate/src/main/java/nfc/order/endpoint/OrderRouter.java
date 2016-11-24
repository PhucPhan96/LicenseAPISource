package nfc.order.endpoint;

import nfc.order.OrderItem;

public class OrderRouter {
	public String resolveOrderItemChannel(OrderItem orderItem) {
		return (orderItem.isDelivery()) ? "delivery" : "goodFood";
	}
}
