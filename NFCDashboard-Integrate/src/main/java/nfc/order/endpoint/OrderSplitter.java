package nfc.order.endpoint;

import java.util.List;

import nfc.order.Order;
import nfc.order.OrderDetail;
import nfc.order.OrderItem;

public class OrderSplitter {
	public List<OrderItem> split(OrderDetail order) {
		return order.getItems();
	}
}
