package nfc.app.order.endpoint;

import nfc.app.order.Order;

public class OrderService {
	public Order saveOrder(Order order)
	{
		System.out.println("SupplID " + order.getSuppl_id());
		System.out.println("Detail " + order.getOrderDetails().size());
		
		return order;
	}
}
