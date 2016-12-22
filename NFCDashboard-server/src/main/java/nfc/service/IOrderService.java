package nfc.service;
import nfc.model.ViewModel.OrderView;

public interface IOrderService {
	boolean insertOrderView(OrderView orderView);
	boolean updateOrderView(OrderView orderView);
	boolean deleteOrderView(int orderId);
}
