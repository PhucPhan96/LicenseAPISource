package nfc.service;
import java.util.List;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.ViewModel.OrderView;

public interface IOrderService {
	boolean insertOrderView(OrderView orderView);
	boolean updateOrderView(OrderView orderView);
	boolean deleteOrderView(long orderId);
	List<OrderView> getListOrderViewForPos(String username);
	List<OrderDetail> getListOrderDetail(int orderId);
	List<Order> getListOrder(int supplierId);
	Order getOrder(int orderId);
}
