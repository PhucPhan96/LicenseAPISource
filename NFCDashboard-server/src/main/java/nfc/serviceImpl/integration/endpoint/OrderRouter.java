package nfc.serviceImpl.integration.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.websocket.ServerWebSocketContainer;

import nfc.model.ViewModel.OrderView;

public class OrderRouter {
	public String routeOrder(OrderView orderView) {
		System.out.println("vao order router");
		String result = "prepareSendToStore"; 
		switch (orderView.getOrder().getOrder_status()) {
	        case "new":
	            result = "newOrder";
	            break;
	        case "paid":
	            result = "paidOrder";
	            break;     
	        //delivery here
	    }
		return result;
		//return (order.getOrderDetails().size()>1) ? "pos" : "paid";
	}
}
