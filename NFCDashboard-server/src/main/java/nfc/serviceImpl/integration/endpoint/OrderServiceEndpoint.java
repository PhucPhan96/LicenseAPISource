package nfc.serviceImpl.integration.endpoint;

import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.ViewModel.OrderView;
import nfc.service.IOrderService;

public class OrderServiceEndpoint {
	@Autowired
	private IOrderService orderDAO;
	public OrderView saveOrder(OrderView orderView)
	{
            if(!orderDAO.insertOrderView(orderView)){
                orderView.getOrder().setOrder_status("Failed");
            }
            return orderView;
	}
	public OrderView updateOrder(OrderView orderView){
		//orderDAO.updateOrderView(orderView);
		return orderView;
	}
}
