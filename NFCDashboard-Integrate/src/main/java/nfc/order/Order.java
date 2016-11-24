package nfc.order;

import org.springframework.integration.annotation.Gateway;

public interface Order {
	 @Gateway(requestChannel="orders")
	 public void process(OrderDetail order);
}
