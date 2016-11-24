package nfc.order;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail {
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	public List<OrderItem> getItems() {
		return this.orderItems;
	}
}
