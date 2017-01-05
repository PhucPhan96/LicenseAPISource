package nfc.serviceImpl.integration.endpoint;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import nfc.model.ViewModel.OrderView;

public class OrderRouter {
	/*@Autowired
	ServerWebSocketContainer serverWebSocketContainer;*/
	public String routeOrder(OrderView orderView) {
		String result = "prepareSendToStore"; 
		if(orderView.getOrder().getOrder_status().trim().equals("new")){
			result = "newOrder";
		}
		else if(orderView.getOrder().getOrder_status().trim().equals("paid")){
			result = "paidOrder";
		}
		/*switch (orderView.getOrder().getOrder_status()) {
	        case "new":
	            result = "newOrder";
	            break;
	        case "paid":
	            result = "paidOrder";
	            break;     
	        //delivery here
	    }*/
		System.out.println(result);
		return result;
		//return (order.getOrderDetails().size()>1) ? "pos" : "paid";
	}
}
